package com.devbytes.brainory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devbytes.brainory.data.database.Schedule
import com.devbytes.brainory.data.repository.ScheduleRepository
import com.devbytes.brainory.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepo: ScheduleRepository,
    private val subjectRepo: SubjectRepository
) : ViewModel() {

    val allSchedules: StateFlow<List<Schedule>> = scheduleRepo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subjects = subjectRepo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDateMillis = MutableStateFlow(System.currentTimeMillis())
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis.asStateFlow()

    val schedulesForSelectedDate: StateFlow<List<Schedule>> = combine(
        allSchedules, selectedDateMillis
    ) { schedules, dateMs ->
        val cal = Calendar.getInstance().apply { timeInMillis = dateMs }
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0)
        val start = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 59); cal.set(Calendar.SECOND, 59)
        val end = cal.timeInMillis
        schedules.filter { it.dateTime in start..end }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDate(millis: Long) { _selectedDateMillis.value = millis }

    fun addSchedule(title: String, subjectId: Int, dateTime: Long) {
        viewModelScope.launch {
            scheduleRepo.insert(Schedule(title = title, subjectId = subjectId, dateTime = dateTime))
        }
    }

    fun toggleComplete(schedule: Schedule) {
        viewModelScope.launch {
            scheduleRepo.update(schedule.copy(isCompleted = !schedule.isCompleted))
        }
    }

    fun delete(schedule: Schedule) {
        viewModelScope.launch { scheduleRepo.delete(schedule) }
    }
}