package com.example.shoppingproject.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.shoppingproject.databinding.FragmentLoginBinding
import com.example.shoppingproject.feature.base.BaseFragment
import com.example.shoppingproject.feature.homepage.HomeFragment
import com.example.shoppingproject.feature.register.RegisterFragment
import com.example.shoppingproject.model.Constant.customerEmail
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModel()

    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(viewBinding) {
        viewBinding.btnLogin.setOnClickListener {
            viewModel.login(edtUsername.text.toString(), edtPassword.text.toString())
        }
        viewBinding.btnRegister.setOnClickListener {
            navigate(RegisterFragment.newInstance(), false)
        }
    }

    private fun initViewModel() = with(viewModel) {
        loginResponse.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                customerEmail = viewBinding.edtUsername.text.toString()
                val isAdmin = viewBinding.edtUsername.text.toString() == "admin@gmail.com"
                navigate(HomeFragment.newInstance(isAdmin), false)
            } else {
                Toast.makeText(requireContext(), "Login fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}