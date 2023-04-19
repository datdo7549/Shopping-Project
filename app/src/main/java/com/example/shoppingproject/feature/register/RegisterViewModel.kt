package com.example.shoppingproject.feature.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingproject.feature.base.BaseViewModel
import com.example.shoppingproject.model.LoginModel
import com.example.shoppingproject.model.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel constructor(
    private val firebaseAuth: FirebaseAuth
): BaseViewModel() {
    private var _registerNewAccountResponse: MutableLiveData<RegisterModel> = MutableLiveData()
    val registerNewAccountResponse: LiveData<RegisterModel> = _registerNewAccountResponse

    private var _loginResponse: MutableLiveData<LoginModel> = MutableLiveData()
    val loginResponse: LiveData<LoginModel> = _loginResponse

    fun registerNewAccount(email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    setLoading(false)
                    if (task.isSuccessful) {
                        _registerNewAccountResponse.value = RegisterModel(true, email, password)
                    } else {
                        _registerNewAccountResponse.value = RegisterModel()
                    }
                }
        }
    }

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