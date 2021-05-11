package com.elize.stock.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.elize.stock.R;
import com.elize.stock.model.Product;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

abstract public class ProductDialog {

    private final String title;
    private final String titlePositiveButton;
    private final ConfirmListener listener;
    private final Context context;
    private static final String TITLE_NEGATIVE_BUTTON = "Cancel";
    private Product product;

    ProductDialog(Context context,
                  String title,
                  String titlePositiveButton,
                  ConfirmListener listener) {
        this.title = title;
        this.titlePositiveButton = titlePositiveButton;
        this.listener = listener;
        this.context = context;
    }

    ProductDialog(Context context,
                  String title,
                  String titlePositiveButton,
                  ConfirmListener listener,
                  Product product) {
        this(context, title, titlePositiveButton, listener);
        this.product = product;
    }

    public void display() {
        @SuppressLint("InflateParams") View createdView = LayoutInflater.from(context)
                .inflate(R.layout.product_dialog, null);
        tryToFillView(createdView);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(createdView)
                .setPositiveButton(titlePositiveButton, (dialog, which) -> {
                    EditText editTextName = getEditText(createdView, R.id.product_dialog_name);
                    EditText editTextPrice = getEditText(createdView, R.id.product_dialog_price);
                    EditText editTextQuantity = getEditText(createdView, R.id.product_dialog_quantity);
                    createProduct(editTextName, editTextPrice, editTextQuantity);
                })
                .setNegativeButton(TITLE_NEGATIVE_BUTTON, null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void tryToFillView(View createdView) {
        if (product != null) {
            TextView textViewId = createdView.findViewById(R.id.product_dialog_id);
            textViewId.setText(String.valueOf(product.getId()));
            textViewId.setVisibility(View.VISIBLE);
            EditText editTextName = getEditText(createdView, R.id.product_dialog_name);
            editTextName.setText(product.getNome());
            EditText editTextPrice = getEditText(createdView, R.id.product_dialog_price);
            editTextPrice.setText(product.getPreco().toString());
            EditText editTextQuantity = getEditText(createdView, R.id.product_dialog_quantity);
            editTextQuantity.setText(String.valueOf(product.getQuantidade()));
        }
    }

    private void createProduct(EditText editTextName, EditText editTextPrice, EditText editTextQuantity) {
        String name = editTextName.getText().toString();
        BigDecimal price = tryToConvertPrice(editTextPrice);
        int quantity = tryToConvertQuantity(editTextQuantity);
        long id = fulfillId();
        Product product = new Product(id, name, price, quantity);
        listener.whenConfirmed(product);
    }

    private long fulfillId() {
        if (product != null) {
            return product.getId();
        }
        return 0;
    }

    private BigDecimal tryToConvertPrice(EditText editTextPrice) {
        try {
            return new BigDecimal(editTextPrice.getText().toString());
        } catch (NumberFormatException ignored) {
            return BigDecimal.ZERO;
        }
    }

    private int tryToConvertQuantity(EditText editTextQuantity) {
        try {
            return Integer.valueOf(
                    editTextQuantity.getText().toString());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private EditText getEditText(View createdView, int idTextInputLayout) {
        TextInputLayout textInputLayout = createdView.findViewById(idTextInputLayout);
        return textInputLayout.getEditText();
    }

    public interface ConfirmListener {
        void whenConfirmed(Product product);
    }


}
