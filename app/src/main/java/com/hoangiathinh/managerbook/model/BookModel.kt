package com.hoangiathinh.managerbook.model


data class BookModel(
    val id: String,
    val title: String,
    val description: String,
    val isBorrowed: Boolean = false
)
