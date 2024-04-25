package com.juagri.shared.domain.model.promotion

data class PromotionDashboard(
    val actId: String = "",
    val actName: String = "",
    var mPlan: Double = 0.0,
    var mActual: Double = 0.0,
    var yPlan: Double = 0.0,
    var yActual: Double = 0.0
)
