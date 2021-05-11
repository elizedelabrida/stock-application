package com.elize.stock.ui.dialog;

import android.content.Context;

public class SaveProductDialog extends ProductDialog {

    private static final String TITLE = "Saving product";
    private static final String TITLE_POSITIVE_BUTTON = "Save";

    public SaveProductDialog(Context context,
                             ConfirmListener listener) {
        super(context, TITLE, TITLE_POSITIVE_BUTTON, listener);
    }

}
