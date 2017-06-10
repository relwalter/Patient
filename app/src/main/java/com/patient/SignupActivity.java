package com.patient;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.patient.framework.model.User;
import com.patient.framework.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView,mRPasswordView,mCardView,mPhoneView;
    private UserRepository userRepository;
    private User current;
    private String email,password,card,phone,rPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        userRepository=new UserRepository(this);
        current=new User();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_signup);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password_signup);
        mRPasswordView = (EditText) findViewById(R.id.rpassword_signup);
        mCardView = (EditText) findViewById(R.id.card_signup);
        mPhoneView = (EditText) findViewById(R.id.phone_signup);

        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkExistence();
                }
            }
        });

        mPhoneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup) {
                    if(attemptSignup()){
                        Intent intent
                                =new Intent(SignupActivity.this, CompleteInfoActivity.class);
                        intent.putExtra("card",card);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });

        Button mCompleteButton = (Button) findViewById(R.id.complete_button);
        mCompleteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(attemptSignup()){
                    Intent intent=new Intent(SignupActivity.this,CompleteInfoActivity.class);
                    intent.putExtra("card",card);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                finish();
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void checkExistence(){
        mEmailView.setError(null);
        email = mEmailView.getText().toString();
        current.setEml(email);
        if(userRepository.checkUser(current)){
            mEmailView.setError("用户已存在");
        }
    }


    private boolean attemptSignup() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        rPassword = mRPasswordView.getText().toString();
        card = mCardView.getText().toString();
        phone = mPhoneView.getText().toString();
        current.setEml(email);
        current.setPsw(password);
        current.setCard(card);
        current.setPhone(phone);
        Log.d("signup",current.toString());

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(card) && !isCardValid(card)) {
            mCardView.setError("卡号长度有误");
            focusView = mCardView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            mPhoneView.setError("手机号有误");
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(rPassword) && !isPasswordValid(rPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRPasswordView;
            cancel = true;
        }

        if (!TextUtils.equals(password,rPassword)) {
            mRPasswordView.setError(getString(R.string.error_incorrect_rpassword));
            focusView = mRPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            switch(userRepository.addUser(current)){
                case 1:{
                    SharedPreferences.Editor editor=getSharedPreferences("current",MODE_PRIVATE).edit();
                    editor.putString("card",card);
                    editor.putString("eml",email);
                    editor.putBoolean("valid",false);
                    editor.commit();
                    Toast.makeText(this,"注册成功！进入下一步",Toast.LENGTH_SHORT).show();
                    return true;
                }
                case -1:{
                    Toast.makeText(this,"注册失败！用户已存在",Toast.LENGTH_SHORT).show();
                    mEmailView.requestFocus();
                    return false;
                }
                default:{
                    Toast.makeText(this,"注册失败！请稍后尝试",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }

    private boolean isPhoneValid(String phone){
        return (phone.length()==11)&&(phone.startsWith("1"));
    }

    private boolean isCardValid(String card){
        return card.length()==10;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

