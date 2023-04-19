package com.example.shoppingproject.feature.customerDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingproject.feature.base.BaseViewModel
import com.example.shoppingproject.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CustomerDetailViewModel constructor(
    private val databaseReference: DatabaseReference
) : BaseViewModel() {

    var customerEmail: String = ""

    private var _userProducts: MutableLiveData<MutableList<Product>> = MutableLiveData()
    val userProducts: LiveData<MutableList<Product>> = _userProducts

    private var _deletedCustomerResponse: MutableLiveData<Boolean> = MutableLiveData()
    val deletedCustomerResponse: LiveData<Boolean> = _deletedCustomerResponse

    fun getListProductOfCustomer() {
        databaseReference.child("UserProducts").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { singleDataSnapshot ->
                    val email = singleDataSnapshot.key
                    if (customerEmail == email) {
                        val customerProducts = mutableListOf<Product>()
                        singleDataSnapshot.children.forEach { singleProduct ->
                            val item = singleProduct.getValue(Product::class.java)
                            customerProducts.add(item ?: Product())
                        }
                        _userProducts.value = customerProducts
                        return@forEach
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun deleteCustomer() {
        databaseReference.child("UserProducts").child(customerEmail).removeValue().addOnSuccessListener {
            _deletedCustomerResponse.value = true
        }
    }
}