package com.example.tfg.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Category> categoryList;

    public CategoryListAdapter(@NonNull Context context,  @androidx.annotation.NonNull List<Category> categories) {
        inflater = LayoutInflater.from(context);
        categoryList = new ArrayList<>(categories);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Category getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.foodcategory_inlist, viewGroup, false);
            holder = new ViewHolder();
            holder.categoryName = view.findViewById(R.id.foodCategoryName_inList);
            holder.categoryIcon = view.findViewById(R.id.foodCategoryImage_inList);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Category category = getItem(i);
        holder.categoryName.setText(category.getCategory_name());
        Integer groupId = category.getCategory_id();
        if(groupId==1){
            holder.categoryIcon.setImageResource(R.drawable.icons8_milk_bottle_32);
        } else if(groupId==2){
            holder.categoryIcon.setImageResource(R.drawable.icons8_eggs_32);
        } else if(groupId==3){
            holder.categoryIcon.setImageResource(R.drawable.icons8_meat_32);
        } else if(groupId==4){
            holder.categoryIcon.setImageResource(R.drawable.icons8_fish_food_32);
        } else if(groupId==5){
            holder.categoryIcon.setImageResource(R.drawable.icons8_olive_oil_32);
        } else if(groupId==6){
            holder.categoryIcon.setImageResource(R.drawable.icons8_corn_32);
        } else if(groupId==7){
            holder.categoryIcon.setImageResource(R.drawable.icons8_seeds_32);
        } else if(groupId==8){
            holder.categoryIcon.setImageResource(R.drawable.icons8_eggplant_32);
        } else if(groupId==9){
            holder.categoryIcon.setImageResource(R.drawable.icons8_fruit_32);
        } else if(groupId==10){
            holder.categoryIcon.setImageResource(R.drawable.icons8_sugar_cube_32);
        } else if(groupId==11){
            holder.categoryIcon.setImageResource(R.drawable.icons8_soda_32);
        } else if(groupId==12){
            holder.categoryIcon.setImageResource(R.drawable.icons8_cherry_cheesecake_32);
        }

        return view;

    }

    private static class ViewHolder{
        TextView categoryName;
        ImageView categoryIcon;
    }
}
