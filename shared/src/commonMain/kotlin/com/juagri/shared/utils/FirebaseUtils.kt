package com.juagri.shared.utils

import Constants
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.where

suspend fun CollectionReference.filterCCodeUpdatedTime(cCode: String,lastUpdatedTime: Double): List<DocumentSnapshot> =
    this.where {
        Constants.FIELD_C_CODE equalTo cCode
    }.where {
        Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(lastUpdatedTime)
    }.get().documents

suspend fun CollectionReference.filterUpdatedTime(lastUpdatedTime: Double): List<DocumentSnapshot> =
    this.where {
        Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(lastUpdatedTime)
    }.get().documents

suspend fun CollectionReference.filterRegCodeUpdatedTime(regCode: String,lastUpdatedTime: Double): List<DocumentSnapshot> =
    this.where {
        Constants.FIELD_REG_CODE equalTo regCode
    }.where {
        Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(lastUpdatedTime)
    }.get().documents

suspend fun CollectionReference.filterTCodeUpdatedTime(tCode: String,lastUpdatedTime: Double): List<DocumentSnapshot> =
    this.where {
        Constants.FIELD_T_CODE equalTo tCode
    }.where {
        Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(lastUpdatedTime)
    }.get().documents