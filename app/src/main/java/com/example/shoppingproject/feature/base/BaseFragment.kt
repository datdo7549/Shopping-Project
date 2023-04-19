package com.example.shoppingproject.feature.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.shoppingproject.BaseActivity
import com.example.shoppingproject.feature.LoadingDialog
import com.tunjid.androidx.navigation.StackNavigator

abstract class BaseFragment<VB: ViewBinding, VM: ViewModel> : Fragment() {
    abstract val viewModel: VM
    lateinit var viewBinding: VB
    private val baseActivity by lazy { requireActivity() as BaseActivity }
    val navigator by lazy { baseActivity.stackNavigator }

    private var loadingDialog: LoadingDialog? = null


    abstract fun onCreateViewBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = onCreateViewBinding(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity())
        (viewModel as BaseViewModel).isLoading.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog?.startLoadingDialog()
            } else {
                loadingDialog?.dismissDialog()
            }
        }
    }

    fun navigate(fragment: Fragment, clearAll: Boolean) {
        if (clearAll) {
            navigator!!.clear(null, true)
        }
        navigator!!.push(fragment, fragment.javaClass.name)
    }

    fun navigateUp(upToTag: String?, includeMatch: Boolean?) {
        navigator!!.clear(upToTag, includeMatch!!)
    }

    fun navigateUp() {
        navigator!!.pop()
    }

}