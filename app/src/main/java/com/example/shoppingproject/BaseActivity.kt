package com.example.shoppingproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tunjid.androidx.navigation.StackNavigator

abstract class BaseActivity : AppCompatActivity() {
    var stackNavigator: StackNavigator? = null

    abstract fun startDestination(): Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stackNavigator = StackNavigator(supportFragmentManager, R.id.navRootHost)
        if (savedInstanceState == null) {
            val rootFragment = startDestination()
            stackNavigator!!.push(rootFragment, rootFragment.javaClass.name)
        }
    }
}