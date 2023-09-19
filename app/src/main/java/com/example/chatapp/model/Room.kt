package com.example.chatapp.model

data class Room(
    val id: Int? = null,
    val title: String? = null,
    val categoryId: Int? = null,
    val description: String? = null,
    val ownerId: String? = null,
) {
    companion object {
        const val COLLECTION_NAME = "rooms"
    }
}