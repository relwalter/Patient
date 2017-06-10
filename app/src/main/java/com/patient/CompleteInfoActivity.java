package com.patient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.patient.framework.model.Patient;
import com.patient.framework.model.User;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.repository.UserRepository;

import java.util.Calendar;


public class CompleteInfoActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText mNameView,mGenderView;
    private TextView mBirthView,mBirthPopup;
    private Button mCompleteButton;
    private PatientRepository patientRepository;
    private UserRepository userRepository;
    private Patient current;
    private String card;
    private int nowYear,nowMonth,nowDay,year,month,day;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        userRepository=UserRepository.getInstance(this);
        patientRepository=new PatientRepository(this);
        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        current=new Patient();
        card=getIntent().getStringExtra("card");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name_signup);
        mGenderView = (EditText) findViewById(R.id.gender_signup);
        mBirthView = (TextView) findViewById(R.id.birth_complete);
        mBirthPopup = (TextView) findViewById(R.id.birth_complete_popup);
        Calendar c = Calendar.getInstance();
        nowYear = c.get(Calendar.YEAR);
        nowMonth = c.get(Calendar.MONTH)+1;
        nowDay = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        mBirthView.setText(year+" - "+(month+1)+" - "+day);
        mCompleteButton = (Button) findViewById(R.id.sign_up_button);
        mCompleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptComplete();
            }
        });
        mBirthView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mBirthView.setText(year+" - "+(monthOfYear+1)+" - "+dayOfMonth);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(CompleteInfoActivity.this,R.style.DiaglogTheme,listener,Integer.parseInt(mBirthView.getText().toString().split(" - ")[0]),Integer.parseInt(mBirthView.getText().toString().split(" - ")[1])-1,Integer.parseInt(mBirthView.getText().toString().split(" - ")[2]));
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                startActivity(new Intent(CompleteInfoActivity.this,LoginActivity.class));
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptComplete() {

        // Reset errors.
        mNameView.setError(null);
        mGenderView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String gender = mGenderView.getText().toString();
        String birth = mBirthView.getText().toString();
        int yearOfBirth = Integer.parseInt(birth.split(" - ")[0]);
        int monthOfBirth = Integer.parseInt(birth.split(" - ")[1]);
        int dayOfBirth = Integer.parseInt(birth.split(" - ")[2]);
        current.setCard(card);
        current.setName(name);
        current.setGender(gender);
        current.setAge(nowYear-yearOfBirth);
        Log.d("complete: ",current.toString());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(gender)) {
            mGenderView.setError(getString(R.string.error_field_required));
            focusView = mGenderView;
            cancel = true;
        }

        if (!("男".equals(gender)||"女".equals(gender)||"Male".equals(gender)||"Female".equals(gender))) {
            mGenderView.setError("格式不正确");
            focusView = mGenderView;
            cancel = true;
        }

        if (yearOfBirth>nowYear||(yearOfBirth==nowYear&&monthOfBirth>nowMonth)||(yearOfBirth==nowYear&&monthOfBirth==nowMonth&&dayOfBirth>nowDay)) {
            mBirthPopup.setError("");
            mBirthPopup.setText("出生日期（不能晚于今天）");
            focusView = mBirthPopup;
            mBirthView.setTextColor(R.color.dateRed);
            mBirthView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            switch(patientRepository.addPatient(current)){
                case 1:{
                    setSharedPreferences();
                    if(sharedPreferences.getBoolean("valid",false)){
                        Toast.makeText(this,"登记成功",Toast.LENGTH_SHORT).show();
                        Intent intent
                                =new Intent(CompleteInfoActivity.this,Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this,"登记成功，即将返回登录界面",Toast.LENGTH_SHORT).show();
                        Intent intent
                                =new Intent(CompleteInfoActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                }
                case -1: {
                    setSharedPreferences();
                    Toast.makeText(this,"资料已存在，正在跳过此步骤",Toast.LENGTH_SHORT).show();
                    Intent intent
                            =new Intent(CompleteInfoActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                default:{
                    Toast.makeText(this,"登记失败，请稍候再次重试",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setSharedPreferences(){
        String eml=sharedPreferences.getString("eml","");
        String card=sharedPreferences.getString("card","");
        User currentUser=userRepository.getUser(eml);
        Patient currentPatient=patientRepository.getPatient(card);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",currentPatient.getName());
        editor.putString("psw",currentUser.getPsw());
        editor.putString("phone",currentUser.getPhone());
        editor.putInt("pid",currentPatient.getId());
        editor.putString("gender",currentPatient.getGender());
        editor.putInt("age",currentPatient.getAge());
        editor.putBoolean("valid",true);
        editor.commit();
    }

}

