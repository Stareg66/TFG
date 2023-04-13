package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfg.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailedFragment extends Fragment {

    private TextView mFoodName;
    private TextView mFoodCalories;

    public FoodDetailedFragment() {
        // Required empty public constructor
    }

    public static FoodDetailedFragment newInstance(String foodName, String foodCalories) {
        FoodDetailedFragment fragment = new FoodDetailedFragment();
        Bundle args = new Bundle();
        args.putString("foodName", foodName);
        args.putString("foodCalories", foodCalories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_detailed, container, false);

        mFoodName = rootView.findViewById(R.id.foodName_detailed);
        mFoodCalories = rootView.findViewById(R.id.foodCalories_detailed);

        if (getArguments() != null) {
            mFoodName.setText(getArguments().getString("foodName"));
            mFoodCalories.setText(getArguments().getString("foodCalories"));
        }

        return rootView;
    }
}