package com.elize.stock.asynctask;

import android.os.AsyncTask;

public class BaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

    private final ExecuteListener<T> executeListener;
    private final DoneListener<T> doneListener;

    public BaseAsyncTask(ExecuteListener<T> executeListener,
                         DoneListener<T> doneListener) {
        this.executeListener = executeListener;
        this.doneListener = doneListener;
    }

    @Override
    protected T doInBackground(Void... voids) {
        return executeListener.whenExecuted();
    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        doneListener.whenDone(result);
    }

    public interface ExecuteListener<T> {
        T whenExecuted();
    }

    public interface DoneListener<T> {
        void whenDone(T result);
    }

}
