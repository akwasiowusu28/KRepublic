package com.republic.support;

/** *
 * Created by Akwasi Owusu on 7/15/15.
 */
public abstract class OperationCallback {

    public abstract <T>  void performOperation(T arg);

    public void onOperationFailed(Throwable e){
        //TODO log it
    }
}
