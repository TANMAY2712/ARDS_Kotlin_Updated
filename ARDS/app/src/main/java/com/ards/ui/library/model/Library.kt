package com.ards.ui.library.model

data class Library(
    val name: String,
    val description: String,
    val imageResId: Int, // Image resource ID
    val totalVideos: Int
)
