package com.example.shoppingproject.feature.customerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppingproject.R
import com.example.shoppingproject.databinding.FragmentCustomerDetailBinding
import com.example.shoppingproject.feature.ExportSuccessDialog
import com.example.shoppingproject.feature.base.BaseFragment
import com.example.shoppingproject.feature.customerDetail.adapter.ProductAdapter2
import com.example.shoppingproject.feature.homepage.HomeFragment
import com.example.shoppingproject.model.Product
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerDetailFragment : BaseFragment<FragmentCustomerDetailBinding, CustomerDetailViewModel>() {
    override val viewModel: CustomerDetailViewModel by viewModel()

    private val products: MutableList<Product> = mutableListOf()
    private lateinit var productAdapter2: ProductAdapter2

    private var exportSuccessDialog: ExportSuccessDialog? = null

    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentCustomerDetailBinding {
        return FragmentCustomerDetailBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("CUSTOMER_EMAIL")?.let {
            viewModel.customerEmail = it
        }
        initView()
        initViewModel()
    }

    private fun initView() = with(viewBinding) {
        productAdapter2 = ProductAdapter2(products)
        rvProducts.adapter = productAdapter2

        btnExport.setOnClickListener {
            exportSuccessDialog = ExportSuccessDialog(requireActivity())
            exportSuccessDialog?.show(viewModel.userProducts.value ?: mutableListOf()) {
                viewModel.deleteCustomer()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        getListProductOfCustomer()
        userProducts.observe(viewLifecycleOwner) {
            products.clear()
            products.addAll(it)
            productAdapter2.notifyDataSetChanged()
        }

        deletedCustomerResponse.observe(viewLifecycleOwner) {
            if (it) {
                exportSuccessDialog?.dismissDialog()
                navigateUp()
            }
        }
    }

    companion object {
        fun newInstance(email: String): CustomerDetailFragment {
            val args = Bundle()
            args.putString("CUSTOMER_EMAIL", email)
            val fragment = CustomerDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}