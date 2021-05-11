package com.elize.stock.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elize.stock.R;
import com.elize.stock.model.Product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListProductsAdapter extends
        RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private OnItemClickRemoveContextMenuListener
            onItemClickRemoveContextMenuListener = (position, removedProduct) -> {
    };
    private final Context context;
    private final List<Product> products = new ArrayList<>();

    public ListProductsAdapter(Context context,
                               OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void setOnItemClickRemoveContextMenuListener(OnItemClickRemoveContextMenuListener onItemClickRemoveContextMenuListener) {
        this.onItemClickRemoveContextMenuListener = onItemClickRemoveContextMenuListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View createdView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(createdView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.fill(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void update(List<Product> products) {
        // Avoid concurrency issues
        notifyItemRangeRemoved(0, this.products.size());
        this.products.clear();
        this.products.addAll(products);
        this.notifyItemRangeInserted(0, this.products.size());
    }

    public void add(Product... products) {
        int currentSize = this.products.size();
        Collections.addAll(this.products, products);
        int newSize = this.products.size();
        notifyItemRangeInserted(currentSize, newSize);
    }

    public void update(int position, Product product) {
        products.set(position, product);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewId;
        private final TextView textViewName;
        private final TextView textViewPrice;
        private final TextView textViewQuantity;
        private Product product;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.product_item_id);
            textViewName = itemView.findViewById(R.id.product_item_name);
            textViewPrice = itemView.findViewById(R.id.product_item_price);
            textViewQuantity = itemView.findViewById(R.id.product_item_quantity);
            configureItemClick(itemView);
            configureContextMenu(itemView);
        }

        private void configureContextMenu(@NonNull View itemView) {
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                new MenuInflater(context).inflate(R.menu.list_products_menu, menu);
                menu.findItem(R.id.menu_list_products_remove)
                        .setOnMenuItemClickListener(
                                item -> {
                                    int productPosition = getAdapterPosition();
                                    onItemClickRemoveContextMenuListener
                                            .onItemClick(productPosition, product);
                                    return true;
                                });
            });
        }

        private void configureItemClick(@NonNull View itemView) {
            itemView.setOnClickListener(v -> onItemClickListener
                    .onItemClick(getAdapterPosition(), product));
        }

        void fill(Product product) {
            this.product = product;
            textViewId.setText(String.valueOf(product.getId()));
            textViewName.setText(product.getNome());
            textViewPrice.setText(formatToCurrency(product.getPreco()));
            textViewQuantity.setText(String.valueOf(product.getQuantidade()));
        }

        private String formatToCurrency(BigDecimal value) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            return formatter.format(value);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, Product product);
    }

    public interface OnItemClickRemoveContextMenuListener {
        void onItemClick(int position, Product removedProduct);
    }

}
