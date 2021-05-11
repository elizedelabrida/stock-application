package com.elize.stock.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elize.stock.R;
import com.elize.stock.model.Product;
import com.elize.stock.repository.ProductRepository;
import com.elize.stock.ui.dialog.SaveProductDialog;
import com.elize.stock.ui.dialog.UpdateProductDialog;
import com.elize.stock.ui.recyclerview.adapter.ListProductsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListProductsActivity extends AppCompatActivity {

    private static final String APPBAR_TITLE = "Product List";
    private ListProductsAdapter adapter;
    private ProductRepository productRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        setTitle(APPBAR_TITLE);

        configureProductList();
        configureFabSaveProduct();

        productRepository = new ProductRepository(this);
        getProducts();
    }

    private void getProducts() {
        productRepository.getProducts(new ProductRepository.DataDoneCallback<List<Product>>() {
            @Override
            public void whenSuccessful(List<Product> result) {
                adapter.update(result);
            }

            @Override
            public void whenFail(int error) {
                displayError(R.string.error_message_get_products);
            }
        });
    }

    private void displayError(int message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    private void configureProductList() {
        RecyclerView listProducts = findViewById(R.id.activity_list_products_recyclerview);
        adapter = new ListProductsAdapter(this, this::openUpdateProduct);
        listProducts.setAdapter(adapter);

        adapter.setOnItemClickRemoveContextMenuListener((position, productSelected) ->
                productRepository.remove(productSelected,
                        remove(position)));
    }

    @NotNull
    private ProductRepository.DataDoneCallback<Void> remove(int position) {
        return new ProductRepository.DataDoneCallback<Void>() {
            @Override
            public void whenSuccessful(Void result) {
                adapter.remove(position);
            }

            @Override
            public void whenFail(int error) {
                displayError(R.string.error_message_remove_product);
            }
        };
    }


    private void configureFabSaveProduct() {
        FloatingActionButton fabAddProduct = findViewById(R.id.activity_list_fab_add_product);
        fabAddProduct.setOnClickListener(v -> openSaveProductDialog());
    }

    private void openSaveProductDialog() {
        new SaveProductDialog(this, this::save).display();
    }

    private void save(Product product) {
        productRepository.save(product, new ProductRepository.DataDoneCallback<Product>() {
            @Override
            public void whenSuccessful(Product result) {
                adapter.add(result);
            }

            @Override
            public void whenFail(int error) {
                displayError(R.string.error_message_save_product);
            }
        });
    }

    private void openUpdateProduct(int position, Product product) {
        new UpdateProductDialog(this, product,
                createdProduct -> update(position, createdProduct))
                .display();
    }

    private void update(int position, Product createdProduct) {
        productRepository.update(createdProduct, new ProductRepository.DataDoneCallback<Product>() {
            @Override
            public void whenSuccessful(Product updatedProduct) {
                adapter.update(position, updatedProduct);
            }

            @Override
            public void whenFail(int error) {
                displayError(R.string.error_message_update_product);
            }
        });
    }
}
