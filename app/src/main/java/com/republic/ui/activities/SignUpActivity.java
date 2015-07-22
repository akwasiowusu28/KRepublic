package com.republic.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.republic.domain.UserController;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.support.Utils;

public class SignUpActivity extends AppCompatActivity {

    private UserController userController;
    private String phoneNumber;
    private ProgressDialog progressDialog;
    private Context context;
    private boolean phoneCheckAlreadyMade = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userController = RepublicFactory.getUserController();
        context = this;
        setupSignUpButton();
    }

    private void setupSignUpButton() {
        Button signUpButton = (Button) findViewById(R.id.createAccountButton);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpAsync().execute(null, null, null);
            }
        });
    }

    private void createAccount() {

        phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();

        if(!phoneCheckAlreadyMade) {
            userController.checkPhoneExists(phoneNumber, new OperationCallback<User>() {

                @Override
                public void performOperation(User arg) {
                    if (arg == null) {
                        performCreateAccount();
                        phoneCheckAlreadyMade = true;
                    } else {
                        Utils.makeToast(context, R.string.accountExistForPhone);
                        dismissProgressDialog();
                    }
                }
            });
        }
    }


    private void setupProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getText(R.string.pleaseWait));
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void performCreateAccount() {
        EditText passwordField = (EditText) findViewById(R.id.password);
        EditText confirmPasswordField = (EditText) findViewById(R.id.confirmPassword);

        String name = ((EditText) findViewById(R.id.userName)).getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (passwordsMatch(password, confirmPassword)) {
            createUser(name, phoneNumber, password);
        } else {
            Utils.switchInvalidFieldsBackColor(false, passwordField, confirmPasswordField);
            Utils.makeToast(context, R.string.passwordsNotMatch);
            dismissProgressDialog();
        }
    }

    private void createUser(final String name, final String phone, final String password) {

        userController.createUser(name, phone, password,Utils.getDeviceId(this), new OperationCallback<User>() {

            @Override
            public void performOperation(User arg) {
                Utils.writeToPref(context, Utils.Constants.USER_CONFIRMED, String.valueOf(false));
                dismissProgressDialog();
                userController.login(phone, password, null); // attempt log in, fire and forget...idea is, if successful, user will not be shown the log in page when the main page launches
                launchConfirmActivity();
            }

            @Override
            public void onOperationFailed(Throwable e) {
                Utils.makeToast(context, R.string.createAccountFailed);
                dismissProgressDialog();
            }
        });
    }

    private AsyncTask<Void, Void, Void> signUpAsync() {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                setupProgressDialog();
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                createAccount();
                return null;
            }
        };
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private void launchConfirmActivity() {
        Intent intent = new Intent(this, ConfirmActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
