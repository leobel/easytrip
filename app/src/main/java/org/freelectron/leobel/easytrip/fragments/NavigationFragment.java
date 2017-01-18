package org.freelectron.leobel.easytrip.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.freelectron.leobel.easytrip.R;

public class NavigationFragment extends Fragment {

    private boolean isViewCreated;
    Fragment fragment;
    String tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        isViewCreated = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(fragment != null){
            addFragment(fragment, tag);
            fragment = null;
            tag = null;
        }
    }

    public void addFragment(@NonNull Fragment fragment, @Nullable String tag) {
        if(!isViewCreated){
            this.fragment = fragment;
            this.tag = tag;
        }
        else{
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.root_container, fragment, tag);
            transaction.commit();
        }

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
        FragmentManager fragmentManager = getChildFragmentManager();
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

    @Override
    public void onDestroyView() {
        isViewCreated = false;
        super.onDestroyView();
    }
}
