package com.example.tfg.fragments.form;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private ArrayList<Food> itemList;
    private ArrayList<Food> selectedItemsList;
    private ListView selectedItemsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_day_form, container, false);

        selectedItemsListView = view.findViewById(R.id.selectedItemsListViewBreakfast);
        Button addItemButton = view.findViewById(R.id.addItemButtonBreakfast);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new FoodListAdapter(getActivity(), 8, new ArrayList<>());
                showPopupListView();
            }
        });

        return view;
    }

    private void showPopupListView() {
        itemList = new ArrayList<>();
        itemList.add(new Food(1, "Agua"));
        itemList.add(new Food(2, "Pan"));
        itemList.add(new Food(3, "Mandarina"));

        selectedItemsList = new ArrayList<>();

        // Inflate the pop-up layout
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup, null);
        EditText searchEditText = popupView.findViewById(R.id.filterFoodForm);
        ListView listView = popupView.findViewById(R.id.foodListForm);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Item");
        builder.setView(popupView);

        adapter = new FoodListAdapter(requireContext(), 8, itemList);
        listView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase(Locale.getDefault());
                filterItems(searchText);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedFood = itemList.get(position);
                addSelectedItemToLayout(selectedFood);
            }
        });

        builder.create().show();
    }

    private void filterItems(String searchText) {
        itemList.clear();
        for (Food item : selectedItemsList) {
            if (item.getFoodName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                itemList.add(item);
            }
        }
    }

    private void addSelectedItemToLayout(Food food) {
        if(!selectedItemsList.contains(food)) {
            selectedItemsList.add(food);
            FoodListAdapter selectedItemsAdapter = new FoodListAdapter(requireContext(), 8, selectedItemsList);
            selectedItemsListView.setAdapter(selectedItemsAdapter);
        } else {
            Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
        }

    }


    /*private void showPopupListView() {
        // Inflate the pop-up layout
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup, null);
        EditText searchEditText = popupView.findViewById(R.id.filterFoodForm);
        ListView listView = popupView.findViewById(R.id.foodListForm);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Item");
        builder.setView(popupView);

        listView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase(Locale.getDefault());
                fetchSearchResults(searchText, adapter);
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Food selectedFood = adapter.getItem(which);
                addSelectedItemToLayout(selectedFood.getFoodName());
            }
        });

        builder.create().show();
    }

    private void fetchSearchResults(String query, FoodListAdapter adapter) {
        String apiUrl = "https://sanger.dia.fi.upm.es/foodnorm/foods/" + query;

        FoodListConnection connection = new FoodListConnection(null, adapter);
        connection.execute(apiUrl);
    }



    private void addSelectedItemToLayout(String selectedItem) {
        LinearLayout selectedItemsLayout = requireView().findViewById(R.id.selectedItemsLayoutMorning);

        TextView textView = new TextView(requireContext());
        textView.setText(selectedItem);
        textView.setPadding(0, 8, 0, 8);
        selectedItemsLayout.addView(textView);
    }*/
}