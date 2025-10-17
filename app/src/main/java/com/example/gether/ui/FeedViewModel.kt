package com.example.gether.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gether.data.InMemoryRepo
import com.example.gether.data.Post
import com.example.gether.data.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FeedViewModel : ViewModel() {
    private val me = User("me", "You", "https://i.pravatar.cc/150?img=1")

    val uiState: StateFlow<FeedUiState> = InMemoryRepo.posts
        .map { FeedUiState(currentUser = me, posts = it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, FeedUiState(currentUser = me, posts = emptyList()))

    fun like(id: String) = InMemoryRepo.toggleLike(id)
    fun post(text: String, imageUrl: String?) = InMemoryRepo.addPost(me, text, imageUrl)
}

data class FeedUiState(val currentUser: User, val posts: List<Post>)
