package com.juagri.shared.ui.starclub

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.starclub.StarClubCustomer
import com.juagri.shared.domain.model.starclub.StarClubMetric
import com.juagri.shared.domain.usecase.StarClubUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.value
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

private val STAR_CLUB_TIERS = listOf("Bronze", "Silver", "Gold", "Platinum", "Diamond")

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
        val ccode = getJUEmployee()?.code.value()
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

    private fun computeWonAndCanWin(customer: StarClubCustomer?): StarClubUiState {
        val achievedClub = customer?.achievedClub?.trim().orEmpty()
        val wonTierImages = mutableListOf<String>()
        val canWinTierImages = mutableListOf<String>()

        val achievedIndex = STAR_CLUB_TIERS.indexOfFirst { it.equals(achievedClub, ignoreCase = true) }
        if (achievedClub.isNotBlank() && !achievedClub.equals("Not Qualified", ignoreCase = true) && achievedIndex >= 0) {
            for (i in STAR_CLUB_TIER_IMAGES.indices) {
                if (i <= achievedIndex) {
                    wonTierImages.add(STAR_CLUB_TIER_IMAGES[i])
                } else {
                    canWinTierImages.add(STAR_CLUB_TIER_IMAGES[i])
                }
            }
        } else {
            canWinTierImages.addAll(STAR_CLUB_TIER_IMAGES)
        }

        val metrics = when {
            achievedIndex >= 0 -> customer?.clubs?.get(STAR_CLUB_TIERS[achievedIndex])
            else -> customer?.metrics
        }
        val metricsOrNull = if (metrics.isNullOrEmpty()) null else metrics
        return StarClubUiState(
            wonTierImages = wonTierImages,
            canWinTierImages = canWinTierImages,
            metrics = metricsOrNull,
            customerName = customer?.cname
        )
    }
}
