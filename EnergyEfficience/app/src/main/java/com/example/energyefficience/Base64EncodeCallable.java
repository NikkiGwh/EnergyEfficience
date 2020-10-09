package com.example.energyefficience;

import java.util.Base64;
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
        byte[] buffer = createBigString(msgSize).getBytes();
        notifyResult(Base64.getEncoder().encodeToString(buffer), callback, resultHandler);
        return null;
    }
    private void notifyResult(final String result, final Base64Callback callback, final Handler resultHandler){
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }
    public String createBigString(int msgSize){
        //java chars are 2 bytes --> /2 = anzahl der chars * 1024 = Anzahl der Zahl für n KB
        msgSize = msgSize / 2;
        msgSize = msgSize* 1024;
        StringBuilder sb = new StringBuilder(msgSize);
        for(int i = 0; i< msgSize; i++){
            sb.append('a');
        }
        return sb.toString();
    }
}
