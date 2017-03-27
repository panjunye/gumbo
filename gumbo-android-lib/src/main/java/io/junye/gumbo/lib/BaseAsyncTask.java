package io.junye.gumbo.lib;

import android.os.AsyncTask;

/**
 * Created by Junye on 2017/3/24 0024.
 */

public  abstract class BaseAsyncTask<Params,Progress,Result> extends AsyncTask<Params,Progress,Result> {

    private ResponseListener<Result> responseListener;

    public interface ResponseListener<Result>{
        void onFinish(Result result);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        responseListener.onFinish(result);
    }

    public ResponseListener<Result> getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(ResponseListener<Result> responseListener) {
        this.responseListener = responseListener;
    }
}
