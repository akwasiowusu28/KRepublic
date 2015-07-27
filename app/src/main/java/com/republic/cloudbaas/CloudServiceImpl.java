package com.republic.cloudbaas;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.exceptions.CloudRequestFailedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class CloudServiceImpl implements CloudService {

    private boolean isCollectionOperation;
    //This is to make sure the right objects are passed to the callback method in case there are multiple request that return collections.
    // Increment this any time you run a request that has a potential of returning a collection
    private int collectionRequestsCount = 0;

    public CloudServiceImpl() {
        isCollectionOperation = false;
    }

    public <T> void addDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack) {

            Backendless.Persistence.of(itemClass).save(dataItem,
                    new BackendAsynCallBack<>(operationCallBack, itemClass));

    }

    public <T> void initialize(T dataContext) {
        Backendless.initApp(dataContext, LocalConstants.APPLICATION_ID,
                LocalConstants.SECRET_KEY, LocalConstants.VERSION);
    }

    public <T> void addDataItemSync(T dataItem, Class<T> itemClass) {
            Backendless.Persistence.of(itemClass).save(dataItem);

    }

    public <T> void updateDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack) {
        addDataItem(dataItem, itemClass, operationCallBack);
    }

    public <T> void deleteDataItem(T dataItem, Class<T> itemClass,  OperationCallback<T> operationCallBack) {

            Backendless.Persistence.of(itemClass).remove(dataItem,
                    AsyncCallback.class.cast(operationCallBack));

    }

    public <T> void findItemById(int itemId, Class<T> itemClass,  OperationCallback<T> operationCallBack) {
            findItemByFieldName(LocalConstants.ID, String.valueOf(itemId), itemClass,
                    operationCallBack);
    }

    public <T> void findItemByFieldName(String fieldName, String searchValue,
                                        Class<T> itemClass, OperationCallback<T> operationCallBack) {
            String whereClause = fieldName + "='" + searchValue + "'";
            BackendlessDataQuery query = new BackendlessDataQuery(whereClause);
            Backendless.Persistence
                    .of(itemClass)
                    .find(query,
                            (AsyncCallback<BackendlessCollection<T>>) new BackendAsynCallBack<>(operationCallBack, itemClass));

    }

    public <T> void findAllItems(Class<T> itemClass,  OperationCallback<T> operationCallBack) {
            isCollectionOperation = true;
            collectionRequestsCount++;
            BackendlessDataQuery query = new BackendlessDataQuery();
            query.setPageSize(100);

            if (itemClass != BackendlessUser.class) {

                QueryOptions queryOptions = new QueryOptions();
                queryOptions.addSortByOption("created desc");
                query.setQueryOptions(queryOptions);
            }

            Backendless.Persistence
                    .of(itemClass)
                    .find(query, (AsyncCallback<BackendlessCollection<T>>) new BackendAsynCallBack<>(
                            operationCallBack, itemClass));
    }

    public <T> void getLastInsertedItem(Class<T> itemClass,  OperationCallback<T> operationCallBack) {

            Backendless.Persistence.of(itemClass).findLast(
                    new BackendAsynCallBack<>(operationCallBack, itemClass));

    }

    public <T> void findItemsByFieldName(String fieldName, String searchValue,
                                         Class<T> itemClass, OperationCallback<T> operationCallBack) {
            isCollectionOperation = true;
            collectionRequestsCount++;
            String whereClause = fieldName + "='" + searchValue + "'";
            BackendlessDataQuery query = new BackendlessDataQuery(whereClause);
            Backendless.Data
                    .of(itemClass)
                    .find(query,
                            (AsyncCallback<BackendlessCollection<T>>) new BackendAsynCallBack<>(
                                    operationCallBack, itemClass));
    }

    public <T> void findItemsByWhereClause(String whereClause,
                                           Class<T> itemClass, OperationCallback<T> operationCallBack) {
            isCollectionOperation = true;
            collectionRequestsCount++;
            BackendlessDataQuery query = new BackendlessDataQuery(whereClause);
            Backendless.Persistence
                    .of(itemClass)
                    .find(query,
                            (AsyncCallback<BackendlessCollection<T>>) new BackendAsynCallBack<>(
                                    operationCallBack, itemClass));

    }

    public <T, E> void findItemsBetween(String fieldName, E lowerBound,
                                        E upperBound, Class<T> itemClass,  OperationCallback<T> operationCallBack) {
            isCollectionOperation = true;
            collectionRequestsCount++;

            // Find a way to remove the date formatting thingy to a different
            // method.

            String lowerBoundString = "";
            String upperBoundString = "";

            if (lowerBound instanceof Date && upperBound instanceof Date) {
                SimpleDateFormat dateFormater = new SimpleDateFormat(LocalConstants.DATEFORMAT);
                lowerBoundString = dateFormater.format((Date) lowerBound);
                upperBoundString = dateFormater.format((Date) upperBound);
            }

            String whereClause = fieldName + LocalConstants.IS_GREATER_OR_EQUAL_TO
                    + lowerBoundString + LocalConstants.AND + fieldName + LocalConstants.IS_LESS_OR_EQUAL_TO
                    + upperBoundString + LocalConstants.SINGLE_QUOTE;
            BackendlessDataQuery query = new BackendlessDataQuery(whereClause);
            Backendless.Persistence
                    .of(itemClass)
                    .find(query,
                            (AsyncCallback<BackendlessCollection<T>>) new BackendAsynCallBack<>(
                                    operationCallBack, itemClass));

    }

    public void addUser(User user, OperationCallback<BackendlessUser> operationCallBack) {

        BackendlessUser backendlessUser = (BackendlessUser) user;
        Backendless.UserService.register(backendlessUser,
                new BackendAsynCallBack<>(operationCallBack, BackendlessUser.class));
    }

    public void updateUser(User user, OperationCallback<BackendlessUser> operationCallBack) {
        Backendless.UserService.update(user, new BackendAsynCallBack<>(operationCallBack, BackendlessUser.class));
    }

    public void login(String userId, String password, OperationCallback<BackendlessUser> operationCallBack) {

        Backendless.UserService.login(userId, password,
                new BackendAsynCallBack<>(operationCallBack, BackendlessUser.class));

    }

    @Override
    public String getUserToken() {
        return UserTokenStorageFactory.instance().getStorage().get();
    }

    private class LocalConstants {
        public static final String APPLICATION_ID = "E7F75A25-A1E0-CC13-FF5B-111FBA427400";
        public static final String SECRET_KEY = "7F9A514F-01DF-409D-FF72-3F1E8C9AFC00";
        public static final String VERSION = "v1";
        public static final String ID = "Id";
        public static final String DATEFORMAT = "yyyyMMddHHmmss";
        public static final String IS_GREATER_OR_EQUAL_TO = ">= '";
        public static final String AND = "' and ";
        public static final String IS_LESS_OR_EQUAL_TO = "<='";
        public static final String SINGLE_QUOTE = "'";
    }

    private class BackendAsynCallBack<T> implements AsyncCallback<T> {

        private final OperationCallback<T> operationCallBack;
        private Class<T> itemClass;

        public BackendAsynCallBack(OperationCallback<T> operationCallBack, Class<T> itemClass) {
            this.operationCallBack = operationCallBack;
            this.itemClass = itemClass;
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            if (operationCallBack != null) {
                operationCallBack.onOperationFailed(new CloudRequestFailedException(
                        fault.getMessage() + " for class: " + itemClass.getSimpleName()));
            }
        }

        @Override
        public void handleResponse(T responseObject) {
            if (operationCallBack != null)
                if (responseObject instanceof BackendlessCollection) {
                    List<T> responseData = ((BackendlessCollection<T>) responseObject).getData();
                    if (!isCollectionOperation) {
                        T item = responseData.size() > 0 ? responseData.get(0)
                                : null;
                        operationCallBack.performOperation(item);
                    } else {
                        collectionRequestsCount--;
                        operationCallBack
                                .performOperation(responseData);
                    }
                } else {
                    operationCallBack.performOperation(responseObject);
                }
            isCollectionOperation = collectionRequestsCount > 0 && isCollectionOperation;
        }
    }

}
