package com.fit5046.paindiary.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.paindiary.databinding.FragmentDailyRecordsBinding;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.adapter.RecyclerAdapter;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;

import java.util.List;

public class DailyRecordsFragment extends Fragment {
    private FragmentDailyRecordsBinding binding;
    private PainDataViewModel painDataViewModel;


    public DailyRecordsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDailyRecordsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Initialize recycler view
        RecyclerView recyclerView = binding.recordRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        // set UI divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        final RecyclerAdapter adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        // update recycler view when there is a change in database
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainDataViewModel.class);
        painDataViewModel.getAllPainData().observe(getViewLifecycleOwner(), new Observer<List<PainData>>() {
            @Override
            public void onChanged(List<PainData> painData) {
                adapter.setPainDataList(painData);
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