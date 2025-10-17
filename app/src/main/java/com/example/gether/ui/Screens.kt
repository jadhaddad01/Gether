@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.gether.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gether.data.Post

@Composable
fun FeedScreen(onCompose: () -> Unit, onProfile: () -> Unit, vm: FeedViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Gether") }, actions = { TextButton(onClick = onProfile) { Text("Profile") } }) },
        floatingActionButton = { FloatingActionButton(onClick = onCompose) { Icon(Icons.Default.Add, null) } }
    ) { p ->
        LazyColumn(Modifier.fillMaxSize().padding(p)) {
            items(state.posts, key = { it.id }) { PostCard(it, onLike = { vm.like(it.id) }) }
        }
    }
}

@Composable
private fun PostCard(post: Post, onLike: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(12.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = post.author.avatarUrl, contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(post.author.name, fontWeight = FontWeight.SemiBold)
            }
            Text(post.text)
            if (post.imageUrl != null) {
                AsyncImage(model = post.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(200.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onLike) { Text(if (post.liked) "♥ ${post.likes}" else "♡ ${post.likes}") }
                TextButton(onClick = {}) { Text("Comment") }
                TextButton(onClick = {}) { Text("Share") }
            }
        }
    }
}

@Composable
fun ComposePostScreen(onDone: () -> Unit, vm: FeedViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("New post") }, navigationIcon = { IconButton(onClick = onDone) { Icon(Icons.Default.ArrowBack, null) } }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(text, { text = it }, label = { Text("What's happening?") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("Image URL (optional)") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = { if (text.isNotBlank()) vm.post(text.trim(), imageUrl.ifBlank { null }); onDone() }, enabled = text.isNotBlank()) {
                Text("Post")
            }
        }
    }
}

@Composable
fun ProfileScreen(onBack: () -> Unit, vm: FeedViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text(state.currentUser.name) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }) }) { p ->
        Column(Modifier.fillMaxSize().padding(p), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))
            AsyncImage(model = state.currentUser.avatarUrl, contentDescription = null, modifier = Modifier.size(96.dp).clip(CircleShape))
            Spacer(Modifier.height(8.dp))
            Text("@me", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(16.dp))
            Text("Your recent posts", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Start))
            LazyColumn(Modifier.fillMaxSize()) {
                items(state.posts.filter { it.author.id == state.currentUser.id }) { PostCard(it, onLike = { vm.like(it.id) }) }
            }
        }
    }
}
