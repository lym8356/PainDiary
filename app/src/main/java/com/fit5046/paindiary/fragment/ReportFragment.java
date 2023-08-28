package com.fit5046.paindiary.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.paindiary.databinding.FragmentReportBinding;
import com.fit5046.paindiary.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        tabLayout = binding.tabLayout;
        viewPager = binding.reportViewPager;

        //use page adapter to navigate between tabs
        adapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        //add tab object to tab lists
        adapter.addFragment(new PainLocationChartFragment(), "Pain Location Chart");
        adapter.addFragment(new StepsTakenChartFragment(), "Steps Walked Chart");
        adapter.addFragment(new PainWeatherChartFragment(), "Pain Weather Chart");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}