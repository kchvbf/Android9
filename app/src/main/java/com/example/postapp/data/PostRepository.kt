package com.example.postapp.data

import com.example.postapp.API.PostApi
import com.example.postapp.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepository {

    private val postApi = PostApi.create()

    fun getPosts(): Flow<List<Post>> {
        return flow {
            val posts = postApi.getPosts()
            emit(posts)
        }
    }

    fun updatePost(post: Post): Flow<Post> {
        return flow {
            val updatedPost = postApi.updatePost(post.id, post)
            emit(updatedPost)
        }
    }
}