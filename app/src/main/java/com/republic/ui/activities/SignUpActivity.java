package com.republic.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.republic.domain.UserController;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.support.CountryCodeList;
import com.republic.ui.support.Utils;

import java.util.HashMap;

public class SignUpActivity extends Activity {

    private UserController userController;
    private String phoneNumber;
    private ProgressDialog progressDialog;
    private Context context;
    private boolean phoneCheckAlreadyMade = false;
    private String name;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userController = RepublicFactory.getUserController();
        context = this;
        setupSignUpButton();
        setupAreaCodeField();
    }

    private void setupSignUpButton() {
        Button signUpButton = (Button) findViewById(R.id.createAccountButton);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText passwordField = (EditText) findViewById(R.id.password);
                EditText confirmPasswordField = (EditText) findViewById(R.id.confirmPassword);

                name = ((EditText) findViewById(R.id.userName)).getText().toString();
                password = passwordField.getText().toString();
                confirmPassword = confirmPasswordField.getText().toString();

                phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
                String goodChars = Utils.Constants.NUMS_REGEX;

                if (isNoFieldEmpty()) {

                    if ((!phoneNumber.matches(goodChars))) {

                        Utils.makeToast(context, R.string.wrong_number_input);

                    } else {

                        if (isPasswordMatch()) {
                            String code = ((TextView) findViewById(R.id.countryCodeField)).getText().toString();
                            phoneNumber = code + phoneNumber;
                            signUpAsync().execute(null, null, null);
                        }

                    }
                }
            }
        });
    }

    private boolean isNoFieldEmpty(){
        boolean noneEmpty = true;
        String emptyString = Utils.Constants.EMPTY_STRING;
        if(name.trim().equals(emptyString)){
            Utils.makeToast(context, R.string.empty_name);
            noneEmpty = false;
        }
        else if (phoneNumber.trim().equals(emptyString)){
            Utils.makeToast(context, R.string.empty_phone);
            noneEmpty = false;
        }
        else if(password.trim().equals(emptyString)){
            Utils.makeToast(context, R.string.empty_pass);
            noneEmpty = false;
        }
        return noneEmpty;
    }

    private void setupAreaCodeField(){
        TextView areaCodeField = (TextView)findViewById(R.id.countryCodeField);

        TelephonyManager telephonyManager  = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = telephonyManager.getSimCountryIso().toUpperCase();

        HashMap<String, String> countryCodes = new CountryCodeList().getCountryCodes();
        if(countryCodes.containsKey(countryCode)){
           areaCodeField.setText(countryCodes.get(countryCode));
        }
    }

    private void createAccount() {

        if(!phoneCheckAlreadyMade) {
            userController.checkPhoneExists(phoneNumber, new OperationCallback<User>() {

                @Override
                public void performOperation(User arg) {
                    if (arg == null) {
                        dismissProgressDialog();
                        confirmPhone(name, phoneNumber, password);
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

    private boolean isPasswordMatch() {

        boolean isMatch = false;
        if (passwordsMatch(password, confirmPassword)) {
           isMatch = true;
        } else {
            Utils.makeToast(context, R.string.passwordsNotMatch);
        }
        return isMatch;
    }

    private void confirmPhone(final String name, final String phone, final String password) {

        Utils.writeToPref(this,Utils.Constants.USER_NAME, name);
        Utils.writeToPref(this, Utils.Constants.PASSWORD, password);
        Utils.writeToPref(this, Utils.Constants.PHONE, phone);
        Utils.writeToPref(this, Utils.Constants.USER_CONFIRMED, String.valueOf(false));
        Utils.writeToPref(this, Utils.Constants.USER_TOKEN, Utils.Constants.USER_TEMP_TOKEN);
        Intent intent = new Intent(this, ConfirmActivity.class);
        startActivity(intent);

        finish();
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
