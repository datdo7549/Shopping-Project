package com.example.shoppingproject.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingproject.feature.base.BaseViewModel
import com.example.shoppingproject.model.LoginModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    private var _loginResponse: MutableLiveData<LoginModel> = MutableLiveData()
    val loginResponse: LiveData<LoginModel> = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    setLoading(false)
                    if (task.isSuccessful) {
                        _loginResponse.value = LoginModel(true, email, password)
                    } else {
                        _loginResponse.value = LoginModel()
                    }
                }
        }
    }
}