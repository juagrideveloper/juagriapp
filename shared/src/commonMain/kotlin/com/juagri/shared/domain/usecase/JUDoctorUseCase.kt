package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.doctor.JUDoctorRepository

class JUDoctorUseCase(private val repository: JUDoctorRepository) {
    suspend fun getJUDoctorItems(parentId: String) = repository.getJUDoctorItems(parentId)
}