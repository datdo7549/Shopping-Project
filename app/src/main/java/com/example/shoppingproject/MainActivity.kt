package com.example.shoppingproject

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.shoppingproject.databinding.ActivityMainBinding
import com.example.shoppingproject.feature.login.LoginFragment

class MainActivity : BaseActivity() {
    override fun startDestination(): Fragment {
        return LoginFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(inflater)
        setContentView(viewBinding.root)
    }
}