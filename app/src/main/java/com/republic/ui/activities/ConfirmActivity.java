package com.republic.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ConfirmActivity extends AppCompatActivity {

    private Button confirmButton;
    private String confirmCode;
    private UserController userController;
    private RepublicAlertDialog confirmAlertDialog;
    private boolean confirmFieldsDisabled;
    private EditText confirmTextField;
    private TextView confirmLabel;
    private Button sendCodeButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        initializeFields();
        setupConfirmButton();
        showConfirmAlertDialog();
        setupSendConfirmationCodeButton();
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
                if(confirmTextField.getText().toString().equals(confirmCode)) {
                    updateUser();
                }else{
                   Utils.makeToast(ConfirmActivity.this, R.string.confirmation_failed);
                }
            }
        });
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
        confirmAlertDialog.show();
    }

    private RepublicAlertDialog makeAlertDialog() {
        return new RepublicAlertDialog(this,
                RepublicAlertDialog.ButtonModes.YES_NO,
                new RepublicAlertDialog.ButtonMethods() {

                    @Override
                    public void yesButtonOperation() {
                        user  = RepublicFactory.getSession().getCurrentUser();

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

    private void updateUser() {

        if(user != null) {
            user.setIsConfirmed(true);
            userController.updateUser(user, new OperationCallback<User>() {
                @Override
                public  void performOperation(User arg) {
                    Utils.writeToPref(ConfirmActivity.this, Utils.Constants.USER_CONFIRMED, String.valueOf(true));
                    launchMainActivity();
                }

                @Override
                public void onOperationFailed(Throwable e) {
                    super.onOperationFailed(e);
                    Utils.makeToast(ConfirmActivity.this, R.string.confirmation_failed);
                }
            });
        }
    }

    private void sendConfirmSMS() {
        generateConfirmCode();
        String phoneNumber = user != null ? user.getPhone() : Utils.readFromPref(this, Utils.Constants.PHONE);
        if(! Utils.isEmptyString(phoneNumber)) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(phoneNumber, null, getConfirmMessage(), null, null);
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
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class LocalConstants {
        public static final String CONFIRM_SMS_MESSAGE = "Your Republic membership confirmation code is: ";
        public static final String ISCONFIRMED = "isconfirmed";
    }
}
