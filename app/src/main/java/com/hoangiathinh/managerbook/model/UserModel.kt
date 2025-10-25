package com.hoangiathinh.managerbook.model


data class UserModel(
    val id: String,
    val name: String,
    val borrowedBookIds: List<String>
)