package com.republic.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.republic.domain.UserController;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;

public class LoginActivity extends AppCompatActivity {

    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                    break;
                case R.id.signupButton:
                    break;
            }
        }
    };

    private void login(){
        userController.login("","",null);

    }
}
