package com.patient;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.patient.framework.model.User;
import com.patient.framework.repository.UserRepository;
import com.patient.framework.service.UserServices;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class reset2Activity extends AppCompatActivity{

    private static final int REQUEST_READ_CONTACTS = 0;
    private SharedPreferences sharedPreferences;
    // UI references.
    private EditText mEmailView,mPasswordView;
    private View mLoginFormView;
    private UserRepository userRepository;
    private User current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset2);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set up the reset form.
        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        userRepository=UserRepository.getInstance(reset2Activity.this);
        current=new User();

        mEmailView = (EditText) findViewById(R.id.psw_reset2);
        mPasswordView = (EditText) findViewById(R.id.rpsw_reset2);

        Button mSignInButton = (Button) findViewById(R.id.reset2_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReset();
            }
        });

        mLoginFormView = findViewById(R.id.reset2_form);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                startActivity(new Intent(reset2Activity.this,LoginActivity.class));
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void attemptReset() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String psw = mEmailView.getText().toString();
        String rpsw = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(psw) && !isPasswordValid(psw)) {
            mEmailView.setError("密码长度有误");
            focusView = mEmailView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(rpsw) && !isPasswordValid(rpsw)) {
            mPasswordView.setError("密码长度有误");
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.equals(psw,rpsw)) {
            mPasswordView.setError(getString(R.string.error_incorrect_rpassword));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String eml=sharedPreferences.getString("eml","");
            String card=sharedPreferences.getString("card","");
            SharedPreferences.Editor editor=sharedPreferences.edit();
            userRepository=UserRepository.getInstance(reset2Activity.this);
            current=new User();
            current.setEml(eml);
            current.setCard(card);
            current.setPsw(psw);
            if(userRepository.resetUserPsw(current)==1){
                Toast.makeText(this,"密码重置成功，即将返回登录界面",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(reset2Activity.this,LoginActivity.class);
                editor.putBoolean("valid",true);
                editor.commit();
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this,"用户信息不匹配",Toast.LENGTH_SHORT).show();
                editor.putBoolean("valid",false);
                editor.commit();
                mEmailView.requestFocus();
            }

            }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

