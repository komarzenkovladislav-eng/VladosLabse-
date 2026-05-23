package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ScheduleDatabase.getDatabase(application)
    private val repository = ScheduleRepository(database)

    // UI Navigation State
    private val _selectedTab = MutableStateFlow(0) // 0: Schedule, 1: Homework, 2: Settings
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Day Selection State (1: ПН, 2: ВТ, 3: СР, 4: ЧТ, 5: ПТ)
    private val _scheduleDay = MutableStateFlow(1)
    val scheduleDay: StateFlow<Int> = _scheduleDay.asStateFlow()

    private val _homeworkDay = MutableStateFlow(1)
    val homeworkDay: StateFlow<Int> = _homeworkDay.asStateFlow()

    // Neon Spark/Glow selection (lessonId)
    private val _selectedLessonId = MutableStateFlow<Int?>(null)
    val selectedLessonId: StateFlow<Int?> = _selectedLessonId.asStateFlow()

    // Modals
    private val _showAddLessonDialog = MutableStateFlow(false)
    val showAddLessonDialog: StateFlow<Boolean> = _showAddLessonDialog.asStateFlow()

    // Profile State (loads from DB)
    val profileState: StateFlow<UserProfile?> = repository.profileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Lessons State (loads reactive list)
    val _allLessons = repository.allLessonsFlow
    val lessonsState: StateFlow<List<Lesson>> = repository.allLessonsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Homework State (loads reactive list)
    val homeworkState: StateFlow<List<Homework>> = repository.allHomeworkFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Set days to the current real day if it is ПН-ПТ (1-5 in calendar)
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK) // Calendar.SUNDAY = 1, MONDAY = 2...
        // Convert to our format: 1=ПН, 2=ВТ, 3=СР, 4=ЧТ, 5=ПТ
        val mappedDay = when (day) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> 1 // default to Monday for weekends
        }
        _scheduleDay.value = mappedDay
        _homeworkDay.value = mappedDay
    }

    fun selectTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun selectScheduleDay(day: Int) {
        _scheduleDay.value = day
    }

    fun selectHomeworkDay(day: Int) {
        _homeworkDay.value = day
    }

    fun toggleLessonGlow(lessonId: Int) {
        if (_selectedLessonId.value == lessonId) {
            _selectedLessonId.value = null
        } else {
            _selectedLessonId.value = lessonId
        }
    }

    fun setShowAddLessonDialog(show: Boolean) {
        _showAddLessonDialog.value = show
    }

    // --- Database Actions ---

    fun saveUserProfile(firstName: String, lastName: String, avatarId: Int, isDarkTheme: Boolean = true, language: String = "RU") {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfile(
                    id = 1,
                    firstName = firstName,
                    lastName = lastName,
                    avatarId = avatarId,
                    isDarkTheme = isDarkTheme,
                    language = language
                )
            )
        }
    }

    fun updateProfileTheme(isDark: Boolean) {
        viewModelScope.launch {
            val current = repository.getProfile()
            if (current != null) {
                repository.saveProfile(current.copy(isDarkTheme = isDark))
            } else {
                repository.saveProfile(UserProfile(firstName = "", lastName = "", avatarId = 0, isDarkTheme = isDark))
            }
        }
    }

    fun updateProfileLanguage(lang: String) {
        viewModelScope.launch {
            val current = repository.getProfile()
            if (current != null) {
                repository.saveProfile(current.copy(language = lang))
            } else {
                repository.saveProfile(UserProfile(firstName = "", lastName = "", avatarId = 0, language = lang))
            }
        }
    }

    fun addLesson(day: Int, lessonNum: Int, subject: String, start: String, end: String) {
        viewModelScope.launch {
            // Delete any existing lesson in that slot first to prevent duplicate slots
            repository.deleteLessonAtSlot(day, lessonNum)
            repository.insertLesson(
                Lesson(
                    dayOfWeek = day,
                    lessonNumber = lessonNum,
                    subjectName = subject,
                    startTime = start,
                    endTime = end
                )
            )
        }
    }

    fun deleteLesson(id: Int) {
        viewModelScope.launch {
            repository.deleteLesson(id)
        }
    }

    fun saveHomework(day: Int, lessonNum: Int, assigned: String, hwForTomorrow: String, todayDone: Boolean, tomorrowDone: Boolean) {
        viewModelScope.launch {
            val existing = repository.allHomeworkFlow.firstOrNull()?.find { it.dayOfWeek == day && it.lessonNumber == lessonNum }
            val item = Homework(
                id = existing?.id ?: 0,
                dayOfWeek = day,
                lessonNumber = lessonNum,
                assignedToday = assigned,
                homeworkTomorrow = hwForTomorrow,
                isTodayCompleted = todayDone,
                isTomorrowCompleted = tomorrowDone
            )
            repository.insertHomework(item)
        }
    }

    // --- "Next Lesson" Computations ---
    data class NextLessonStatus(
        val lessonName: String,
        val timeLabel: String,
        val statusTextKey: String, // String resource ID equivalent key
        val relativeMinutes: Int
    )

    fun getNextLessonStatus(lessons: List<Lesson>, lang: String): NextLessonStatus? {
        if (lessons.isEmpty()) return null

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        // Convert to our format (1-5)
        val mappedDay = when (currentDay) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            else -> return null // weekends, no class
        }

        // Filter lessons for today
        val todayLessons = lessons.filter { it.dayOfWeek == mappedDay }.sortedBy { it.lessonNumber }
        if (todayLessons.isEmpty()) return null

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val nowTimeString = sdf.format(calendar.time)
        val nowMinutes = parseTimeToMinutes(nowTimeString)

        for (lesson in todayLessons) {
            val startMinutes = parseTimeToMinutes(lesson.startTime)
            val endMinutes = parseTimeToMinutes(lesson.endTime)

            if (nowMinutes < startMinutes) {
                // Lesson starts in standard minutes
                val diff = startMinutes - nowMinutes
                return NextLessonStatus(
                    lessonName = lesson.subjectName,
                    timeLabel = "${lesson.startTime} - ${lesson.endTime}",
                    statusTextKey = "starts_in",
                    relativeMinutes = diff
                )
            } else if (nowMinutes in startMinutes..endMinutes) {
                // Lesson is currently active
                val diff = endMinutes - nowMinutes
                return NextLessonStatus(
                    lessonName = lesson.subjectName,
                    timeLabel = "${lesson.startTime} - ${lesson.endTime}",
                    statusTextKey = "lesson_active",
                    relativeMinutes = diff
                )
            }
        }

        // If we reached here, all lessons for today have finished
        return null
    }

    private fun parseTimeToMinutes(time: String): Int {
        return try {
            val parts = time.split(":")
            val hours = parts[0].toInt()
            val mins = parts[1].toInt()
            hours * 60 + mins
        } catch (e: Exception) {
            0
        }
    }
}
