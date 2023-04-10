package com.arsars.pixabayclient.ui.screens.photo

data class PhotoUI(
    val id: Long,
    val tags: List<String>,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val userName: String,
    val imageURL: String,
    val downloads: Int,
    val likes: Int,
    val comments: Int
)
