package com.juagri.shared.ui.starclub

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.starclub.StarClubCustomer
import com.juagri.shared.domain.model.starclub.StarClubMetric
import com.juagri.shared.domain.usecase.StarClubUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.viewmodel.viewModelScope

/** Tier image filenames in order: bronze → silver → gold → platinum → diamond */
val STAR_CLUB_TIER_IMAGES = listOf(
    "img_star_club_bronze.png",
    "img_star_club_silver.png",
    "img_star_club_gold.png",
    "img_star_club_platinum.png",
    "img_star_club_diamond.png"
)

/** Tier thresholds: (min TotalSales, min FocusProduct). DSO must be < 135 for all tiers. */
private val TIER_THRESHOLDS = listOf(
    Pair(18.0, 7.5),   // Bronze
    Pair(30.0, 12.0),  // Silver
    Pair(55.0, 22.0),  // Gold
    Pair(80.0, 32.0),  // Platinum
    Pair(110.0, 44.0)  // Diamond
)

private const val DSO_MAX = 135.0

data class StarClubUiState(
    val wonTierImages: List<String> = emptyList(),
    val canWinTierImages: List<String> = emptyList(),
    val metrics: Map<String, StarClubMetric>? = null,
    val customerName: String? = null
)

class StarClubViewModel(
    session: SessionPreference,
    dataManager: DataManager,
    private val starClubUseCase: StarClubUseCase
) : BaseViewModel(session, dataManager) {

    private val _starClubState = MutableStateFlow(StarClubUiState())
    val starClubState = _starClubState.asStateFlow()

    fun initScreen() {
        setScreenId(Constants.SCREEN_STAR_CLUB)
        loadStarClub()
    }

    private fun loadStarClub() {
        val ccode = "CAP-0022" //getJUEmployee()?.code.value()
        if (ccode.isBlank()) {
            _starClubState.value = StarClubUiState(
                canWinTierImages = STAR_CLUB_TIER_IMAGES
            )
            return
        }
        starClubUseCase.getStarClubCustomer(ccode)
            .onEach { customer ->
                _starClubState.value = computeWonAndCanWin(customer)
            }
            .catch {
                _starClubState.value = StarClubUiState(
                    canWinTierImages = STAR_CLUB_TIER_IMAGES
                )
            }
            .launchIn(viewModelScope)
    }

    /**
     * Tiers are won based on achieved thresholds (and DSO < 135):
     * Bronze: TotalSales >= 18, FocusProduct >= 7.5
     * Silver: TotalSales >= 30, FocusProduct >= 12
     * Gold: TotalSales >= 55, FocusProduct >= 22
     * Platinum: TotalSales >= 80, FocusProduct >= 32
     * Diamond: TotalSales >= 110, FocusProduct >= 44
     */
    private fun computeWonAndCanWin(customer: StarClubCustomer?): StarClubUiState {
        println("computeWonAndCanWin: $customer")
        val metrics = customer?.metrics ?: emptyMap()
        val totalSales = metrics["TotalSales"]?.achieved ?: 0.0
        val focusProduct = metrics["FocusProduct"]?.achieved ?: 0.0
        val dso = metrics["DSO"]?.achieved ?: 0.0

        val wonTierImages = mutableListOf<String>()
        val canWinTierImages = mutableListOf<String>()
        for (i in STAR_CLUB_TIER_IMAGES.indices) {
            val (minSales, minFocus) = TIER_THRESHOLDS[i]
            val isWon = totalSales >= minSales &&
                focusProduct >= minFocus &&
                dso < DSO_MAX
            if (isWon) {
                wonTierImages.add(STAR_CLUB_TIER_IMAGES[i])
            } else {
                canWinTierImages.add(STAR_CLUB_TIER_IMAGES[i])
            }
        }
        return StarClubUiState(
            wonTierImages = wonTierImages,
            canWinTierImages = canWinTierImages,
            metrics = metrics.ifEmpty { null },
            customerName = customer?.cname
        )
    }
}
