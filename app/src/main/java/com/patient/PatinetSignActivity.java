package com.patient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.patient.framework.model.Patient;
import com.patient.framework.model.PatientSign;
import com.patient.framework.model.User;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.repository.PatientSignRepository;
import com.patient.framework.repository.UserRepository;

import java.util.regex.Pattern;

public class PatinetSignActivity extends AppCompatActivity {

    int pid;
    private String currentRealName,currentHeight,currentWeight,currentTemp,currentBreath,currentPulse,
            currentPressure,currentBlsugar,currentMore;
    private TextView mrealNameView,mNameEditView,mHeightView,mWeightView,mTempView,mBreathView,mPulseView,
            mPressureView,mBlsugarView,mMoreView;
    private ImageView mImageView;
    private EditText mHeightEditView,mWeightEditView,mTempEditView,mBreathEditView,mPulseEditView,
            mPressureEditView,mBlsugarEditView,mMoreEditView;
    private FloatingActionButton fab,fab_edit;
    private PatientSignRepository patientSignRepository;
    private PatientSign patientSign;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        pid=sharedPreferences.getInt("pid",0);
        patientSignRepository=new PatientSignRepository(PatinetSignActivity.this);
        loadPatientSignView();
    }

    private void loadPatientSignView(){
        setContentView(R.layout.activity_patient_sign);
        mrealNameView=(TextView) findViewById(R.id.realname_sign);
        mHeightView=(TextView) findViewById(R.id.height_sign);
        mWeightView=(TextView) findViewById(R.id.weight_sign);
        mTempView=(TextView) findViewById(R.id.temp_sign);
        mBreathView=(TextView) findViewById(R.id.breath_sign);
        mPulseView=(TextView) findViewById(R.id.pulse_sign);
        mPressureView=(TextView) findViewById(R.id.pressure_sign);
        mBlsugarView=(TextView) findViewById(R.id.blsugar_sign);
        mMoreView=(TextView) findViewById(R.id.more_sign);
        setPatientSignView();
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar_sign);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPatientSignEditView();
            }
        });

    }

    private void loadPatientSignEditView(){
        setContentView(R.layout.activity_patient_sign_edit);
        setPatientSignEditView();
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar_sign_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab_edit = (FloatingActionButton) findViewById(R.id.fab_sign_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savePatientSign()){
                    Toast.makeText(PatinetSignActivity.this,"资料更新成功",Toast.LENGTH_SHORT)
                            .show();
                    loadPatientSignView();
                }else{
                    Toast.makeText(PatinetSignActivity.this,
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

    private void setPatientSignView(){
        autowireFromDB();

        mrealNameView.setText(currentRealName);
        mHeightView.setText(currentHeight);
        mWeightView.setText(currentWeight);
        mTempView.setText(currentTemp);
        mBreathView.setText(currentBreath);
        mPulseView.setText(currentPulse);
        mPressureView.setText(currentPressure);
        mBlsugarView.setText(currentBlsugar);
        mMoreView.setText(currentMore);
    }

    private void setPatientSignEditView(){
        autowireFromDB();

        mNameEditView=(TextView) findViewById(R.id.realname_sign_edit);
        mHeightEditView=(EditText) findViewById(R.id.height_sign_edit);
        mWeightEditView=(EditText) findViewById(R.id.weight_sign_edit);
        mTempEditView=(EditText) findViewById(R.id.temp_sign_edit);
        mBreathEditView=(EditText) findViewById(R.id.breath_sign_edit);
        mPulseEditView=(EditText) findViewById(R.id.pulse_sign_edit);
        mPressureEditView=(EditText) findViewById(R.id.pressure_sign_edit);
        mBlsugarEditView=(EditText) findViewById(R.id.blsugar_sign_edit);
        mMoreEditView=(EditText) findViewById(R.id.more_sign_edit);

        mNameEditView.setText(currentRealName);
        mHeightEditView.setText(currentHeight);
        mWeightEditView.setText(currentWeight);
        mTempEditView.setText(currentTemp);
        mBreathEditView.setText(currentBreath);
        mPulseEditView.setText(currentPulse);
        mPressureEditView.setText(currentPressure);
        mBlsugarEditView.setText(currentBlsugar);
        mMoreEditView.setText(currentMore);
    }

    private void autowireFromDB(){
        patientSign=patientSignRepository.getPatientSign(pid);
        String[] patientSignInfo;
        if(patientSign==null){
            patientSignInfo=new String[]{"无","无","无","无","无","无","无","无","无","无"};
            Toast.makeText(PatinetSignActivity.this,"您还没有录入任何体征信息",Toast.LENGTH_SHORT)
                    .show();
        }else{
            patientSignInfo=patientSign.getInfo();
        }
        currentRealName=sharedPreferences.getString("name","");
        currentHeight=patientSignInfo[2];
        currentWeight=patientSignInfo[3];
        currentTemp=patientSignInfo[4];
        currentBreath=patientSignInfo[5];
        currentPulse=patientSignInfo[6];
        currentPressure=patientSignInfo[7];
        currentBlsugar=patientSignInfo[8];
        currentMore=patientSignInfo[9];
    }

    private void autowireFromText(){
        currentHeight=mHeightEditView.getText().toString();
        currentWeight=mWeightEditView.getText().toString();
        currentTemp=mTempEditView.getText().toString();
        currentBreath=mBreathEditView.getText().toString();
        currentPulse=mPulseEditView.getText().toString();
        currentPressure=mPressureEditView.getText().toString();
        currentBlsugar=mBlsugarEditView.getText().toString();
        currentMore=mMoreEditView.getText().toString();
    }

    private boolean savePatientSign() {
        autowireFromText();
        if(attemptSave()){
            patientSign=new PatientSign(pid,Float.parseFloat(currentHeight),Float.parseFloat(currentWeight),
                    Float.parseFloat(currentTemp),Float.parseFloat(currentBreath),
                    Float.parseFloat(currentPulse), currentPressure,Float.parseFloat(currentBlsugar),
                    currentMore);
            if (!patientSignRepository.checkPatientSign(pid)) {
                Log.d("pid",Integer.toString(pid));
                if (patientSignRepository.addPatientSignSign(patientSign)==1) {
                    return true;
                }
            } else if (patientSignRepository.updatePatientSignSign(patientSign)) {
                return true;
            }
        }
        return false;
    }

    private boolean attemptSave(){
        boolean cancel = false;
        View focusView = null;
        Pattern pattern = Pattern.compile("^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$");


        if (TextUtils.isEmpty(currentHeight)||!pattern.matcher(currentHeight).matches()) {
            mHeightEditView.setError("身高格式不正确");
            focusView = mHeightEditView;
            cancel = true;
        }

        if (TextUtils.isEmpty(currentWeight)||!pattern.matcher(currentWeight).matches()) {
            mWeightEditView.setError("体重格式不正确");
            focusView = mWeightEditView;
            cancel = true;
        }

        if(TextUtils.isEmpty(currentTemp)||!pattern.matcher(currentTemp).matches()){
            mTempEditView.setError("体温格式不正确");
            focusView = mTempEditView;
            cancel = true;
        }else if (!(Float.parseFloat(currentTemp)>=34 && Float.parseFloat(currentTemp)<=42)){
            mTempEditView.setError("温度输入有误");
            focusView = mTempEditView;
            cancel = true;
        }

        if(TextUtils.isEmpty(currentBreath)||!pattern.matcher(currentBreath).matches()){
            mBreathEditView.setError("格式不正确");
            focusView = mBreathEditView;
            cancel = true;
        }else if (!(Float.parseFloat(currentBreath)>=10 && Float.parseFloat(currentBreath)<=150)){
            mBreathEditView.setError("呼吸频率有误");
            focusView = mBreathEditView;
            cancel = true;
        }

        if(TextUtils.isEmpty(currentPulse)||!pattern.matcher(currentPulse).matches()){
            mPulseEditView.setError("脉搏格式不正确");
            focusView = mPulseEditView;
            cancel = true;
        }else if (!(Float.parseFloat(currentPulse)>=10 && Float.parseFloat(currentPulse)<=150)){
            mPulseEditView.setError("脉搏输入有误");
            focusView = mPulseEditView;
            cancel = true;
        }

        if(TextUtils.isEmpty(currentBlsugar)||!pattern.matcher(currentBlsugar).matches()){
            mBlsugarEditView.setError("血糖浓度格式不正确");
            focusView = mBlsugarEditView;
            cancel = true;
        }else if (!(Float.parseFloat(currentBlsugar)>=2 && Float.parseFloat(currentBlsugar)<=15)){
            mBlsugarEditView.setError("血糖浓度有误");
            focusView = mBlsugarEditView;
            cancel = true;
        }

        if (!currentPressure.contains("/")) {
            mPressureEditView.setError("血压格式有误");
            focusView = mPressureEditView;
            cancel = true;
        }else {
            if(TextUtils.isEmpty(currentPressure.split("/")[0])
                    ||TextUtils.isEmpty(currentPressure.split("/")[1])){
                mPressureEditView.setError("血压输入有误");
                focusView = mPressureEditView;
                cancel = true;
            }else {
                if(!pattern.matcher(currentPressure.split("/")[0]).matches()
                        ||!pattern.matcher(currentPressure.split("/")[1]).matches()) {
                    mPressureEditView.setError("血压格式有误");
                    focusView = mPressureEditView;
                    cancel = true;
                }else {
                    float relPress=Float.parseFloat(currentPressure.split("/")[0]);
                    float shrPress=Float.parseFloat(currentPressure.split("/")[1]);
                    if(!(relPress>=10 && relPress<=200)||!(shrPress>=10 && shrPress<=200)){
                        mPressureEditView.setError("血压输入有误");
                        focusView = mPressureEditView;
                        cancel = true;
                    }
                }
            }
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }else {
            return true;
        }
    }


}
