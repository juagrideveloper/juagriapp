package com.juagri.shared

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Screen : Parcelable {
   /* @Parcelize
    data object List : Screen()*/
   /*data class List(val text: String) : Screen()
    @Parcelize
    data class Details(val text: String) : Screen()*/
}