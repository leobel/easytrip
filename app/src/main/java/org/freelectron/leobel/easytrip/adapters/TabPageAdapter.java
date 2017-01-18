package org.freelectron.leobel.easytrip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.freelectron.leobel.easytrip.fragments.InspireMeFragment;
import org.freelectron.leobel.easytrip.fragments.FlightFragment;
import org.freelectron.leobel.easytrip.fragments.HotelFragment;

/**
 * Created by leobel on 1/18/17.
 */
public class TabPageAdapter extends FragmentPagerAdapter{

    private static final int NUMBER_OF_TABS = 3;
    public static final int INSPIRE_ME_TAB_INDEX = 0;
    public static final int FLIGHTS_TAB_INDEX = 1;
    public static final int HOTELS_TAB_INDEX = 2;

    private final int selectedTab;
    private Fragment[] fragments;

    public TabPageAdapter(FragmentManager supportFragmentManager, int selectedTab) {
        super(supportFragmentManager);
        this.selectedTab = selectedTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case INSPIRE_ME_TAB_INDEX:
                return new InspireMeFragment();
            case FLIGHTS_TAB_INDEX:
                return new FlightFragment();
            case HOTELS_TAB_INDEX:
                return new HotelFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }
}
