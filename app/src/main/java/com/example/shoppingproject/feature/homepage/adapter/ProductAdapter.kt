package com.example.shoppingproject.feature.homepage.adapter

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

class ProductAdapter(
    private val dataSet: MutableList<Product>,
    private val onRemoveClick: (String) -> Any,
    private val onPlusClick: (String) -> Any,
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: AppCompatTextView
        val tvProductAmount: AppCompatTextView
        val tvProductImage: AppCompatImageView

        val btnRemove: AppCompatImageButton
        val btnAdd: AppCompatImageButton

        init {
            // Define click listener for the ViewHolder's View.
            tvProductName = view.findViewById(R.id.tvProductName)
            tvProductAmount = view.findViewById(R.id.tvAmount)
            tvProductImage = view.findViewById(R.id.imvProductImg)
            btnRemove = view.findViewById(R.id.btnRemove)
            btnAdd = view.findViewById(R.id.btnPlus)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvProductName.text = dataSet[position].productName
        viewHolder.tvProductAmount.text = dataSet[position].amount.toString()

        viewHolder.btnRemove.setOnClickListener {
            onRemoveClick.invoke(dataSet[position].productId)
        }

        viewHolder.btnAdd.setOnClickListener {
            onPlusClick.invoke(dataSet[position].productId)
        }

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
