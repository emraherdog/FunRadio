package com.ufxmeng.je.funradio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ufxmeng.je.funradio.fragment.PodcastFragment;
import com.ufxmeng.je.funradio.fragment.RadioFragment;

/**
 * Created by JE on 6/4/2016.
 */
public class ViewPagerMainAdapter extends FragmentStatePagerAdapter {

    private static final int PAGE_COUNT = 2;

    public ViewPagerMainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RadioFragment();
                break;
            case 1:
                fragment = new PodcastFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
