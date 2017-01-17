package org.freelectron.leobel.easytrip;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    public void addFragment(@NonNull Fragment fragment,@Nullable String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.root_container, fragment, tag);
        transaction.commit();
    }

    /**
     * Add an existing fragment to the current navigation fragment's container.
     *
     * @param fragment The new fragment to add in the container.
     * @param tag Optional tag name for the fragment, to later retrieve the
     * fragment with {@link FragmentManager#findFragmentByTag(String)
     * FragmentManager.findFragmentByTag(String)}.
     */
    public void pushFragment(@NonNull Fragment fragment, @Nullable String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.root_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();

//        if (getActivity() instanceof HomeActivity) {
//            Toolbar toolbar = ((HomeActivity) getActivity()).getToolbar();
//            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
//        }
    }
}
