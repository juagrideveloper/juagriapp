package com.juagri.shared.utils

import dev.gitlive.firebase.firestore.Timestamp
import io.ktor.util.date.GMTDate


fun Timestamp?.toDDMMYYYY() = GMTDate(this.value().toLong()).toDDMMYYYY()

fun Timestamp?.toYYYYMMDD() = GMTDate(this.value().toLong()).toYYYYMMDD()
fun GMTDate.toDDMMYYYY(): String = buildString {
    append(dayOfMonth.padZero(2))
    append("-")
    append(month.ordinal.plus(1).padZero(2))
    append("-")
    append(year.padZero(4))
}

fun GMTDate.toYYYYMMDD(): String = buildString {
    append(year.padZero(4))
    append("-")
    append(month.ordinal.plus(1).padZero(2))
    append("-")
    append(dayOfMonth.padZero(2))
}
private fun Int.padZero(length: Int): String = toString().padStart(length, '0')