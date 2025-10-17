package com.example.gether.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object InMemoryRepo {
    private val _posts = MutableStateFlow(samplePosts())
    val posts = _posts.asStateFlow()

    fun toggleLike(id: String) {
        _posts.value = _posts.value.map {
            if (it.id == id) it.copy(liked = !it.liked, likes = if (!it.liked) it.likes + 1 else it.likes - 1) else it
        }
    }

    fun addPost(author: User, text: String, imageUrl: String?) {
        val new = Post(UUID.randomUUID().toString(), author, text, imageUrl)
        _posts.value = listOf(new) + _posts.value
    }
}

private fun samplePosts(): List<Post> {
    val u1 = User("u1", "Ava", "https://i.pravatar.cc/150?img=5")
    val u2 = User("u2", "Liam", "https://i.pravatar.cc/150?img=12")
    val u3 = User("u3", "Mia", "https://i.pravatar.cc/150?img=25")
    return listOf(
        Post(id = "p1", author = u1, text = "First post on Gether 👋", likes = 3),
        Post(id = "p2", author = u2, text = "Compose + Kotlin = ❤️", imageUrl = "https://picsum.photos/seed/compose/900/500", likes = 12),
        Post(id = "p3", author = u3, text = "Coffee then code.")
    )
}
