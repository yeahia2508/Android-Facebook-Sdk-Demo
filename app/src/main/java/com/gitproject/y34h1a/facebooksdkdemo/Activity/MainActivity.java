package com.gitproject.y34h1a.facebooksdkdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.gitproject.y34h1a.facebooksdkdemo.Fragment.LikeFragment;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.PostComment;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.SendFeedFragment;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.SendPhoto;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.SendVideo;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.UserInfo;
import com.gitproject.y34h1a.facebooksdkdemo.Fragment.UserPosts;
import com.gitproject.y34h1a.facebooksdkdemo.Helper.Utils;
import com.gitproject.y34h1a.facebooksdkdemo.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    protected static final String TAG = MainActivity.class.getName();
    private SimpleFacebook mSimpleFacebook;
    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    AppBarLayout appbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSimpleFacebook = SimpleFacebook.getInstance(this);
        Utils.updateLanguage(getApplicationContext(), "en");
        Utils.printHashKey(getApplicationContext());

        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        initView();
        if(!mSimpleFacebook.isLogin()){
            setLogin();
        }
    }

    private void initView(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsAdapter(context, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        appbarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appbarLayout.addOnOffsetChangedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    private void setLogin() {
        // Login listener
        final OnLoginListener onLoginListener = new OnLoginListener() {

            @Override
            public void onFail(String reason) {
                Log.w(TAG, "Failed to login");
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                // change the state of the button or do whatever you want

            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Canceled the login");
            }

        };
        mSimpleFacebook.login(onLoginListener);
    }

    /**
     * Logout example
     */
    private void setLogout() {
        final OnLogoutListener onLogoutListener = new OnLogoutListener() {
            @Override
            public void onLogout() {
                // change the state of the button or do whatever you want

            }
        };

        mSimpleFacebook.logout(onLogoutListener);
    }


    class TabsAdapter extends FragmentPagerAdapter {

        Context context;

        public TabsAdapter(Context con , FragmentManager fm) {
            super(fm);
            this.context = con;
        }


        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return SendFeedFragment.newInstance("", "");
                case 1:
                    //add your fragment
                    return SendPhoto.newInstance("", "");
                case 2:
                    //add your fragment
                    return SendVideo.newInstance("", "");
                case 3:
                    return PostComment.newInstance("", "");
                case 4:
                    //add your fragment
                    return LikeFragment.newInstance("", "");
                case 5:
                    //add your fragment
                    return UserPosts.newInstance("", "");
                case 6:
                    return UserInfo.newInstance("", "");
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Feed";
                case 1:
                    return "Photo";
                case 2:
                    return "Video";
                case 3:
                    return "Comment";
                case 4:
                    return "Like";
                case 5:
                    return "Get Posts";
                case 6:
                    return "User Info";
            }
            return "";
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }
    private void sendMail(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "yeahia.arif@gmail.com");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

}
