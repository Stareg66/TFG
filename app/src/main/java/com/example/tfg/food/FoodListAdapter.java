package com.example.tfg.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tfg.R;

import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Food> {

    private final LayoutInflater inflater;
    private final List<Food> foods;

    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<Food> foods) {
        super(context, resource, foods);
        inflater = LayoutInflater.from(context);
        this.foods = foods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.food_inlist, parent, false);
        }

        TextView foodNameTextView = convertView.findViewById(R.id.foodName_inList);
        TextView foodCaloriesTextView = convertView.findViewById(R.id.foodCalories_inList);

        Food food = foods.get(position);
        foodNameTextView.setText(food.getFoodName());
        foodCaloriesTextView.setText(String.valueOf(food.getKcal()));
        return convertView;
    }
}
