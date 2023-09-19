package com.example.chatapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.RoomsDao
import com.example.chatapp.model.Room
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    val events = SingleLiveEvent<HomeViewEvent>()
    val roomsList = MutableLiveData<List<Room>>()
    val loadingLiveData = MutableLiveData<Message?>()
    val messageLiveData = SingleLiveEvent<Message>()

    fun navigateToAddRoom() {
        events.postValue(HomeViewEvent.NavigateToAddRoom)
    }

    fun getRooms() {
        RoomsDao.getAllRooms { task ->
            if (!task.isSuccessful) {

                return@getAllRooms
            }
            val rooms = task.result.toObjects(Room::class.java)
            roomsList.postValue(rooms)

        }
    }

    fun logout() {
        messageLiveData.postValue(
            Message(
                message = "Are you Sure to logout?",
                posActionName = "Yes",
                onPosActionClick = {
                    Firebase.auth.signOut()
                    SessionProvider.user = null
                    events.postValue(HomeViewEvent.NavigateToLogin)
                },
                negActionName = "Cancel"
            )
        )
    }

}