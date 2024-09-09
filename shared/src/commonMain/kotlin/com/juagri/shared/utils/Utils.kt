package com.juagri.shared.utils

import Constants
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.utils.strings.Names
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlin.math.min

const val MILLIS_ONE_DAY = 86400000.0
private const val MILLIS_ONE_SEC = 1000.0
fun String?.value() = this ?: ""
fun Boolean?.value() = this ?: false

fun String?.isEqualTo(compare: String) = (this.value().lowerCase() == compare.lowerCase())

fun String?.isNotEqualTo(compare: String) = (this.value().lowerCase() != compare.lowerCase())

fun JURegion?.selectedValue(label:Names): String = this?.regName ?: label.selectRegion
fun JUTerritory?.selectedValue(label:Names): String = this?.tName ?: label.selectTerritory
fun JUDealer?.selectedValue(label:Names): String = this?.cName ?: label.selectDealer
fun FinYear?.selectedValue(label:Names): String = this?.fYear ?: label.selectFinYear
fun FinMonth?.selectedValue(label:Names): String = this?.fMonth ?: label.selectFinMonth
fun PromotionEventItem?.selectedValue(label:Names): String = this?.name ?: label.selectEvent

fun Double?.value() = this ?: 0.0
fun Double?.maxLen():Int = if(value() > 0) value().toInt() else 100

fun Double?.toTimeStamp() = Timestamp.fromMilliseconds(this.value())

fun Double?.isDeleted() = this.value().toInt() == -1

fun Int?.value() = this ?: 0

fun Timestamp?.value():Double = this?.toMilliseconds() ?: 0.0

fun Timestamp?.startTime():Double  = this?.let {
        val currentTime = value()
        currentTime.minus(currentTime.mod(MILLIS_ONE_DAY))
    }?: 0.0

fun Timestamp?.endTime():Double  = this?.let {
    val currentTime = value()
    currentTime.plus(MILLIS_ONE_DAY - currentTime.mod(MILLIS_ONE_DAY) - MILLIS_ONE_SEC)
}?: 0.0

fun Float?.value():Float = this ?: 0f
fun Float?.valueInLacs() = (this.value() / Constants.DIVIDED_BY).limitDecimals()

fun Float?.limitDecimals(maxDecimals: Int = 2): String {
    val result = this.value().toString()
    val lastIndex = result.length - 1
    var pos = lastIndex
    while (pos >= 0 && result[pos] != '.') {
        pos--
    }
    return if (maxDecimals < 1 && pos >= 0) {
        result.substring(0, min(pos, result.length))
    } else if (pos >= 0) {
        result.substring(0, min(pos + 1 + maxDecimals, result.length))
    } else {
        return result
    }
}

fun Double?.limitDecimals(maxDecimals: Int = 2): String {
    val result = this.value().toString()
    val lastIndex = result.length - 1
    var pos = lastIndex
    while (pos >= 0 && result[pos] != '.') {
        pos--
    }

    return if (maxDecimals < 1 && pos >= 0) {
        result.substring(0, min(pos, result.length))
    } else if (pos >= 0) {
        result.substring(0, min(pos + 1 + maxDecimals, result.length))
    } else {
        return result
    }
}

fun Double?.toMoneyFormat() = getIndianCurrencyFormat(this.value().toString())

fun getIndianCurrencyFormat(money: String): String {
    var stringBuilder = ""
    val amount = money.split(".")[0]
    val amountArray = amount.toCharArray()
    var a = 0
    var b = 0
    for (i in amountArray.indices.reversed()) {
        if (a < 3) {
            stringBuilder += amountArray[i]
            a++
        } else if (b < 2) {
            if (b == 0) {
                stringBuilder += ","
                stringBuilder += amountArray[i]
                b++
            } else {
                stringBuilder += amountArray[i]
                b = 0
            }
        }
    }
    return stringBuilder.reversed()
}

fun Long?.value():Long = this ?: 0L

fun Long?.doubleValue():Double = (this ?: 0).toDouble()

fun MutableState<Boolean>.enable() {
    this.value = true
}

fun MutableState<Boolean>.disable(){
    this.value = false
}

fun String?.lowerCase() = this.value().toLowerCase(Locale.current)

fun String?.isContains(compare: String) = this.lowerCase().contains(compare.lowerCase())

@Composable
fun getScreenHeaderColor() = Brush.horizontalGradient(
    colors = listOf(
        getColors().primary,
        getColors().secondary
    )
)

@Composable
fun getBackgroundGradient() = Brush.horizontalGradient(
    colors = listOf(
        getColors().secondary,
        getColors().primary
    )
)

@Composable
fun getColors() = MaterialTheme.colorScheme