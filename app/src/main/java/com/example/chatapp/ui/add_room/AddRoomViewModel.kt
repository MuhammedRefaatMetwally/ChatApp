package com.example.chatapp.ui.add_room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.RoomsDao
import com.example.chatapp.model.Category
import com.example.chatapp.ui.Message

class AddRoomViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Message?>()
    val messageLiveData = SingleLiveEvent<Message>()
    val events = SingleLiveEvent<AddRoomViewEvent>()
    val roomName = MutableLiveData<String>()
    val roomDesc = MutableLiveData<String>()
    val roomNameError = MutableLiveData<String?>()
    val roomDescError = MutableLiveData<String?>()
    val categories = Category.getCategories()

    var selectedCategory = categories[0]

    fun createRoom() {
        if (!validForm()) return
        loadingLiveData.postValue(
            Message(
                message = "Loading...",
                isCancelable = false
            )
        )
        RoomsDao.createRoom(
            title = roomName.value ?: "",
            desc = roomDesc.value ?: "",
            ownerId = SessionProvider.user?.id ?: "",
            categoryId = selectedCategory.id
        ) { task ->
            if (task.isSuccessful) {
                loadingLiveData.postValue(null)
                messageLiveData.postValue(
                    Message(
                        message = "Room Created Successfully",
                        posActionName = "Ok",
                        onPosActionClick = {
                            events.postValue(AddRoomViewEvent.NavigateToHomeAndFinish)
                        }
                    )
                )
                return@createRoom
            }
        }
    }

    fun onCategorySelected(position: Int) {
        selectedCategory = categories[position]
    }

    private fun validForm(): Boolean {
        var isValid = true
        if (roomName.value.isNullOrBlank()) {
            roomNameError.postValue("Pleas Enter Room Name")
            isValid = false
        } else {
            roomNameError.postValue(null)
        }

        if (roomDesc.value.isNullOrBlank()) {
            roomDescError.postValue("Pleas Enter Room Description")
            isValid = false
        } else {
            roomDescError.postValue(null)
        }
        return isValid
    }
}


