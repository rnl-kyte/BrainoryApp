package com.devbytes.brainory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devbytes.brainory.data.database.Flashcard
import com.devbytes.brainory.data.repository.FlashcardRepository
import com.devbytes.brainory.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val flashcardRepo: FlashcardRepository,
    private val subjectRepo: SubjectRepository
) : ViewModel() {

    val subjects = subjectRepo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubjectId = MutableStateFlow<Int?>(null)
    val selectedSubjectId: StateFlow<Int?> = _selectedSubjectId.asStateFlow()

    val flashcards: StateFlow<List<Flashcard>> = _selectedSubjectId.flatMapLatest { id ->
        if (id == null) flashcardRepo.getAll() else flashcardRepo.getBySubject(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quiz state
    private val _quizIndex   = MutableStateFlow(0)
    val quizIndex: StateFlow<Int> = _quizIndex.asStateFlow()

    private val _showAnswer  = MutableStateFlow(false)
    val showAnswer: StateFlow<Boolean> = _showAnswer.asStateFlow()

    fun selectSubject(id: Int?) { _selectedSubjectId.value = id }

    fun addFlashcard(subjectId: Int, question: String, answer: String) {
        viewModelScope.launch { flashcardRepo.insert(Flashcard(subjectId = subjectId, question = question, answer = answer)) }
    }

    fun updateFlashcard(card: Flashcard) {
        viewModelScope.launch { flashcardRepo.update(card) }
    }

    fun deleteFlashcard(card: Flashcard) {
        viewModelScope.launch { flashcardRepo.delete(card) }
    }

    fun flipCard() { _showAnswer.value = !_showAnswer.value }

    fun nextCard(total: Int) {
        _showAnswer.value = false
        if (_quizIndex.value < total - 1) _quizIndex.value++
    }

    fun prevCard() {
        _showAnswer.value = false
        if (_quizIndex.value > 0) _quizIndex.value--
    }

    fun resetQuiz() { _quizIndex.value = 0; _showAnswer.value = false }

    fun markCorrect(card: Flashcard) {
        viewModelScope.launch {
            flashcardRepo.update(card.copy(timesReviewed = card.timesReviewed + 1, timesCorrect = card.timesCorrect + 1))
        }
    }

    fun markIncorrect(card: Flashcard) {
        viewModelScope.launch {
            flashcardRepo.update(card.copy(timesReviewed = card.timesReviewed + 1))
        }
    }
}