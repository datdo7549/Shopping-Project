package com.example.shoppingproject.feature.customerDetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingproject.R
import com.example.shoppingproject.model.Product

class ProductAdapter2(
    private val dataSet: MutableList<Product>,
) : RecyclerView.Adapter<ProductAdapter2.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: AppCompatTextView
        val tvProductAmount: AppCompatTextView
        val tvProductImage: AppCompatImageView

        init {
            // Define click listener for the ViewHolder's View.
            tvProductName = view.findViewById(R.id.tvProductName)
            tvProductAmount = view.findViewById(R.id.tvAmount)
            tvProductImage = view.findViewById(R.id.imvProductImg)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product_2, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvProductName.text = dataSet[position].productName
        viewHolder.tvProductAmount.text = dataSet[position].amount.toString()

        Glide
            .with(viewHolder.itemView.context)
            .load(dataSet[position].productImage)
            .centerCrop()
            .circleCrop()
            .into(viewHolder.tvProductImage);
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
