package com.republic.support;

import com.republic.ui.support.Logger;

import java.util.List;

/** *
 * Created by Akwasi Owusu on 7/15/15.
 */
public abstract class OperationCallback<T> {

    public void performOperation(T arg){}

    public void performOperation(List<T> arg){}

    public void onOperationFailed(Throwable e){
        Logger.log(OperationCallback.class, e.getMessage());
    }
}
