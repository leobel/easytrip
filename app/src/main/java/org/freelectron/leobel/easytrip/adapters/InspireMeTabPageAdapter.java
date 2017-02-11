package org.freelectron.leobel.easytrip.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.freelectron.leobel.easytrip.fragments.FlightFragment;
import org.freelectron.leobel.easytrip.fragments.HotelFragment;
import org.freelectron.leobel.easytrip.fragments.InspireMeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leobel on 1/19/17.
 */

public class InspireMeTabPageAdapter extends FragmentPagerAdapter {
    public static final int NUMBER_OF_TABS = 5;
    public static final int ADVENTURE_TAB_INDEX = 0;
    public static final int ART_CULTURE_TAB_INDEX = 1;
    public static final int FESTIVAL_EVENT_TAB_INDEX = 2;
    public static final int LONELY_PLANET_TAB_INDEX = 3;
    public static final int RELAX_HOLYDAY_TAB_INDEX = 4;


    public static String[] catagories = new String[]{"LonelyPlanet", "Adventure", "ArtCulture", "FestivalEvents", "RelaxHolyday"};
    public static HashMap<String, String>[] boards = new HashMap[NUMBER_OF_TABS];

    static {
        HashMap<String, String> lonelyBoards = new HashMap<>();
        lonelyBoards.put("leobelizquierdo/california-trip", "California Trip");
        lonelyBoards.put("leobelizquierdo/places-to-ski", "Skiing");
        lonelyBoards.put("leobelizquierdo/surfing", "Surfing");
        boards[0] = lonelyBoards;

        HashMap<String, String> adventureBoards = new HashMap<>();
        adventureBoards.put("leobelizquierdo/full-frame", "Full Frame");
        boards[1] = adventureBoards;

        HashMap<String, String> artCultureBoards = new HashMap<>();
        artCultureBoards.put("leobelizquierdo/level-6", "Level 6");
        boards[2] = artCultureBoards;

        HashMap<String, String> festivalEventsBoards = new HashMap<>();
        festivalEventsBoards.put("leobelizquierdo/wonderful-places", "Wonderful Places");
        boards[3] = festivalEventsBoards;

        HashMap<String, String> relaxBoards = new HashMap<>();
        relaxBoards.put("leobelizquierdo/test2", "Test 2");
        boards[4] = relaxBoards;
    };


    private final int selectedTab;
    private Fragment[] fragments;

    public InspireMeTabPageAdapter(FragmentManager supportFragmentManager, int selectedTab) {
        super(supportFragmentManager);
        this.selectedTab = selectedTab;
        fragments = new Fragment[NUMBER_OF_TABS];
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] == null) fragments[position] = InspireMeFragment.newInstance(position);
        return fragments[position];
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }
}
