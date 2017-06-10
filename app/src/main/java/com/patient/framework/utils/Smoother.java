package com.patient.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class Smoother {

    public static void startActivity(final Context context, final Class<?> option, long delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(new Intent(context,option));
            }
        },delay);
    }

    public static void startActivity(final Context context, final Class<?> option){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(new Intent(context,option));
            }
        },300);
    }
}
