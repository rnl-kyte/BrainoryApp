package com.devbytes.brainory.data.dao

import androidx.room.*
import com.devbytes.brainory.data.database.*
import kotlinx.coroutines.flow.Flow

// ── UserDao ───────────────────────────────────────────────────────────────────
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getFirstUser(): User?
}

// ── SubjectDao ────────────────────────────────────────────────────────────────
@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: Subject): Long

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun getAll(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getById(id: Int): Subject?
}

// ── StudySessionDao ───────────────────────────────────────────────────────────
@Dao
interface StudySessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: StudySession): Long

    @Update
    suspend fun update(session: StudySession)

    @Delete
    suspend fun delete(session: StudySession)

    @Query("SELECT * FROM study_sessions ORDER BY date DESC")
    fun getAll(): Flow<List<StudySession>>

    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY date DESC")
    fun getBySubject(subjectId: Int): Flow<List<StudySession>>

    @Query("""
        SELECT * FROM study_sessions
        WHERE date >= :startOfDay AND date <= :endOfDay
        ORDER BY date DESC
    """)
    fun getByDate(startOfDay: Long, endOfDay: Long): Flow<List<StudySession>>

    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE subjectId = :subjectId")
    suspend fun getTotalMinutesForSubject(subjectId: Int): Int?

    @Query("""
        SELECT SUM(durationMinutes) FROM study_sessions
        WHERE date >= :startOfDay AND date <= :endOfDay
    """)
    suspend fun getTodayTotalMinutes(startOfDay: Long, endOfDay: Long): Int?
}

// ── ScheduleDao ───────────────────────────────────────────────────────────────
@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule): Long

    @Update
    suspend fun update(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("SELECT * FROM schedules ORDER BY dateTime ASC")
    fun getAll(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE dateTime >= :start AND dateTime <= :end ORDER BY dateTime ASC")
    fun getByDateRange(start: Long, end: Long): Flow<List<Schedule>>
}

// ── FlashcardDao ──────────────────────────────────────────────────────────────
@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flashcard: Flashcard): Long

    @Update
    suspend fun update(flashcard: Flashcard)

    @Delete
    suspend fun delete(flashcard: Flashcard)

    @Query("SELECT * FROM flashcards WHERE subjectId = :subjectId ORDER BY id DESC")
    fun getBySubject(subjectId: Int): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards ORDER BY id DESC")
    fun getAll(): Flow<List<Flashcard>>
}

// ── DrawingMeta ───────────────────────────────────────────────────────────────
data class DrawingMeta(
    val id: Int,
    val subjectId: Int,
    val dateCreated: Long,
    val title: String,
    val dataSize: Int
)

// ── DrawingDao ────────────────────────────────────────────────────────────────
@Dao
interface DrawingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drawing: Drawing): Long

    @Delete
    suspend fun delete(drawing: Drawing)

    @Query("SELECT id, subjectId, dateCreated, title, length(imageData) as dataSize FROM drawings ORDER BY dateCreated DESC")
    fun getAllMeta(): Flow<List<DrawingMeta>>

    @Query("SELECT * FROM drawings WHERE id = :id")
    suspend fun getById(id: Int): Drawing?

    @Query("SELECT * FROM drawings WHERE subjectId = :subjectId ORDER BY dateCreated DESC")
    fun getBySubject(subjectId: Int): Flow<List<Drawing>>
}