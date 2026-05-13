package com.devbytes.brainory.data.repository

import com.devbytes.brainory.data.dao.*
import com.devbytes.brainory.data.database.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val dao: UserDao) {
    suspend fun register(user: User): Long = dao.insert(user)
    suspend fun login(email: String, password: String): User? = dao.login(email, password)
    suspend fun findByEmail(email: String): User? = dao.findByEmail(email)
    suspend fun getFirstUser(): User? = dao.getFirstUser()
}

@Singleton
class SubjectRepository @Inject constructor(private val dao: SubjectDao) {
    fun getAll(): Flow<List<Subject>> = dao.getAll()
    suspend fun insert(subject: Subject): Long = dao.insert(subject)
    suspend fun update(subject: Subject) = dao.update(subject)
    suspend fun delete(subject: Subject) = dao.delete(subject)
    suspend fun getById(id: Int): Subject? = dao.getById(id)
}

@Singleton
class StudySessionRepository @Inject constructor(private val dao: StudySessionDao) {
    fun getAll(): Flow<List<StudySession>> = dao.getAll()
    fun getBySubject(subjectId: Int): Flow<List<StudySession>> = dao.getBySubject(subjectId)
    fun getByDate(startOfDay: Long, endOfDay: Long): Flow<List<StudySession>> = dao.getByDate(startOfDay, endOfDay)
    suspend fun insert(session: StudySession): Long = dao.insert(session)
    suspend fun update(session: StudySession) = dao.update(session)
    suspend fun delete(session: StudySession) = dao.delete(session)
    suspend fun getTodayTotal(start: Long, end: Long): Int = dao.getTodayTotalMinutes(start, end) ?: 0
}

@Singleton
class ScheduleRepository @Inject constructor(private val dao: ScheduleDao) {
    fun getAll(): Flow<List<Schedule>> = dao.getAll()
    fun getByDateRange(start: Long, end: Long): Flow<List<Schedule>> = dao.getByDateRange(start, end)
    suspend fun insert(schedule: Schedule): Long = dao.insert(schedule)
    suspend fun update(schedule: Schedule) = dao.update(schedule)
    suspend fun delete(schedule: Schedule) = dao.delete(schedule)
}

@Singleton
class FlashcardRepository @Inject constructor(private val dao: FlashcardDao) {
    fun getAll(): Flow<List<Flashcard>> = dao.getAll()
    fun getBySubject(subjectId: Int): Flow<List<Flashcard>> = dao.getBySubject(subjectId)
    suspend fun insert(flashcard: Flashcard): Long = dao.insert(flashcard)
    suspend fun update(flashcard: Flashcard) = dao.update(flashcard)
    suspend fun delete(flashcard: Flashcard) = dao.delete(flashcard)
}

@Singleton
class DrawingRepository @Inject constructor(private val dao: DrawingDao) {
    fun getAllMeta(): Flow<List<DrawingMeta>> = dao.getAllMeta()
    fun getBySubject(subjectId: Int): Flow<List<Drawing>> = dao.getBySubject(subjectId)
    suspend fun getById(id: Int): Drawing? = dao.getById(id)
    suspend fun insert(drawing: Drawing): Long = dao.insert(drawing)
    suspend fun delete(drawing: Drawing) = dao.delete(drawing)
}