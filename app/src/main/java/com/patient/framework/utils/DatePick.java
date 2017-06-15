package com.patient.framework.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.ViewGroup;

import java.util.Date;

public class DatePick extends DatePickerDialog {

    //type=1则代表以后的日期是过期的，2则代表以前的日期是过期的.
    private int type = -1;

    public DatePick(Context context, OnDateSetListener callBack, int year,
                      int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public DatePick(Context context, OnDateSetListener callBack, int year,
                        int monthOfYear, int dayOfMonth, int type) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        this.type = type;
    }

    @Override
    public void onDateChanged(android.widget.DatePicker view, int year, int month, int day) {
        Date time = new Date();		//目前日期
        Date nextTime = new Date();		//将要设置的日期，把值传入其中
        nextTime.setYear(year - 1900);
        nextTime.setMonth(month);
        nextTime.setDate(day);
        if(view != null){
            if(type == 1){
                if ((nextTime.getTime() - time.getTime())>=0) {
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(0).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(0).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(0).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(1).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(1).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(1).setEnabled(false);
                    super.updateDate(time.getYear()+1900, time.getMonth(), (time.getDate()));
                }else{
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(0).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(0).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(0).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(1).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(1).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(1).setEnabled(true);
                    super.onDateChanged(view, year, month, day);
                }
            }else if(type == 2){
                if ((nextTime.getTime() - time.getTime()) <= 0) {
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(2).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(2).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(2).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(1).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(1).setEnabled(false);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(1).setEnabled(false);
                    super.updateDate(time.getYear() + 1900, time.getMonth(),(time.getDate()));	//更新picker
                    super.onDateChanged(view, year, month, day);
                } else {
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(2).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(2).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(2).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(0)).getChildAt(1).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(1)).getChildAt(1).setEnabled(true);
                    ((ViewGroup) ((ViewGroup) view.getChildAt(0)).getChildAt(2)).getChildAt(1).setEnabled(true);
                    super.updateDate(year, month, day);
                    super.onDateChanged(view, year, month, day);	//更新标题
                }
            }
        }
    }
}
