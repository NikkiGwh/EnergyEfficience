package com.example.energyefficience;

import java.util.concurrent.Callable;
import android.os.Handler;
import android.util.Log;

public class Base64EncodeCallable implements Callable {
    private int msgSize = 0; //in KB
    private Base64Callback callback;
    private Handler resultHandler;
    public Base64EncodeCallable(int msgSize, Base64Callback callback, Handler resultHandler){
        this.callback = callback;
        this.resultHandler = resultHandler;
        this.msgSize = msgSize;
    }


    @Override
    public Object call() throws Exception {
        String plainText = Base64Implementation.createBigString(msgSize);
        String encodedText = Base64Implementation.encodeSynchronously(plainText);
        notifyResult(encodedText, callback, resultHandler);
        return null;
    }
    private void notifyResult(final String result, final Base64Callback callback, final Handler resultHandler){
       // Log.i("aktueller Thread fertig: ", Thread.currentThread().getName());
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }
}
