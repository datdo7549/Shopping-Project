package com.example.shoppingproject.feature.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingproject.feature.base.BaseViewModel
import com.example.shoppingproject.model.Constant
import com.example.shoppingproject.model.Customer
import com.example.shoppingproject.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeViewModel(
    private val databaseReference: DatabaseReference
) : BaseViewModel() {
    private var _products: MutableLiveData<List<Product?>> = MutableLiveData()

    private var _userProducts: MutableLiveData<MutableList<Product>> = MutableLiveData()
    val userProducts: LiveData<MutableList<Product>> = _userProducts

    private var _customerStack: MutableLiveData<MutableList<Customer>> = MutableLiveData()
    val customerStack: LiveData<MutableList<Customer>> = _customerStack

    private var _payResponse: MutableLiveData<Boolean> = MutableLiveData()
    val payResponse: LiveData<Boolean> = _payResponse

    private var _total: MutableLiveData<Float> = MutableLiveData()
    val total: LiveData<Float> = _total

    var isAdmin: Boolean = false

    fun getProducts() {
        setLoading(true)
        databaseReference.child("Products").get().addOnSuccessListener {
            val products = mutableListOf<Product?>()
            it.children.forEach { singleDataSnapshot ->
                val product = singleDataSnapshot.getValue(Product::class.java)?.copy(
                    amount = 1
                )
                products.add(product)
            }
            setLoading(false)
            _products.value = products
        }
    }

    fun addProduct(productId: String) {
        val product = _products.value?.find { it?.productId == productId }
        val tempProduct = _userProducts.value ?: mutableListOf()
        val isExistedProduct = tempProduct.find { it.productId == productId }
        if (isExistedProduct != null) {
            val oldAmount = isExistedProduct.amount ?: 1
            val selectedProductIndex = tempProduct.indexOf(isExistedProduct)

            val modifiedProduct = tempProduct.apply {
                val productToModify = get(selectedProductIndex)
                set(selectedProductIndex, productToModify.copy(amount = oldAmount + 1))
            }
            _userProducts.value = modifiedProduct ?: mutableListOf()
        } else {
            tempProduct.add(product ?: Product())
        }
        _userProducts.value = tempProduct
    }

    fun onRemoveProduct(productId: String) {
        val selectedProduct = _userProducts.value?.find { it.productId == productId }
        val oldAmount = selectedProduct?.amount ?: 1
        val selectedProductIndex = _userProducts.value?.indexOf(selectedProduct) ?: 1

        val modifiedProduct = if (oldAmount == 1) {
            _userProducts.value?.apply { remove(selectedProduct) }
        } else {
            _userProducts.value?.apply {
                val productToModify = get(selectedProductIndex)
                set(selectedProductIndex, productToModify.copy(amount = oldAmount - 1))
            }
        }
        _userProducts.value = modifiedProduct ?: mutableListOf()
    }

    fun onPlusProduct(productId: String) {
        val selectedProduct = _userProducts.value?.find { it.productId == productId }
        val oldAmount = selectedProduct?.amount ?: 1
        val selectedProductIndex = _userProducts.value?.indexOf(selectedProduct) ?: 1

        val modifiedProduct = _userProducts.value?.apply {
            val productToModify = get(selectedProductIndex)
            set(selectedProductIndex, productToModify.copy(amount = oldAmount + 1))
        }
        _userProducts.value = modifiedProduct ?: mutableListOf()
    }

    fun payNow() {
        setLoading(true)
        val currentUserProducts = _userProducts.value
        databaseReference.child("UserProducts")
            .child(Constant.customerEmail.split("@")[0])
            .setValue(currentUserProducts)
            .addOnSuccessListener {
                setLoading(false)
                _payResponse.value = true
            }
    }

    fun getTotal() {
        var total = 0f
        _userProducts.value?.map {
            total += (it.amount * it.productPrice)
        }
        _total.value = total
    }

    fun getListCustomerInStack() {
        setLoading(true)
        databaseReference.child("UserProducts").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customers = mutableListOf<Customer>()
                snapshot.children.forEach { singleDataSnapshot ->
                    val customerEmail = singleDataSnapshot.key
                    val customerProducts = mutableListOf<Product>()
                    singleDataSnapshot.children.forEach { singleProduct ->
                        val item = singleProduct.getValue(Product::class.java)
                        customerProducts.add(item ?: Product())
                    }
                    customers.add(Customer(customerEmail.toString(), customerProducts))
                }
                _customerStack.value = customers
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun clearData() {
        _userProducts.value = mutableListOf()
    }

    fun reset() {
        _payResponse.value = false
        _total.value = 0F
    }
}