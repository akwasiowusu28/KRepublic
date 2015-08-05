package com.republic.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.republic.domain.UserController;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.support.RepublicAlertDialog;
import com.republic.ui.support.Utils;

import java.util.Calendar;

public class ConfirmActivity extends Activity {

    private Button confirmButton;
    private String confirmCode;
    private UserController userController;
    private RepublicAlertDialog confirmAlertDialog;
    private boolean confirmFieldsDisabled;
    private EditText confirmTextField;
    private TextView confirmLabel;
    private Button sendCodeButton;
    private ProgressDialog progressDialog;
    private String name;
    private String password;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        initializeFields();
        setupConfirmButton();
        showConfirmAlertDialog();
        setupSendConfirmationCodeButton();
        retrieveUserDetails();
    }

    private void retrieveUserDetails() {
        Intent intent = getIntent();
        name = intent.getStringExtra(Utils.Constants.USER_NAME);
        phone = intent.getStringExtra(Utils.Constants.PHONE);
        password = intent.getStringExtra(Utils.Constants.PASSWORD);
    }

    private void initializeFields() {
        userController = RepublicFactory.getUserController();
        confirmFieldsDisabled = false;
        confirmTextField = (EditText) findViewById(R.id.confirmTextField);
        confirmLabel = (TextView) findViewById(R.id.confirmLabel);
        sendCodeButton = (Button) findViewById(R.id.sendConfirmCode);

    }

    private void setupConfirmButton() {
        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String codeFromPref = Utils.readFromPref(ConfirmActivity.this, Utils.Constants.CONFIRM_CODE);

                if (confirmTextField.getText().toString().equals(codeFromPref)) {
                    createAccountAsync().execute();
                } else {
                    Utils.makeToast(ConfirmActivity.this, R.string.confirmation_failed);
                }
            }
        });
    }

    private void createUser() {

        userController.createUser(name, phone, password, Utils.getDeviceId(ConfirmActivity.this),
                new OperationCallback<User>() {

                    @Override
                    public void performOperation(User arg) {
                        userController.login(phone, password, userLoginCallBack);
                    }

                    @Override
                    public void onOperationFailed(Throwable e) {
                        Utils.makeToast(ConfirmActivity.this, R.string.createAccountFailed);
                        dismissProgressDialog();
                    }
                });
    }

    private OperationCallback<User> userLoginCallBack = new OperationCallback<User>() {
        @Override
        public void performOperation(User user) {
            Utils.writeToPref(ConfirmActivity.this, Utils.Constants.USER_TOKEN, user.getObjectId());
            dismissProgressDialog();
            launchMainActivity();
        }
    };

    private AsyncTask<Void, Void, Void> createAccountAsync() {
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

                createUser();

                return null;
            }
        };
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

    private void setupSendConfirmationCodeButton() {
        sendCodeButton = (Button) findViewById(R.id.sendConfirmCode);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmAlertDialog.show();
            }
        });
    }

    private void showConfirmAlertDialog() {
        confirmAlertDialog = makeAlertDialog();
        confirmAlertDialog.setTitle(getString(R.string.confirmNumber));
        confirmAlertDialog
                .setMessage(getString(R.string.confirmNumberQuestion));
        confirmAlertDialog.setCancelable(false);
        confirmAlertDialog.show();
    }

    private RepublicAlertDialog makeAlertDialog() {
        return new RepublicAlertDialog(this,
                RepublicAlertDialog.ButtonModes.YES_NO,
                new RepublicAlertDialog.ButtonMethods() {

                    @Override
                    public void yesButtonOperation() {
                        sendConfirmSMS();

                        if (confirmFieldsDisabled) {
                            changeConfirmFieldsVisibility(true);
                            sendCodeButton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void noButtonOperation() {
                        confirmFieldsDisabled = true;
                        changeConfirmFieldsVisibility(false);
                        sendCodeButton.setVisibility(View.VISIBLE);
                        this.cancelButtonOperation();
                    }
                });
    }

    private void changeConfirmFieldsVisibility(boolean makeVisible) {
        Utils.switchViewVisibility(makeVisible, confirmButton, confirmTextField, confirmLabel);
    }

    private void sendConfirmSMS() {
        generateConfirmCode();
        if (!Utils.isEmptyString(phone)) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(phone, null, getConfirmMessage(), null, null);
        }
    }

    private String getConfirmMessage() {
        return LocalConstants.CONFIRM_SMS_MESSAGE + confirmCode;
    }

    private void generateConfirmCode() {
        String currentTimeString = String.valueOf(Calendar.getInstance()
                .getTimeInMillis());
        confirmCode = currentTimeString
                .substring(currentTimeString.length() - 4);
        Utils.writeToPref(this, Utils.Constants.CONFIRM_CODE, confirmCode);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class LocalConstants {
        public static final String CONFIRM_SMS_MESSAGE = "Your Republic membership confirmation code is: ";
    }
}
