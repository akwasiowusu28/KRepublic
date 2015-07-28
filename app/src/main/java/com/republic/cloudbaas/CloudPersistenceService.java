package com.republic.cloudbaas;

import com.backendless.BackendlessUser;
import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by Akwasi Owusu on 7/21/15.
 */
 public interface CloudPersistenceService {

     <T> void addDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);

    <T> void initialize(T dataContext);

     <T> void addDataItemSync(T dataItem, Class<T> itemClass);

     <T> void updateDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);

     <T> void deleteDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);



}
