package com.devbytes.brainory.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devbytes.brainory.data.dao.*

@Database(
    entities = [User::class, Subject::class, StudySession::class,
                Schedule::class, Flashcard::class, Drawing::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun drawingDao(): DrawingDao
}