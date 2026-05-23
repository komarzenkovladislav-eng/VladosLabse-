package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- ENTITIES ---

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val firstName: String,
    val lastName: String,
    val avatarId: Int, // Index of pre-built avatar (0-5)
    val isDarkTheme: Boolean = true, // Dark AI Mode
    val language: String = "RU" // "RU" or "UA"
)

@Entity(
    tableName = "lessons",
    indices = [Index(value = ["dayOfWeek", "lessonNumber"], unique = true)]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: Int, // 1 = ПН, 2 = ВТ, 3 = СР, 4 = ЧТ, 5 = ПТ
    val lessonNumber: Int, // 1 to 8
    val subjectName: String,
    val startTime: String, // e.g. "08:30"
    val endTime: String // e.g. "09:15"
)

@Entity(
    tableName = "homework",
    indices = [Index(value = ["dayOfWeek", "lessonNumber"], unique = true)]
)
data class Homework(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: Int, // 1 to 5
    val lessonNumber: Int, // 1 to 8
    val assignedToday: String = "", // "Задали на сегодня"
    val homeworkTomorrow: String = "", // "Домашка на завтра"
    val isTodayCompleted: Boolean = false,
    val isTomorrowCompleted: Boolean = false
)

// --- DAOs ---

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfile)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY dayOfWeek ASC, lessonNumber ASC")
    fun getAllLessonsFlow(): Flow<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE dayOfWeek = :day ORDER BY lessonNumber ASC")
    fun getLessonsForDayFlow(day: Int): Flow<List<Lesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Query("DELETE FROM lessons WHERE id = :id")
    suspend fun deleteLesson(id: Int)

    @Query("DELETE FROM lessons WHERE dayOfWeek = :day AND lessonNumber = :lessonNumber")
    suspend fun deleteLessonAtSlot(day: Int, lessonNumber: Int)
}

@Dao
interface HomeworkDao {
    @Query("SELECT * FROM homework ORDER BY dayOfWeek ASC, lessonNumber ASC")
    fun getAllHomeworkFlow(): Flow<List<Homework>>

    @Query("SELECT * FROM homework WHERE dayOfWeek = :day ORDER BY lessonNumber ASC")
    fun getHomeworkForDayFlow(day: Int): Flow<List<Homework>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(homework: Homework)
}

// --- DATABASE ---

@Database(
    entities = [UserProfile::class, Lesson::class, Homework::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun lessonDao(): LessonDao
    abstract fun homeworkDao(): HomeworkDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        fun getDatabase(context: android.content.Context): ScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
