package com.example.gouni_mobile_application.presentation.viewmodel

import com.example.gouni_mobile_application.data.remote.api.Location

sealed class MapUiState {
    object Idle : MapUiState()
    object Loading : MapUiState()
    data class Success(val start: Location, val end: Location, val polyline: String) : MapUiState()
    data class Error(val message: String) : MapUiState()
} 