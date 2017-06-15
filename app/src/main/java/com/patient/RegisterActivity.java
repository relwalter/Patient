package com.patient;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.patient.framework.utils.ImgUtil;
import com.patient.framework.view.CouponView;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private CouponView cmView,surView,inView,ynView,eyeView;
    private ImageView cmImageView,surImageView,inImageView,ynImageView,eyeImageView;
    private Toolbar mToolbar;
    private NestedScrollView mScrollView;
    private TextView mTextView;
    private SharedPreferences sharedPreferences;
    private int nowYear,nowMonth,nowDay,nowHour,nowMinute,nowSecond;
    private boolean reg=false;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
        setupListeners();
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

    private void loadView(){
        setContentView(R.layout.activity_register);
        sharedPreferences=getSharedPreferences("current",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        mTextView=(TextView) findViewById(R.id.reach_to_bottom);
        cmImageView=(ImageView) findViewById(R.id.img_chinese_med);
        surImageView=(ImageView) findViewById(R.id.surgery);
        inImageView=(ImageView) findViewById(R.id.img_inner);
        ynImageView=(ImageView) findViewById(R.id.img_young);
        eyeImageView=(ImageView) findViewById(R.id.img_eye);
        cmImageView.setImageBitmap(ImgUtil.readBitmap(this,R.drawable.chinese_med));
        surImageView.setImageBitmap(ImgUtil.readBitmap(this,R.drawable.surgery));
        inImageView.setImageBitmap(ImgUtil.readBitmap(this,R.drawable.inner));
        ynImageView.setImageBitmap(ImgUtil.readBitmap(this,R.drawable.young));
        eyeImageView.setImageBitmap(ImgUtil.readBitmap(this,R.drawable.eye));
        Calendar c = Calendar.getInstance();
        nowYear = c.get(Calendar.YEAR);
        nowMonth = c.get(Calendar.MONTH)+1;
        nowDay = c.get(Calendar.DAY_OF_MONTH);
        nowHour = c.get(Calendar.HOUR_OF_DAY);
        nowMinute = c.get(Calendar.MINUTE);
        nowSecond = c.get(Calendar.SECOND);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupListeners(){
        if(sharedPreferences.getBoolean("reg",false)){
            new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")
                    .setMessage("当前您已预约成功"+ sharedPreferences.getString("dep","")+
                            "，请到诊疗卡页面查看详细情况")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("好", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(new Intent(RegisterActivity.this,
                                    MainActivity.class));
                        }
                    }).show();
        }else{
            cmView=(CouponView) findViewById(R.id.reg_chinese_med);
            inView=(CouponView) findViewById(R.id.reg_inner);
            surView=(CouponView) findViewById(R.id.reg_surgery);
            ynView=(CouponView) findViewById(R.id.reg_young);
            eyeView=(CouponView) findViewById(R.id.reg_eye);
            mScrollView=(NestedScrollView) findViewById(R.id.reg_scrolling);
            bind(cmView);
            bind(inView);
            bind(surView);
            bind(ynView);
            bind(eyeView);
            mScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE: {
                            /**
                             * 1.getScrollY()获取滑动的距离
                             * 2.getHeight()获取在屏幕上显示的高度
                             * 3.getMeasureHeight()获取控件的真实高度(包含屏幕外的高度)
                             */
                            if (mScrollView.getScrollY() <= 0) {
                                // 说明没有滑动，在屏幕顶部
                            }
                            // 获取ScrollView中包裹的View的高度
                            int measuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
                            int scrollY = mScrollView.getScrollY();
                            int height = mScrollView.getHeight();
                            if (measuredHeight <= scrollY + height + 15) {
                                // 已经滑动的距离+在屏幕上显示的高度>=控件真实高度。说明已经滑动到底部
                                Toast.makeText(RegisterActivity.this,
                                        ">.< 已经到底了...",Toast.LENGTH_SHORT).show();
                                mTextView.setText("  ®2017 健康不用等");
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private void bind(View view){
        final String tag=view.getTag().toString();
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth) {
                        if(year<nowYear || (year==nowYear && monthOfYear+1<nowMonth)
                                || (year==nowYear && monthOfYear+1==nowMonth && dayOfMonth<nowDay)){
                            Toast.makeText(RegisterActivity.this,
                                    "请不要选择今天之前的日期",Toast.LENGTH_SHORT).show();
                            if(!reg){
                                DatePickerDialog dialog=new DatePickerDialog(RegisterActivity.this,
                                        R.style.DiaglogTheme,this,nowYear,
                                        nowMonth-1,nowDay);
                                dialog.show();
                            }
                        }else if(year>nowYear || (year==nowYear && monthOfYear+1>nowMonth)
                                || (year==nowYear && monthOfYear+1==nowMonth && dayOfMonth>nowDay+7)){
                            Toast.makeText(RegisterActivity.this,
                                    "为保证服务效率，请选择一周内的日期",Toast.LENGTH_SHORT).show();
                            if(!reg){
                                DatePickerDialog dialog=new DatePickerDialog(RegisterActivity.this,
                                        R.style.DiaglogTheme,this,nowYear,
                                        nowMonth-1,nowDay);
                                dialog.show();
                            }
                        }else{

                            //TODO: 具体到时段实现高效率预约.

                            reg=true;
                            editor.putString("dep",tag);
                            editor.putString("regTime",year+"-"+(monthOfYear+1)+"-"+ dayOfMonth+
                                    " "+nowHour+":"+nowMinute+":"+ nowSecond);
                            editor.putBoolean("reg",reg);
                            editor.putBoolean("queue",false);
                            editor.commit();
                            Toast.makeText(RegisterActivity.this,
                                    "预约成功！请提前到科室护士站报道",Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Toast.makeText(RegisterActivity.this,
                        "请选择日期",Toast.LENGTH_SHORT).show();
                DatePickerDialog dialog=new DatePickerDialog(RegisterActivity.this,
                        R.style.DiaglogTheme,listener,nowYear,nowMonth-1,nowDay);
//                DatePick dialog=new DatePick(RegisterActivity.this,listener,nowYear,
//                        nowMonth-1,nowDay,2);
                dialog.show();
            }
        });
    }

}
