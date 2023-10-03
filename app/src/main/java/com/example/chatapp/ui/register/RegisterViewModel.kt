package com.example.chatapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.UsersDao
import com.example.chatapp.model.User
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    val messageLiveData = SingleLiveEvent<Message>()
    val isLoading = MutableLiveData<Boolean>()
    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirmation = MutableLiveData<String>()

    val userNameError = MutableLiveData<String?>()
    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmationError = MutableLiveData<String?>()

    val events = SingleLiveEvent<RegisterViewEvent>()

    private val auth = Firebase.auth


    fun register() {
        if (!validForm()) return
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    insertUserToFireStore(task.result.user?.uid)
                } else {
                    isLoading.value = false
                    messageLiveData.postValue(
                        Message(
                            message = task.exception?.localizedMessage
                        )
                    )
                }
            }
    }

    private fun insertUserToFireStore(uid: String?) {

        val user = User(
            id = uid,
            email = email.value,
            userName = userName.value
        )
        UsersDao.createUser(user) { task ->
            isLoading.value = false
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "User Register Successfully",
                        posActionName = "Ok",
                        onPosActionClick = {
                            SessionProvider.user = user
                            events.postValue(RegisterViewEvent.NavigationToHome)
                        }
                    )
                )
            } else {
                messageLiveData.postValue(
                    Message(
                        message = task.exception?.localizedMessage
                    )
                )
            }
        }
    }

    private fun validForm(): Boolean {
        var isValid = true
        if (userName.value.isNullOrBlank()) {
            userNameError.postValue("Please Enter User Name!")
            isValid = false
        } else {
            userNameError.postValue(null)
        }
        if (email.value.isNullOrBlank()) {
            emailError.postValue("Please Enter Email!")
            isValid = false
        } else {
            emailError.postValue(null)
        }

        if (password.value.isNullOrBlank()) {
            passwordError.postValue("Please Enter Password!")
            isValid = false
        } else {
            passwordError.postValue(null)
        }


        if (passwordConfirmation.value.isNullOrBlank() || passwordConfirmation.value != password.value) {
            passwordConfirmationError.postValue("Password does not match!")
            isValid = false
        } else {
            passwordConfirmationError.postValue(null)
        }

        return isValid
    }

    fun navigateToLogin() {
        events.postValue(RegisterViewEvent.NavigationToLogin)
    }
}

