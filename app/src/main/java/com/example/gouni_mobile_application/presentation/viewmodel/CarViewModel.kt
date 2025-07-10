package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.domain.usecase.car.DeleteCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.GetCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.GetCarByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.car.HasCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.InsertCarUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarViewModel(
    private val getCarUseCase: GetCarUseCase,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val insertCarUseCase: InsertCarUseCase,
    private val hasCarUseCase: HasCarUseCase,
    private val deleteCarUseCase: DeleteCarUseCase
) : ViewModel() {

    private val _carState = MutableStateFlow<UiState<Car?>>(UiState.Loading)
    val carState: StateFlow<UiState<Car?>> = _carState.asStateFlow()

    private val _carByIdState = MutableStateFlow<UiState<Car?>>(UiState.Loading)
    val carByIdState: StateFlow<UiState<Car?>> = _carByIdState.asStateFlow()

    private val _hasCarState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val hasCarState: StateFlow<UiState<Boolean>> = _hasCarState.asStateFlow()

    private val _insertCarState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val insertCarState: StateFlow<UiState<Unit>> = _insertCarState.asStateFlow()

    private val _deleteCarState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteCarState: StateFlow<UiState<Unit>> = _deleteCarState.asStateFlow()

    fun getCar(userId: String) {
        viewModelScope.launch {
            try {
                getCarUseCase(userId).collect { car ->
                    _carState.value = UiState.Success(car)
                }
            } catch (e: Exception) {
                _carState.value = UiState.Error(e.message ?: "Error loading car")
            }
        }
    }

    fun getCarById(id: String) {
        viewModelScope.launch {
            try {
                _carByIdState.value = UiState.Loading
                getCarByIdUseCase(id).collect { car ->
                    _carByIdState.value = UiState.Success(car)
                }
            } catch (e: Exception) {
                _carByIdState.value = UiState.Error(e.message ?: "Error loading car")
            }
        }
    }

    fun hasCar(userId: String) {
        viewModelScope.launch {
            try {
                _hasCarState.value = UiState.Loading
                val hasCar = hasCarUseCase(userId)
                _hasCarState.value = UiState.Success(hasCar)
            } catch (e: Exception) {
                _hasCarState.value = UiState.Error(e.message ?: "Error checking car")
            }
        }
    }

    fun insertCar(car: Car) {
        viewModelScope.launch {
            try {
                _insertCarState.value = UiState.Loading
                insertCarUseCase(car)
                _insertCarState.value = UiState.Success(Unit)
                getCar(car.userId)
            } catch (e: Exception) {
                _insertCarState.value = UiState.Error(e.message ?: "Error inserting car")
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                _deleteCarState.value = UiState.Loading
                deleteCarUseCase(car)
                _deleteCarState.value = UiState.Success(Unit)
                getCar(car.userId)
            } catch (e: Exception) {
                _deleteCarState.value = UiState.Error(e.message ?: "Error deleting car")
            }
        }
    }

    fun resetInsertCarState() {
        _insertCarState.value = UiState.Idle
    }

    fun resetDeleteCarState() {
        _deleteCarState.value = UiState.Idle
    }
} 