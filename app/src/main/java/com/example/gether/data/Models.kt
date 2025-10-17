package com.example.gether.data

data class User(val id: String, val name: String, val avatarUrl: String)
data class Post(
    val id: String,
    val author: User,
    val text: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val liked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
