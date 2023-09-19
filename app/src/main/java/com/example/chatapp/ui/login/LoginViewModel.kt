package com.example.chatapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.UsersDao
import com.example.chatapp.model.User
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    val messageLiveData = SingleLiveEvent<Message>()
    val isLoading = MutableLiveData<Boolean>()

    val email = MutableLiveData<String>("crowy@dota.com")
    val password = MutableLiveData<String>("123456")

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    private val auth = Firebase.auth

    val events = SingleLiveEvent<LoginViewEvent>()

    fun login() {
        if (!validForm()) return
        isLoading.value = true
        auth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    getUserFromFireStore(task.result.user?.uid)
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

    private fun getUserFromFireStore(uid: String?) {

        UsersDao.getUsers(uid) { task ->
            isLoading.value = false
            if (task.isSuccessful) {
                val user = task.result.toObject(User::class.java)
                SessionProvider.user = user
                messageLiveData.postValue(
                    Message(
                        message = "Logged In Successfully",
                        posActionName = "Ok",
                        onPosActionClick = {
                            events.postValue(LoginViewEvent.NavigationToHome)
                        },
                        isCancelable = false
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


        return isValid
    }

    fun navigateToRegister() {
        events.postValue(LoginViewEvent.NavigationToRegister)
    }
}