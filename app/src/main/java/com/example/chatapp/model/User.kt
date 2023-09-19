package com.example.chatapp.model

data class User(
    val id: String? = null,
    val userName: String? = null,
    val email: String? = null,
) {
    companion object {
        const val COLLECTION_NAME = "users"
    }
}
