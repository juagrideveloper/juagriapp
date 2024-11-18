package com.juagri.shared.domain.model.promotion

data class ParticipationCounts(
    val actId: String = "",
    val actName: String = "",
    var yPlan: Double = 0.0,
    var yParticipated: Double = 0.0,
    var yActual: Double = 0.0,
    var yTotal: Double = 0.0,
)
