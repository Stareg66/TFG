package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfg.R;
import com.example.tfg.fragments.form.FirstDayFormFragment;
import com.example.tfg.fragments.form.SecondDayFormFragment;
import com.example.tfg.fragments.form.ThirdDayFormFragment;
import com.example.tfg.fragments.form.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FormFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_form, container, false);
        tabLayout = view.findViewById(R.id.tabLayoutForm);
        viewPager = view.findViewById(R.id.viewPagerForm);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getChildFragmentManager());

        vpAdapter.addFragment(new FirstDayFormFragment(),"Primer día");
        vpAdapter.addFragment(new SecondDayFormFragment(),"Segundo día");
        vpAdapter.addFragment(new ThirdDayFormFragment(),"Tercer día");

        viewPager.setAdapter(vpAdapter);
    }
}