package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKUser;

import org.freelectron.leobel.easytrip.adapters.InspireMeTabPageAdapter;
import org.freelectron.leobel.easytrip.adapters.TabPageAdapter;
import org.freelectron.leobel.easytrip.fragments.InspireMeFragment;
import org.freelectron.leobel.easytrip.models.AuthorizationException;
import org.freelectron.leobel.easytrip.models.InternetConnectionException;
import org.freelectron.leobel.easytrip.models.Realm.BoardRealm;
import org.freelectron.leobel.easytrip.models.Realm.TravelCategoryRealm;
import org.freelectron.leobel.easytrip.models.Response;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.services.PreferenceService;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, InspireMeFragment.OnPinterestListener {

    public static String SELECTED_TAB = "SELECTED_TAB";


    private ViewPager viewPager;
    private InspireMeTabPageAdapter pageAdapter;
    private TabLayout tabLayout;

    @Inject
    public PreferenceService preferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EasyTripApp.getInstance().getComponent().inject(this);

        if(!preferenceService.isDataBaseCreated()){
            if(populateDataBase()){
                preferenceService.setDataBaseCreated(true);
            }
        }

        Fresco.initialize(getApplication());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem toggleLogin = navigationView.getMenu().findItem(R.id.login);
        Intent intent = getIntent();
        int selectedTab = intent.getIntExtra(SELECTED_TAB, 0);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pageAdapter = new InspireMeTabPageAdapter(getSupportFragmentManager(), selectedTab);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        setTabLayoutIcons(pageAdapter);

        PDKClient.getInstance().onConnect(this);
        if(!PDKClient.isAuthenticated()){
            toggleLogin.setTitle(R.string.login);
        }
        else{
            toggleLogin.setTitle(R.string.logout);

        }


    }

    private static boolean populateDataBase() {
        Realm realm = Realm.getDefaultInstance();
        try{
            realm.beginTransaction();

            for (int i = 0; i< InspireMeTabPageAdapter.catagories.length; i++){
                TravelCategoryRealm category = realm.createObject(TravelCategoryRealm.class, i);
                category.setName(InspireMeTabPageAdapter.catagories[i]);
                RealmList<BoardRealm> realmBoards = new RealmList<>();
                for(Map.Entry<String, String> board: InspireMeTabPageAdapter.boards[i].entrySet()){
                    BoardRealm boardRealm = realm.createObject(BoardRealm.class, board.getKey());
                    boardRealm.setName(board.getValue());
                    realmBoards.add(boardRealm);
                }
                category.setBoardRealms(realmBoards);
            }
            realm.commitTransaction();
            return true;
        }
        catch (Exception e){
            realm.cancelTransaction();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inspire_me) {
            viewPager.setCurrentItem(InspireMeTabPageAdapter.LONELY_PLANET_TAB_INDEX);
        } else if (id == R.id.nav_flights) {
            startActivity(new Intent(this, FlightFormActivity.class));
        } else if (id == R.id.nav_hotels) {
//            viewPager.setCurrentItem(pageAdapter.HOTELS_TAB_INDEX);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.login) {
            if(!PDKClient.isAuthenticated()){
                pinterestService.login(this, scopes)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if(response.isSuccessful()){
                                item.setTitle(R.string.logout);
                                Timber.d("User login successful :" + response.getValue().getFirstName());
                            }
                            else{
                                item.setTitle(R.string.login);
                                Timber.d("User login error: " + response.getError());
                            }
                        });
            }
            else{
                PDKClient.getInstance().logout();
                item.setTitle(R.string.login);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTabLayoutIcons(FragmentPagerAdapter pagerAdapter) {
        for (int i = 0; i < pagerAdapter.getCount(); ++i) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            int drawableId = -1;
            String title = "";

            if (i == InspireMeTabPageAdapter.LONELY_PLANET_TAB_INDEX) {
                title = getString(R.string.lonely_planet_tab);
            }
            else if (i == InspireMeTabPageAdapter.ADVENTURE_TAB_INDEX) {
                title = getString(R.string.adventure_tab);
            }
            else if (i == InspireMeTabPageAdapter.ART_CULTURE_TAB_INDEX) {
                title = getString(R.string.art_culture_tab);
            }
            else if (i == InspireMeTabPageAdapter.FESTIVAL_EVENT_TAB_INDEX) {
                title = getString(R.string.festival_event_tab);
            }
            else if (i == InspireMeTabPageAdapter.RELAX_HOLYDAY_TAB_INDEX) {
                title = getString(R.string.relax_tab);
            }
            if (!title.isEmpty()) {
                //tab.setIcon(drawableId);
                tab.setText(title);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
    }

    @Override
    public void onLoadingItemsError(Throwable error) {
        if(error instanceof AuthorizationException){
            requireLoginDialog.show();
        }
        else if(error instanceof ConnectException){
            requireConnectionDialog.show();
        }
        Timber.d("Result error: " + error.getMessage());
    }
}
