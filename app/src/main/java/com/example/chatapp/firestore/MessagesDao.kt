package com.example.chatapp.firestore

import com.example.chatapp.model.Message
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference

object MessagesDao {

    fun getMessageCollection(roomId: String): CollectionReference =
        RoomsDao.getRoomsCollection()
            .document(roomId)
            .collection(Message.COLLECTION_NAME)


    fun sendMessage(message: Message, onCompleteListener: OnCompleteListener<Void>) {
        val messageDoc = getMessageCollection(message.roomId ?: "")
            .document()
        message.id = messageDoc.id
        messageDoc.set(message)
            .addOnCompleteListener(onCompleteListener)
    }
}