package com.example.shoppingproject.remote.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.shoppingproject.feature.register.RegisterViewModel
import com.example.shoppingproject.feature.login.LoginViewModel
import com.example.shoppingproject.feature.homepage.HomeViewModel
import com.example.shoppingproject.feature.customerDetail.CustomerDetailViewModel

val localModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CustomerDetailViewModel)
}