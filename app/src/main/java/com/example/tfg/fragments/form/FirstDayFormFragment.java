package com.example.tfg.fragments.form;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.R;
import com.example.tfg.food.Food;
import com.example.tfg.food.FoodListAdapter;
import com.example.tfg.food.FoodListConnection;

import java.util.ArrayList;
import java.util.Locale;

public class FirstDayFormFragment extends Fragment {

    private FoodListAdapter adapter;
    private ArrayList<Food> selectedItemsList;
    private ListView selectedItemsListView;
    private FoodListAdapter selectedItemsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_day_form, container, false);
        selectedItemsList = new ArrayList<>();

        selectedItemsListView = view.findViewById(R.id.selectedItemsListViewBreakfast);
        Button addItemButton = view.findViewById(R.id.addItemButtonBreakfast);

        selectedItemsAdapter = new FoodListAdapter(requireContext(), 4, selectedItemsList);
        selectedItemsListView.setAdapter(selectedItemsAdapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupListView();
            }
        });

        selectedItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedItem = (Food) parent.getItemAtPosition(position);
                Toast.makeText(requireContext(), "Clicked: " + selectedItem.getFoodName(), Toast.LENGTH_SHORT).show();
                selectedItemsList.remove(selectedItem);
                selectedItemsAdapter.updateData(selectedItemsList);
            }
        });

        return view;
    }

    private void addSelectedItemToLayout(Food food) {
        boolean isItemAlreadySelected = false;
        for (Food selectedFood : selectedItemsList) {
            if (selectedFood.getId().equals(food.getId())) {
                isItemAlreadySelected = true;
                break;
            }
        }

        if (!isItemAlreadySelected) {
            selectedItemsList.add(food);
            selectedItemsAdapter.updateData(selectedItemsList);
        } else {
            Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopupListView() {
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup, null);
        EditText searchEditText = popupView.findViewById(R.id.filterFoodForm);
        ListView listView = popupView.findViewById(R.id.foodListForm);
        Button filterButton = popupView.findViewById(R.id.acceptFilter);

        adapter = new FoodListAdapter(getActivity(), 8, new ArrayList<>());
        listView.setAdapter(adapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                fetchSearchResults(searchText, adapter);
            }
        });

        PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedFood = (Food) parent.getItemAtPosition(position);
                addSelectedItemToLayout(selectedFood);

                // Dismiss the popup window after selecting an item
                popupWindow.dismiss();
            }
        });

    }

    private void fetchSearchResults(String query, FoodListAdapter adapter) {
        String apiUrl = "https://sanger.dia.fi.upm.es/foodnorm/foods/" + query;
        FoodListConnection connection = new FoodListConnection(null, adapter);
        connection.execute(apiUrl);
    }

}