package com.kaushal.japacounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.data.Outcome
import com.kaushal.japacounter.data.Status
import com.kaushal.japacounter.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _addNewJapaOutcome = MutableSharedFlow<Boolean>(0)
    val addNewJapaOutcome = _addNewJapaOutcome.asSharedFlow()

    private var currentCount : Int = 0
    private var targetCount : Int? = null

    fun addNewJapa(name: String, goal: String?) {
        viewModelScope.launch {
            try {
                val entities = Entities(name, goal?.toInt(), 0, 0, 0, null, Status.NOT_STARTED)
                mainRepository.addNewJapa(entities)
                _addNewJapaOutcome.emit(true)
            } catch (e: Exception) {
                _addNewJapaOutcome.emit(false)
            }
        }
    }

    private val _myJapaListOutcome = MutableStateFlow<Outcome<List<Entities>>>(Outcome.Empty)
    val myJapaListOutcome = _myJapaListOutcome.asStateFlow()

    fun getMyJapaList() {
        viewModelScope.launch {
            try {
                _myJapaListOutcome.emit(Outcome.loading(true))
                val result = mainRepository.getMyJapaList()
                _myJapaListOutcome.emit(Outcome.success(result))
            } catch (ex: Exception) {
                _myJapaListOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            }
        }
    }

    private val _myJapaDetailsOutcome = MutableStateFlow<Outcome<Entities>>(Outcome.Empty)
    val myJapaDetailsOutcome = _myJapaDetailsOutcome.asStateFlow()

    fun getJapaDetails(japaName: String) {
        viewModelScope.launch {
            try {
                _myJapaDetailsOutcome.emit(Outcome.loading(true))
                val result = mainRepository.getJapaDetails(japaName)
                currentCount = result.currentValue
                targetCount = result.goal
                _myJapaDetailsOutcome.emit(Outcome.success(result))
            } catch (ex: Exception) {
                _myJapaDetailsOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            }
        }
    }


    private val _updateCounterOutcome = MutableSharedFlow<Outcome<Boolean>>()
    val updateCounterOutcome = _updateCounterOutcome.asSharedFlow()

    fun updateJapaCounter(japaName: String, newCount: Int, target: Int?) {
        viewModelScope.launch {
            try {
                _updateCounterOutcome.emit(Outcome.loading(true))
                mainRepository.updateJapaCounter(japaName, newCount, currentCount, target)
                _updateCounterOutcome.emit(Outcome.success(true))
            } catch (ex: Exception) {
                _updateCounterOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            }
        }
    }


    private val _completeJapaOutcome = MutableSharedFlow<Outcome<Boolean>>()
    val completeJapaOutcome = _completeJapaOutcome.asSharedFlow()

    fun completeJapa(japaName: String) {
        viewModelScope.launch {
            try {
                _completeJapaOutcome.emit(Outcome.loading(true))
                mainRepository.completeJapa(japaName)
                _completeJapaOutcome.emit(Outcome.success(true))
            } catch (ex: Exception) {
                _completeJapaOutcome.emit(Outcome.failure(ex, ex.message.orEmpty()))
            }
        }
    }
}