package com.juagri.shared.domain.model.promotion

data class ParticipationEntry(
    val entryId: String,
    val comments: String,
    val lat: Double,
    val long: Double,
    val images: List<ByteArray>
)