package com.example.forestquiz.data

// Note: No longer needs to be Parcelable as we are not passing it via navigation arguments.
data class Qna(
    val q: String = "",
    val a: String = "",
    val o1: String = "",
    val o2: String = "",
    val o3: String = ""
)