package com.devbytes.brainory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devbytes.brainory.data.database.StudySession
import com.devbytes.brainory.data.database.Subject
import com.devbytes.brainory.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val subjectRepo: SubjectRepository,
    private val sessionRepo: StudySessionRepository,
    private val prefs: PreferencesRepository
) : ViewModel() {

    val subjects: StateFlow<List<Subject>> = subjectRepo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSessions: StateFlow<List<StudySession>> = sessionRepo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val loggedInEmail = prefs.loggedInEmail

    private val _todayMinutes = MutableStateFlow(0)
    val todayMinutes: StateFlow<Int> = _todayMinutes.asStateFlow()

    init { loadTodayStats() }

    private fun loadTodayStats() {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
            val start = cal.timeInMillis
            cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59); cal.set(Calendar.SECOND, 59)
            val end = cal.timeInMillis
            _todayMinutes.value = sessionRepo.getTodayTotal(start, end)
        }
    }

    fun addSubject(name: String, colorHex: String = "#6366F1") {
        viewModelScope.launch { subjectRepo.insert(Subject(name = name, colorHex = colorHex)) }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch { subjectRepo.delete(subject) }
    }

    fun logSession(subjectId: Int, durationMinutes: Int, notes: String = "") {
        viewModelScope.launch {
            sessionRepo.insert(StudySession(subjectId = subjectId, durationMinutes = durationMinutes, notes = notes))
            loadTodayStats()
        }
    }
}