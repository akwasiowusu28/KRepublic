package com.republic.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends Activity {

    private UserController userController;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;

        setupButtons();
    }

    private void setupButtons(){
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(listener);

        userController = RepublicFactory.getUserController();

        Button signUpButton = (Button)findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.loginButton:
                    login();
                    break;
                case R.id.signupButton:
                    launchSignUpActivity();
                    break;
            }
        }
    };

    private void login(){
        final String phone = ((EditText)findViewById(R.id.loginPhoneNumber)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPassword)).getText().toString();
        userController.login(phone, password, new OperationCallback<User>() {
            @Override
            public void performOperation(User user) {

                if(user == null){
                    Utils.makeToast(context, R.string.login_failed);
                }
                else {
                    //write the user id to phone so we can perform all subsequent cloud requests with that
                    Utils.writeToPref(context, Utils.Constants.USER_TOKEN, user.getObjectId());
                    Intent intent = new Intent(context, user.isConfirmed() ? MainActivity.class : ConfirmActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void launchSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
