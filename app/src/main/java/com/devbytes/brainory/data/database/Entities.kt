package com.devbytes.brainory.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// User entity
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

// Subject entity
@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val colorHex: String = "#6366F1",
    val targetHours: Float = 0f
)

// StudySession entity
@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val date: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val notes: String = ""
)

// Schedule entity
@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subjectId: Int,
    val dateTime: Long,
    val isCompleted: Boolean = false
)

// Flashcard entity
@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val question: String,
    val answer: String,
    val timesReviewed: Int = 0,
    val timesCorrect: Int = 0
)

// Drawing entity
@Entity(tableName = "drawings")
data class Drawing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val imageData: ByteArray,
    val dateCreated: Long = System.currentTimeMillis(),
    val title: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Drawing) return false
        return id == other.id && title == other.title && subjectId == other.subjectId
    }
    override fun hashCode(): Int = id
}