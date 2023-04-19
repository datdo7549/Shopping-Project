package com.example.shoppingproject.feature.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.shoppingproject.R
import com.example.shoppingproject.databinding.FragmentRegisterBinding
import com.example.shoppingproject.feature.base.BaseFragment
import com.example.shoppingproject.feature.homepage.HomeFragment
import com.example.shoppingproject.feature.login.LoginFragment
import com.example.shoppingproject.model.Constant
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModel()

    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(viewBinding) {
        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || password != confirmPassword) {
                Toast.makeText(requireContext(), "Please type valid email or password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registerNewAccount(email, password)
        }
    }

    private fun initViewModel() = with(viewModel) {
        registerNewAccountResponse.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                Toast.makeText(requireContext(), "Register new account success", Toast.LENGTH_SHORT).show()
                viewModel.login(it.email, it.password)
            } else {
                Toast.makeText(requireContext(), "Register new account fail", Toast.LENGTH_SHORT).show()
            }
        }

        loginResponse.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                Constant.customerEmail = viewBinding.edtEmail.text.toString()
                val isAdmin = viewBinding.edtEmail.text.toString() == "admin@gmail.com"
                navigate(HomeFragment.newInstance(isAdmin), false)
            } else {
                Toast.makeText(requireContext(), "Login fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}