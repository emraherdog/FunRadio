package com.ufxmeng.je.funradio;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ufxmeng.je.funradio.adapter.ViewPagerMainAdapter;

public class MainActivity extends AppCompatActivity {


    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);

        mViewPager.setAdapter(new ViewPagerMainAdapter(getSupportFragmentManager()));

    }


    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 0) {

            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}
