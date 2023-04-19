package com.example.shoppingproject.feature.homepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingproject.R
import com.example.shoppingproject.model.Customer

class CustomerAdapter(
    private val dataSet: MutableList<Customer>,
    private val customerClicked: (String) -> Any
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCustomerName: AppCompatTextView

        init {
            // Define click listener for the ViewHolder's View.
            tvCustomerName = view.findViewById(R.id.tvCustomerName)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_customer, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvCustomerName.text = dataSet[position].customerEmail

        viewHolder.itemView.setOnClickListener {
            customerClicked.invoke(dataSet[position].customerEmail)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
