package com.example.tfg.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tfg.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodListAdapter extends BaseAdapter implements Filterable {

    private final LayoutInflater inflater;
    private final List<Food> foods;
    private final List<Food> filteredFoods;
    private final int pageSize;
    private int currentPage = 0;
    private Filter foodFilter;

    /*
    public FoodListAdapter(@NonNull Context context, int pageSize, @NonNull List<Food> foods) {
        inflater = LayoutInflater.from(context);
        this.pageSize = pageSize;
        this.foods = foods;
        this.filteredFoods = new ArrayList<>(foods);
        this.foodFilter = new FoodFilter();
    }*/
    public FoodListAdapter(@NonNull Context context, int pageSize, @NonNull List<Food> foods) {
        inflater = LayoutInflater.from(context);
        this.pageSize = pageSize;
        this.foods = new ArrayList<>(foods);
        this.filteredFoods = new ArrayList<>(foods);
        this.foodFilter = new FoodFilter();
    }

    /*
    public void updateData(Food[] newFoods){
        foods.clear();
        filteredFoods.clear();
        foods.addAll(Arrays.asList(newFoods));
        filteredFoods.addAll(Arrays.asList(newFoods));
        notifyDataSetChanged();
    }*/

    public void updateData(List<Food> newFoods) {
        foods.clear();
        foods.addAll(newFoods);
        filteredFoods.clear();
        filteredFoods.addAll(newFoods);
        currentPage = 0; // Reset the current page to the first page
        notifyDataSetChanged();
    }

    public int getCount(){
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, filteredFoods.size());
        return end - start;
    }

    public Food getItem(int position){
        int start = currentPage * pageSize;
        return filteredFoods.get(start + position);
    }

    public long getItemId(int position){
        int start = currentPage * pageSize;
        return filteredFoods.get(start + position).getId();
    }

    public void goToNextPage() {
        if ((currentPage + 1) * pageSize < filteredFoods.size()) {
            currentPage++;
            notifyDataSetChanged();
        }
    }
    public void goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return foodFilter;
    }
    /*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.food_inlist, parent, false);
        }

        TextView foodNameTextView = convertView.findViewById(R.id.foodName_inList);
        //TextView foodCaloriesTextView = convertView.findViewById(R.id.foodCalories_inList);

        Food food = getItem(position);
        foodNameTextView.setText(food.getFoodName());
        //foodCaloriesTextView.setText(String.valueOf(food.getKcal()));
        return convertView;
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.food_inlist, parent, false);
            holder = new ViewHolder();
            holder.foodNameTextView = convertView.findViewById(R.id.foodName_inList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Food food = getItem(position);
        holder.foodNameTextView.setText(food.getFoodName());

        return convertView;
    }

    private static class ViewHolder {
        TextView foodNameTextView;
    }

    private class FoodFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filterResults.count = foods.size();
                filterResults.values = foods;
            } else {
                List<Food> filteredList = new ArrayList<>();
                String filterString = constraint.toString().toLowerCase();
                for (Food food : foods) {
                    if (food.getFoodName().toLowerCase().contains(filterString)) {
                        filteredList.add(food);
                    }
                }
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredFoods.clear();
            filteredFoods.addAll((List<Food>) results.values);
            notifyDataSetChanged();
        }
    }

    public void clearFilter(){
        filteredFoods.clear();
        filteredFoods.addAll(foods);
        currentPage = 0;
        notifyDataSetChanged();
    }


}
