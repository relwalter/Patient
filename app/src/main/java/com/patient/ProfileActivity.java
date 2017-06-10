package com.patient;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.patient.framework.model.Patient;
import com.patient.framework.model.User;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.repository.UserRepository;
import com.patient.framework.service.UserServices;

public class ProfileActivity extends AppCompatActivity {

    private String currentRealName,currentEml,currentCard,currentPhone,currentGender,currentAge;
    private TextView mrealNameView,mEmlEditView,mrealNameEditView,mNameView,mEmlView,mCardView,mPhoneView,mGenderView,mAgeView;
    private EditText mNameEditView,mCardEditView,mPhoneEditView,mGenderEditView,mAgeEditView;
    private FloatingActionButton fab,fab_edit;
    private UserRepository userServices;
    private PatientRepository patientServices;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProfileView();
    }

    private void loadProfileView(){
        setContentView(R.layout.activity_profile);
        mrealNameView=(TextView) findViewById(R.id.realname_profile);
        mEmlView=(TextView) findViewById(R.id.eml_profile);
        mCardView=(TextView) findViewById(R.id.card_profile);
        mPhoneView=(TextView) findViewById(R.id.phone_profile);
        mNameView=(TextView) findViewById(R.id.name_profile);
        mGenderView=(TextView) findViewById(R.id.gender_profile);
        mAgeView=(TextView) findViewById(R.id.age_profile);
        userServices=UserRepository.getInstance(ProfileActivity.this);
        patientServices=new PatientRepository(ProfileActivity.this);
        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        setProfileView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfileEditView();
            }
        });

    }

    private void loadProfileEditView(){
        setContentView(R.layout.activity_profile_edit);
        setProfileEditView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab_edit = (FloatingActionButton) findViewById(R.id.fab_profile_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveProfile()){
                    Toast.makeText(ProfileActivity.this,"资料更新成功",Toast.LENGTH_SHORT)
                            .show();
                    loadProfileView();
                }else{
                    Toast.makeText(ProfileActivity.this,
                            "资料更新失败，请检查后重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setProfileView(){
        currentCard=sharedPreferences.getString("card","");
        currentEml=sharedPreferences.getString("eml","");
        String[] userInfo=userServices.getUser(currentEml).getInfo();
        String[] patientInfo=patientServices.getPatient(currentCard).getInfo();
        currentRealName=patientInfo[1];
        currentGender=patientInfo[2];
        currentAge=patientInfo[3];
        currentPhone=userInfo[4];
        mrealNameView.setText(currentRealName);
        mEmlView.setText(currentEml);
        mCardView.setText(currentCard);
        mPhoneView.setText(currentPhone);
        mNameView.setText(currentRealName);
        mGenderView.setText(currentGender);
        mAgeView.setText(currentAge);
    }

    private void setProfileEditView(){
        mrealNameEditView=(TextView) findViewById(R.id.realname_profile_edit);
        mEmlEditView=(TextView) findViewById(R.id.eml_profile_edit);
        mCardEditView=(EditText) findViewById(R.id.card_profile_edit);
        mPhoneEditView=(EditText) findViewById(R.id.phone_profile_edit);
        mNameEditView=(EditText) findViewById(R.id.name_profile_edit);
        mGenderEditView=(EditText) findViewById(R.id.gender_profile_edit);
        mAgeEditView=(EditText) findViewById(R.id.age_profile_edit);
        currentCard=sharedPreferences.getString("card","");
        currentEml=sharedPreferences.getString("eml","");
        String[] userInfo=userServices.getUser(currentEml).getInfo();
        String[] patientInfo=patientServices.getPatient(currentCard).getInfo();
        currentRealName=patientInfo[1];
        currentGender=patientInfo[2];
        currentAge=patientInfo[3];
        currentPhone=userInfo[4];
        mrealNameEditView.setText(currentRealName);
        mEmlEditView.setText(currentEml);
        mCardEditView.setText(currentCard);
        mPhoneEditView.setText(currentPhone);
        mNameEditView.setText(currentRealName);
        mGenderEditView.setText(currentGender);
        mAgeEditView.setText(currentAge);
    }

    private boolean saveProfile(){
        String[] userInfo=userServices.getUser(currentEml).getInfo();
        String currentPsw=userInfo[3];
        String oldCard=currentCard;
        currentRealName=mNameEditView.getText().toString();
        currentEml=mEmlEditView.getText().toString();
        currentCard=mCardEditView.getText().toString();
        currentPhone=mPhoneEditView.getText().toString();
        currentGender=mGenderEditView.getText().toString();
        currentAge=mAgeEditView.getText().toString();
        if(attemptSave()){
            User updatedUser=new User(currentCard,currentEml,currentPsw,currentPhone);
            Patient updatedPatient=new Patient(currentRealName,currentGender,Integer.parseInt(currentAge),currentCard);
            if(userServices.updateUserInfo(updatedUser)){
                if(patientServices.updatePatientInfo(updatedPatient,oldCard)){
                    setSharedPreferences();
                    return true;
                }
            }
        }
        return false;
    }

    private void setSharedPreferences(){
        String currentPsw=userServices.getUser(currentEml).getPsw();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",currentRealName);
        editor.putString("card",currentCard);
        editor.putString("eml",currentEml);
        editor.putString("psw",currentPsw);
        editor.putString("phone",currentPhone);
        editor.putString("gender",currentGender);
        editor.putString("age",currentAge);
        editor.putBoolean("valid",true);
        editor.commit();
    }

    private boolean attemptSave(){
        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(currentCard) && !isCardValid(currentCard)) {
            mCardEditView.setError("卡号长度有误");
            focusView = mCardEditView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(currentPhone) && !isPhoneValid(currentPhone)) {
            mPhoneEditView.setError("手机号有误");
            focusView = mPhoneEditView;
            cancel = true;
        }

        if (!(currentRealName.length()>=2 && currentRealName.length()<=12)) {
            mrealNameEditView.setError("姓名长度有误");
            focusView = mrealNameEditView;
            cancel = true;
        }


        if (TextUtils.isEmpty(currentEml)) {
            mEmlEditView.setError(getString(R.string.error_field_required));
            focusView = mEmlEditView;
            cancel = true;
        }else if (!isEmailValid(currentEml)) {
            mEmlEditView.setError(getString(R.string.error_invalid_email));
            focusView = mEmlEditView;
            cancel = true;
        }

        if (!("男".equals(currentGender)||"女".equals(currentGender)||"Male".equals(currentGender)||"Female".equals(currentGender))) {
            mGenderEditView.setError("格式不正确");
            focusView = mGenderEditView;
            cancel = true;
        }

        if (!(Integer.parseInt(currentAge)>=0 && Integer.parseInt(currentAge)<150)){
            mAgeEditView.setError("年龄有误");
            focusView = mAgeEditView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }else {
            return true;
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

}
