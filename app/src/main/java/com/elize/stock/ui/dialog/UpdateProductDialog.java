package com.elize.stock.ui.dialog;

import android.content.Context;

import com.elize.stock.model.Product;

public class UpdateProductDialog extends ProductDialog {

    private static final String TITLE = "Updating product";
    private static final String TITLE_POSITIVE_BUTTON = "Update";

    public UpdateProductDialog(Context context,
                               Product product,
                               ConfirmListener listener) {
        super(context, TITLE, TITLE_POSITIVE_BUTTON, listener, product);
    }
}
