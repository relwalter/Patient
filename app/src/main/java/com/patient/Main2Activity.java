package com.patient;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.*;
import com.patient.framework.repository.PatientRepository;
import com.patient.framework.utils.SaveDrawable;
import com.patient.framework.utils.Smoother;

import java.io.File;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private long exitTime=0L;
    private DrawerLayout drawer;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private NavigationView navigationView;
    private TextView mNameTextView,mEmlTextView;
    private ImageView mImageView;
    private PatientRepository patientRepository;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context=Main2Activity.this;
        patientRepository=new PatientRepository(Main2Activity.this);
        String eml=getSharedPreferences("current",MODE_PRIVATE).getString("eml","");
        String card=getSharedPreferences("current",MODE_PRIVATE).getString("card","");
        int imageSource=getSharedPreferences("current",MODE_PRIVATE).getInt("avatar",R.drawable.user_young);
        String name=patientRepository.getPatient(card).getName();
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        mImageView=(ImageView)headerView.findViewById(R.id.imageView);
        mImageView.setImageResource(imageSource);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,ProfileActivity.class));
            }
        });
        mNameTextView=(TextView)headerView.findViewById(R.id.name_drawer);
        mEmlTextView=(TextView)headerView.findViewById(R.id.eml_drawer);

        mNameTextView.setText(name);
        mEmlTextView.setText(eml);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager=(ViewPager) findViewById(R.id.viewPager);
        mCardAdapter=new CardPagerAdapter();
        mCardAdapter.addCardItem(
                new CardItem(R.string.title_1, R.string.text_1,"card_btn_reg",R.id.card_view_1));
        mCardAdapter.addCardItem(
                new CardItem(R.string.title_2, R.string.text_2,"card_btn_pro",R.id.card_view_2));
        mCardAdapter.addCardItem(
                new CardItem(R.string.title_3, R.string.text_3,"card_btn_sign",R.id.card_view_3));

        mCardShadowTransformer=new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);


        drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onClick(View view) {
        String clicked=view.getTag().toString();
        Log.d("clicked",clicked);
        if("card_btn_1".equals(clicked)){

        }else if("card_btn_2".equals(clicked)){
            startActivity(new Intent(Main2Activity.this,ProfileActivity.class));
        }else if("card_btn_3".equals(clicked)){

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onBackPressed() {
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
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(Main2Activity.this,SettingsActivity.class));
            return true;
        }else if(id == R.id.action_quit) {
            new AlertDialog.Builder(Main2Activity.this).setTitle("提示")
                    .setMessage("真的要退出当前用户吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            invalidate();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
//        if (id == R.id.nav_index) {
//
//        } else
        if (id == R.id.nav_act){
            Smoother.startActivity(Main2Activity.this,MainActivity.class);
        } else if (id == R.id.nav_act1) {
            Smoother.startActivity(Main2Activity.this,RegisterActivity.class);
        } else if (id == R.id.nav_act2) {
            Smoother.startActivity(Main2Activity.this,ProfileActivity.class);
        } else if (id == R.id.nav_act3) {
            Smoother.startActivity(Main2Activity.this,PatinetSignActivity.class);
        } else if (id == R.id.nav_share) {
            attemptShare();
        } else if (id == R.id.nav_send) {
            Smoother.startActivity(Main2Activity.this,ScrollingActivity.class);
        } else if (id == R.id.nav_quit) {
            new AlertDialog.Builder(Main2Activity.this).setTitle("提示")
                    .setMessage("真的要退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            invalidate();
                            finish();
                            Smoother.startActivity(Main2Activity.this,LoginActivity.class);
                        }
                    }).setNegativeButton("返回", null).show();
        } else if (id == R.id.nav_setting){
            Smoother.startActivity(Main2Activity.this,SettingsActivity.class);
        }
        return true;
    }

    private void attemptShare(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        SaveDrawable.saveDrawableById(getResources(),R.drawable.share,
                Environment.getExternalStorageDirectory()+"/download/share.jpg");
        Uri imgUri;
//        Resources res = this.getResources();
//        imgUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                + res.getResourcePackageName(R.drawable.share) + "/"
//                + res.getResourceTypeName(R.drawable.share) + "/"
//                + res.getResourceEntryName(R.drawable.share));
        imgUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/download/share.jpg"));
        intent.putExtra("Kdescription", "Hey，我正在试用健康不用等app。在线就能轻松挂号，看病快人一步！自从用上它才知道什么叫做相见恨晚，你也来试试吧！");
        intent.putExtra(Intent.EXTRA_STREAM, imgUri);
        startActivity(intent);
    }

    public static void conduct(String keyword){
        if(keyword.equals("reg")){
            context.startActivity(new Intent(context,RegisterActivity.class));
        }else if(keyword.equals("pro")){
            context.startActivity(new Intent(context,ProfileActivity.class));
        }else if(keyword.equals("sign")){
            context.startActivity(new Intent(context,PatinetSignActivity.class));
        }
    }

    private void invalidate(){
        SharedPreferences.Editor editor
                =getSharedPreferences("current",MODE_PRIVATE).edit();
        editor.putBoolean("valid",false);
        editor.putBoolean("reg",false);
        editor.putBoolean("queue",false);
        editor.putInt("qtime",-1);
        editor.putInt("before",0);
        editor.putString("name","");
        editor.putString("card","");
        editor.putString("eml","");
        editor.putString("psw","");
        editor.putString("pid","");
        editor.commit();
    }

}
