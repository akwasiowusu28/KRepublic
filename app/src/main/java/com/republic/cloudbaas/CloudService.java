package com.republic.cloudbaas;

import com.backendless.BackendlessUser;
import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by Akwasi Owusu on 7/21/15.
 */
 public interface CloudService {

     <T> void addDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);

    <T> void initialize(T dataContext);

     <T> void addDataItemSync(T dataItem, Class<T> itemClass);

     <T> void updateDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);

     <T> void deleteDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack);

     <T> void findItemById(int itemId, Class<T> itemClass,  OperationCallback<T> operationCallBack) ;

     <T> void findItemByFieldName(String fieldName, String searchValue,
                                        Class<T> itemClass, OperationCallback<T> operationCallBack);

     <T> void findAllItems(Class<T> itemClass,  OperationCallback<T> operationCallBack) ;

     <T> void getLastInsertedItem(Class<T> itemClass,  OperationCallback<T> operationCallBack);

     <T> void findItemsByFieldName(String fieldName, String searchValue,
                                         Class<T> itemClass, OperationCallback<T> operationCallBack) ;

     <T> void findItemsByWhereClause(String whereClause,
                                           Class<T> itemClass, OperationCallback<T> operationCallBack) ;

     <T, E> void findItemsBetween(String fieldName, E lowerBound,
                                        E upperBound, Class<T> itemClass,  OperationCallback<T> operationCallBack) ;

     void addUser(User user, OperationCallback<BackendlessUser> operationCallBack) ;

     void updateUser(User user, OperationCallback<BackendlessUser> operationCallBack) ;
     void login(String userId, String password, OperationCallback<BackendlessUser> operationCallBack) ;

    String getUserToken();
}
