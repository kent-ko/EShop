package com.eshop.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eshop.Interfaces.ItemClickListener;
import com.eshop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProdcutName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProdcutName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        imageView = itemView.findViewById(R.id.product_image);
        txtProductPrice = itemView.findViewById(R.id.product_price);


    }

     public  void  setItemClickListener(ItemClickListener listener){

        this.listener = listener;
    }


    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);
    }
}
