package com.example.data

import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val db: ScheduleDatabase) {
    private val userDao = db.userDao()
    private val lessonDao = db.lessonDao()
    private val homeworkDao = db.homeworkDao()

    // Profile
    val profileFlow: Flow<UserProfile?> = userDao.getProfileFlow()
    suspend fun getProfile(): UserProfile? = userDao.getProfile()
    suspend fun saveProfile(profile: UserProfile) = userDao.saveProfile(profile)

    // Lessons
    val allLessonsFlow: Flow<List<Lesson>> = lessonDao.getAllLessonsFlow()
    fun getLessonsForDay(day: Int): Flow<List<Lesson>> = lessonDao.getLessonsForDayFlow(day)
    suspend fun insertLesson(lesson: Lesson) = lessonDao.insertLesson(lesson)
    suspend fun deleteLesson(id: Int) = lessonDao.deleteLesson(id)
    suspend fun deleteLessonAtSlot(day: Int, lessonNumber: Int) = lessonDao.deleteLessonAtSlot(day, lessonNumber)

    // Homework
    val allHomeworkFlow: Flow<List<Homework>> = homeworkDao.getAllHomeworkFlow()
    fun getHomeworkForDay(day: Int): Flow<List<Homework>> = homeworkDao.getHomeworkForDayFlow(day)
    suspend fun insertHomework(homework: Homework) = homeworkDao.insertHomework(homework)
}
