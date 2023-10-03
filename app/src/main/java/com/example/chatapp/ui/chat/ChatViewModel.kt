package com.example.chatapp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.MessagesDao
import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.google.firebase.firestore.DocumentChange


class ChatViewModel : ViewModel() {
    var room: Room? = null
    val messageLiveData = MutableLiveData<String>()
    val toastLiveData = MutableLiveData<String>()
    val newMessageLiveData = SingleLiveEvent<List<Message>>()

    fun changeRoom(room: Room?) {
        this.room = room
        listenForMessageInRoom()
    }

    fun sendMessage() {
        if (messageLiveData.value.isNullOrEmpty()) return
        val message = Message(
            content = messageLiveData.value,
            roomId = room?.id,
            senderName = SessionProvider.user?.userName,
            senderId = SessionProvider.user?.id,
        )
        MessagesDao.sendMessage(message) {
            if (it.isSuccessful) {
                messageLiveData.value = ""
                return@sendMessage
            }
            toastLiveData.value = "Something went wrong , Please try again later !"
        }
    }

    private fun listenForMessageInRoom() {
        MessagesDao.getMessageCollection(roomId = room?.id ?: "")
            .orderBy("dateTime")
            .limitToLast(100)
            .addSnapshotListener { snapShot, error ->
                val newMessages = mutableListOf<Message>()
                snapShot?.documentChanges?.forEach { docChange ->
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val message = docChange.document.toObject(Message::class.java)
                        newMessages.add(message)

                    } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                    } else if (docChange.type == DocumentChange.Type.REMOVED) {

                    }
                }
                newMessageLiveData.value = newMessages
            }
    }
}