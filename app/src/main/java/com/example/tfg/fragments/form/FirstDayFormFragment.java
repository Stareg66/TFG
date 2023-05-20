package com.example.tfg.fragments.form;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FirstDayFormFragment extends Fragment {

    private FoodListAdapter adapter;

    //Breakfast
    private ArrayList<Food> selectedItemsList;
    private ListView selectedItemsListView;
    private FoodListAdapter selectedItemsAdapter;

    //Lunch
    private ArrayList<Food> selectedItemsListLu;
    private ListView selectedItemsListViewLu;
    private FoodListAdapter selectedItemsAdapterLu;

    //Dinner
    private ArrayList<Food> selectedItemsListDi;
    private ListView selectedItemsListViewDi;
    private FoodListAdapter selectedItemsAdapterDi;

    //User
    private String userId;
    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_day_form, container, false);

        //User
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        //Breakfast
        selectedItemsList = new ArrayList<>();
        selectedItemsListView = view.findViewById(R.id.selectedItemsListViewBreakfast);
        Button addItemButton = view.findViewById(R.id.addItemButtonBreakfast);

        //Lunch
        selectedItemsListLu = new ArrayList<>();
        selectedItemsListViewLu = view.findViewById(R.id.selectedItemsListViewLunch);
        Button addItemButtonLu = view.findViewById(R.id.addItemButtonLunch);

        //Dinner
        selectedItemsListDi = new ArrayList<>();
        selectedItemsListViewDi = view.findViewById(R.id.selectedItemsListViewDinner);
        Button addItemButtonDi = view.findViewById(R.id.addItemButtonDinner);

        fetchSelectedItemsFromFirestore();

        //Breakfast
        selectedItemsAdapter = new FoodListAdapter(requireContext(), 8, selectedItemsList);
        selectedItemsListView.setAdapter(selectedItemsAdapter);

        //Lunch
        selectedItemsAdapterLu = new FoodListAdapter(requireContext(), 8, selectedItemsListLu);
        selectedItemsListViewLu.setAdapter(selectedItemsAdapterLu);

        //Dinner
        selectedItemsAdapterDi = new FoodListAdapter(requireContext(), 8, selectedItemsListDi);
        selectedItemsListViewDi.setAdapter(selectedItemsAdapterDi);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupListView("breakfast");
            }
        });

        addItemButtonLu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupListView("lunch");
            }
        });

        addItemButtonDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupListView("dinner");
            }
        });

        selectedItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedItem = (Food) parent.getItemAtPosition(position);
                Toast.makeText(requireContext(), "Eliminado: " + selectedItem.getFoodName(), Toast.LENGTH_SHORT).show();
                selectedItemsList.remove(selectedItem);
                deleteSelectedItemToFirestore(selectedItem, "breakfast");
                selectedItemsAdapter.updateData(selectedItemsList);
            }
        });

        selectedItemsListViewLu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedItem = (Food) parent.getItemAtPosition(position);
                Toast.makeText(requireContext(), "Eliminado: " + selectedItem.getFoodName(), Toast.LENGTH_SHORT).show();
                selectedItemsListLu.remove(selectedItem);
                deleteSelectedItemToFirestore(selectedItem, "lunch");
                selectedItemsAdapterLu.updateData(selectedItemsListLu);
            }
        });

        selectedItemsListViewDi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedItem = (Food) parent.getItemAtPosition(position);
                Toast.makeText(requireContext(), "Eliminado: " + selectedItem.getFoodName(), Toast.LENGTH_SHORT).show();
                selectedItemsListDi.remove(selectedItem);
                deleteSelectedItemToFirestore(selectedItem, "dinner");
                selectedItemsAdapterDi.updateData(selectedItemsListDi);
            }
        });

        return view;
    }

    private void fetchSelectedItemsFromFirestore() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> selectedItemsMap = (Map<String, Object>) documentSnapshot.get("selectedItemsMorning1Day");
                    if (selectedItemsMap != null) {
                        List<Food> selectedItems = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : selectedItemsMap.entrySet()) {
                            String foodId = entry.getKey();
                            Map<String, Object> foodMap = (Map<String, Object>) entry.getValue();

                            Food food = new Food(
                                    Integer.parseInt(foodId),
                                    (String) foodMap.get("foodName")
                            );
                            selectedItems.add(food);
                        }
                        selectedItemsAdapter.updateData(selectedItems);
                    }

                    selectedItemsMap = (Map<String, Object>) documentSnapshot.get("selectedItemsLunch1Day");
                    if (selectedItemsMap != null) {
                        List<Food> selectedItems = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : selectedItemsMap.entrySet()) {
                            String foodId = entry.getKey();
                            Map<String, Object> foodMap = (Map<String, Object>) entry.getValue();

                            Food food = new Food(
                                    Integer.parseInt(foodId),
                                    (String) foodMap.get("foodName")
                            );
                            selectedItems.add(food);
                        }
                        selectedItemsAdapterLu.updateData(selectedItems);
                    }

                    selectedItemsMap = (Map<String, Object>) documentSnapshot.get("selectedItemsDinner1Day");
                    if (selectedItemsMap != null) {
                        List<Food> selectedItems = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : selectedItemsMap.entrySet()) {
                            String foodId = entry.getKey();
                            Map<String, Object> foodMap = (Map<String, Object>) entry.getValue();

                            Food food = new Food(
                                    Integer.parseInt(foodId),
                                    (String) foodMap.get("foodName")
                            );
                            selectedItems.add(food);
                        }
                        selectedItemsAdapterDi.updateData(selectedItems);
                    }
                }
            }
        });
    }

    private void addSelectedItemToLayout(Food food, String time, int quantity) {
        boolean isItemAlreadySelected = false;
        switch(time){
            case "breakfast":
                for (Food selectedFood : selectedItemsList) {
                    if (selectedFood.getId().equals(food.getId())) {
                        isItemAlreadySelected = true;
                        break;
                    }
                }

                if (!isItemAlreadySelected) {
                    selectedItemsList.add(food);
                    addSelectedItemToFirestore(food, time, quantity);
                    selectedItemsAdapter.updateData(selectedItemsList);
                } else {
                    Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
                }
                break;
            case "lunch":
                for (Food selectedFood : selectedItemsListLu) {
                    if (selectedFood.getId().equals(food.getId())) {
                        isItemAlreadySelected = true;
                        break;
                    }
                }

                if (!isItemAlreadySelected) {
                    selectedItemsListLu.add(food);
                    addSelectedItemToFirestore(food, time, quantity);
                    selectedItemsAdapterLu.updateData(selectedItemsListLu);
                } else {
                    Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
                }
                break;
            case "dinner":
                for (Food selectedFood : selectedItemsListDi) {
                    if (selectedFood.getId().equals(food.getId())) {
                        isItemAlreadySelected = true;
                        break;
                    }
                }

                if (!isItemAlreadySelected) {
                    selectedItemsListDi.add(food);
                    addSelectedItemToFirestore(food, time, quantity);
                    selectedItemsAdapterDi.updateData(selectedItemsListDi);
                } else {
                    Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void addSelectedItemToFirestore(Food food, String time, int quantity) {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("foodId", food.getId());
        foodMap.put("foodName", food.getFoodName());
        foodMap.put("amountInMg", quantity);

        switch (time) {
            case "breakfast":
                userRef.update("selectedItemsMorning1Day." + food.getId(), foodMap);
                break;
            case "lunch":
                userRef.update("selectedItemsLunch1Day." + food.getId(), foodMap);
                break;
            case "dinner":
                userRef.update("selectedItemsDinner1Day." + food.getId(), foodMap);
                break;
        }
    }

    private void deleteSelectedItemToFirestore(Food food, String time) {
        switch(time){
            case "breakfast":
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> foodList = (Map<String, Object>) documentSnapshot.get("selectedItemsMorning1Day");
                            if (foodList != null && foodList.containsKey(String.valueOf(food.getId()))) {
                                foodList.remove(String.valueOf(food.getId()));
                                userRef.update("selectedItemsMorning1Day", foodList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                selectedItemsList.remove(food);
                                            }
                                        });
                            }
                        }
                    }
                });
                break;
            case "lunch":
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> foodList = (Map<String, Object>) documentSnapshot.get("selectedItemsLunch1Day");
                            if (foodList != null && foodList.containsKey(String.valueOf(food.getId()))) {
                                foodList.remove(String.valueOf(food.getId()));
                                userRef.update("selectedItemsLunch1Day", foodList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                selectedItemsList.remove(food);
                                            }
                                        });
                            }
                        }
                    }
                });
                break;
            case "dinner":
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> foodList = (Map<String, Object>) documentSnapshot.get("selectedItemsDinner1Day");
                            if (foodList != null && foodList.containsKey(String.valueOf(food.getId()))) {
                                foodList.remove(String.valueOf(food.getId()));
                                userRef.update("selectedItemsDinner1Day", foodList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                selectedItemsList.remove(food);
                                            }
                                        });
                            }
                        }
                    }
                });
                break;
        }
    }

    private void showPopupListView(String time) {
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

        View quantityView = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup_confirmation, null);
        Button acceptQuantity = quantityView.findViewById(R.id.acceptQuantity);
        Button denyQuantity = quantityView.findViewById(R.id.denyQuantity);
        EditText quantity = quantityView.findViewById(R.id.quantityTaken);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupWindow popupWindowQuantity = new PopupWindow(quantityView, 750, 750, true);
                popupWindowQuantity.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                popupWindowQuantity.setOutsideTouchable(false);
                popupWindowQuantity.setFocusable(true);
                popupWindowQuantity.showAtLocation(quantityView, Gravity.CENTER, 0, 0);
                acceptQuantity.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Food selectedFood = (Food) parent.getItemAtPosition(position);
                        addSelectedItemToLayout(selectedFood, time, Integer.parseInt(quantity.getText().toString()));
                        popupWindowQuantity.dismiss();
                        popupWindow.dismiss();
                    }

                });

                denyQuantity.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        popupWindowQuantity.dismiss();
                    }
                });
            }
        });

    }

    private void fetchSearchResults(String query, FoodListAdapter adapter) {
        String apiUrl = "https://sanger.dia.fi.upm.es/foodnorm/foods/" + query;
        FoodListConnection connection = new FoodListConnection(null, adapter);
        connection.execute(apiUrl);
    }

}