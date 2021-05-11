package com.elize.stock.retrofit.callback;

import com.elize.stock.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class BaseCallback<T> implements Callback<T> {
    private final ResponseCallback<T> callback;

    public BaseCallback(ResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T result = response.body();
            if (result != null) {
                callback.whenSuccessful(result);
            }
        } else {
            callback.whenFail(R.string.error_message_response_not_successful);

        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        callback.whenFail(R.string.error_message_failed_communication);
    }

    public interface ResponseCallback<T> {
        void whenSuccessful(T result);

        void whenFail(int error);
    }
}
