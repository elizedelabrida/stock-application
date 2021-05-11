package com.elize.stock.retrofit.callback;

import com.elize.stock.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class VoidCallBack implements Callback<Void> {
    private final ResponseCallback<Void> callback;

    public VoidCallBack(ResponseCallback<Void> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {
        if (response.isSuccessful()) {
            callback.whenSuccessful();
        } else {
            callback.whenFail(R.string.error_message_response_not_successful);

        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.whenFail(R.string.error_message_failed_communication);
    }

    public interface ResponseCallback<T> {
        void whenSuccessful();

        void whenFail(int error);
    }
}
