package com.example.shoppingproject.feature.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.shoppingproject.databinding.FragmentHomeBinding
import com.example.shoppingproject.feature.PaySuccessDialog
import com.example.shoppingproject.feature.base.BaseFragment
import com.example.shoppingproject.feature.customerDetail.CustomerDetailFragment
import com.example.shoppingproject.feature.homepage.adapter.CustomerAdapter
import com.example.shoppingproject.feature.homepage.adapter.ProductAdapter
import com.example.shoppingproject.feature.scan.ScannedFragment
import com.example.shoppingproject.model.Customer
import com.example.shoppingproject.model.Product
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModel()

    private val products: MutableList<Product> = mutableListOf()
    private val customers: MutableList<Customer> = mutableListOf()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean("IS_ADMIN")?.let {
            viewModel.isAdmin = it
        }
        parentFragmentManager.setFragmentResultListener(
            "result_key",
            this
        ) { _, result ->
            val productId = result.getString("product_id_key")
            viewModel.addProduct(productId ?: "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(viewBinding) {
        lnEmptyList.visibility = View.VISIBLE
        productAdapter = ProductAdapter(
            products,
            onRemoveClick = {
                viewModel.onRemoveProduct(it)
            },
            onPlusClick = {
                viewModel.onPlusProduct(it)
            }
        )

        customerAdapter = CustomerAdapter(customers) {
            navigate(CustomerDetailFragment.newInstance(it), false)
        }

        if (viewModel.isAdmin) {
            rvProducts.visibility = View.GONE
            lnEmptyList.visibility = View.GONE
            lnTotal.visibility = View.GONE
            lnGroupBtn.visibility = View.GONE

            rvCustomer.visibility = View.VISIBLE
            lnEmptyCustomer.visibility = View.VISIBLE
        } else {
            rvProducts.visibility = View.VISIBLE
            lnEmptyList.visibility = View.VISIBLE
            lnTotal.visibility = View.VISIBLE
            lnGroupBtn.visibility = View.VISIBLE

            rvCustomer.visibility = View.GONE
            lnEmptyCustomer.visibility = View.GONE
        }

        rvProducts.adapter = productAdapter
        rvCustomer.adapter = customerAdapter
        btnAddNewProduct.setOnClickListener {
            navigate(ScannedFragment.newInstance(), false)
        }

        btnPayNow.setOnClickListener {
            viewModel.payNow()
        }
    }

    private fun initViewModel() = with(viewModel) {
        getProducts()
        userProducts.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                viewBinding.lnEmptyList.visibility = View.GONE
            } else {
                viewBinding.lnEmptyList.visibility = View.VISIBLE
            }
            products.clear()
            products.addAll(it)
            getTotal()
            productAdapter.notifyDataSetChanged()
        }

        payResponse.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.clearData()
                val paySuccessDialog = PaySuccessDialog(requireActivity())
                paySuccessDialog.show()
            }
        }

        total.observe(viewLifecycleOwner) {
            viewBinding.tvTotal.text = it.toString()
        }

        if (viewModel.isAdmin) {
            getListCustomerInStack()
        }

        customerStack.observe(viewLifecycleOwner) {
            if (it.size > 0) {
                viewBinding.rvCustomer.visibility = View.VISIBLE
                viewBinding.lnEmptyCustomer.visibility = View.GONE
                customers.clear()
                customers.addAll(it)
                customerAdapter.notifyDataSetChanged()
            } else {
                viewBinding.lnEmptyCustomer.visibility = View.VISIBLE
                viewBinding.rvCustomer.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance(isAdmin: Boolean): HomeFragment {
            val args = Bundle()
            args.putBoolean("IS_ADMIN", isAdmin)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.reset()
    }
}