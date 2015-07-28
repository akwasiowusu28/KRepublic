package com.republic.cloudbaas;

import com.republic.support.OperationCallback;

/**
 * Created by aowusu on 7/28/2015.
 */
public interface CloudQueryService {

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
}
