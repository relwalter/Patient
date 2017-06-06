package com.patient;

import android.content.*;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.repository.UserRepository;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private long exitTime=0L;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private NavigationView navigationView;
    private TextView mNameTextView,mEmlTextView;
    private PatientRepository patientRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        patientRepository=new PatientRepository(Main2Activity.this);
        String eml=getSharedPreferences("current",MODE_PRIVATE).getString("eml","");
        String card=getSharedPreferences("current",MODE_PRIVATE).getString("card","");
        String name=patientRepository.getPatient(card).getName();

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        mNameTextView=(TextView)headerView.findViewById(R.id.name_drawer);
        mEmlTextView=(TextView)headerView.findViewById(R.id.eml_drawer);

        mNameTextView.setText(name);
        mEmlTextView.setText(eml);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager=(ViewPager) findViewById(R.id.viewPager);
        mCardAdapter=new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_2));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_3));

        mCardShadowTransformer=new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Main2Activity.this,SettingsActivity.class));
            return true;
        }else if(id == R.id.action_quit) {
            new AlertDialog.Builder(Main2Activity.this).setTitle("提示")
                    .setMessage("真的要退出当前用户吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor
                                    =getSharedPreferences("current",MODE_PRIVATE).edit();
                            editor.putBoolean("valid",false);
                            editor.commit();
                            startActivity(new Intent(Main2Activity.this,LoginActivity.class));
                            finish();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_index) {

        } else if (id == R.id.nav_act1) {

        } else if (id == R.id.nav_act2) {

        } else if (id == R.id.nav_act3) {

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            Uri imgUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/download/share.jpg"));
            intent.putExtra("Kdescription", "我正在试用健康不用等app，在线就能轻松挂号，看病快人一步。自从用上它才知道什么叫做相见恨晚，你也来试试吧！");
            intent.putExtra(Intent.EXTRA_STREAM, imgUri);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Toast.makeText(Main2Activity.this,"非常感谢您的反馈！",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Main2Activity.this,AboutUsActivity.class));
        } else if (id == R.id.nav_quit) {
            new AlertDialog.Builder(Main2Activity.this).setTitle("提示")
                    .setMessage("真的要退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor
                                    =getSharedPreferences("current",MODE_PRIVATE).edit();
                            editor.putBoolean("valid",false);
                            editor.commit();
                            finish();
                            startActivity(new Intent
                                    (Main2Activity.this,LoginActivity.class));
                        }
                    }).setNegativeButton("返回", null).show();
        } else if (id == R.id.nav_setting){
            startActivity(new Intent(Main2Activity.this,SettingsActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
