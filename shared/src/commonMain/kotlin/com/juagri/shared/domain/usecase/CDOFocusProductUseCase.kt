package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.focusProduct.CDOFocusProductRepository

class CDOFocusProductUseCase(private val repository: CDOFocusProductRepository) {
    suspend fun getCDOFocusProductItems(cdoId: String) = repository.getCDOFocusProductItems(cdoId)
}