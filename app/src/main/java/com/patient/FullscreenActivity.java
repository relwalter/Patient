package com.patient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.w3c.dom.Text;

public class FullscreenActivity extends AppCompatActivity {

    private View mContentView;
    private TextView mTextView;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        hide();
        mContentView = findViewById(R.id.fullscreen_content);
        mTextView = (TextView) findViewById(R.id.fullscreen_content_text);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullscreenActivity.this,LoginActivity.class));
                finish();
            }
        });
        endTime=System.currentTimeMillis()+3000;
        do{
            mTextView.setText(Long.toString(Math.round(endTime/1000)));
        }while(System.currentTimeMillis()<=endTime);

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

}
