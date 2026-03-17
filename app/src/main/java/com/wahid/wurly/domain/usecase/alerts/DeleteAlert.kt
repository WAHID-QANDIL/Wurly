package com.wahid.wurly.domain.usecase.alerts

import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject

class DeleteAlert @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(id: Long) = repository.deleteAlert(id)
}