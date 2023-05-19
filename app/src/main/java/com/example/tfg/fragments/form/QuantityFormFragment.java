package com.example.tfg.fragments.form;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class QuantityFormFragment extends Fragment {

    private FoodListAdapter adapter;

    //List
    private ArrayList<Food> selectedItemsList;
    private ListView selectedItemsListView;
    private FoodListAdapter selectedItemsAdapter;

    //User
    private String userId;
    private DocumentReference userRef;

    public QuantityFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quantity_form, container, false);

        //User
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        selectedItemsList = new ArrayList<>();
        selectedItemsListView = view.findViewById(R.id.foodListQuantity);
        Button addItemButton = view.findViewById(R.id.addItemQuantity);

        fetchSelectedItemsFromFirestore();

        selectedItemsAdapter = new FoodListAdapter(requireContext(), 8, selectedItemsList);
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
                View frequencyView = LayoutInflater.from(requireContext()).inflate(R.layout.food_frequency_detailed, null);
                PopupWindow popupWindow = new PopupWindow(frequencyView, 1000, 1000, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(frequencyView, Gravity.CENTER, 0, 0);

                TextView foodNameDetailed = frequencyView.findViewById(R.id.food_frequency_name_detailed);
                TextView frequencyDetailed = frequencyView.findViewById(R.id.food_frequency_detailed);
                Button buttonChangeFrequency = frequencyView.findViewById(R.id.buttonChangeFrequency);
                Button deleteFoodFrequency = frequencyView.findViewById(R.id.deleteFoodFrequency);

                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> frequencyList = (Map<String, Object>) documentSnapshot.getData().get("frequencyList");

                            if (frequencyList != null) {
                                int frequency = 0; // Default frequency if not found

                                if (frequencyList.containsKey(selectedItem.getId())) {
                                    Map<String, Object> foodMap = (Map<String, Object>) frequencyList.get(selectedItem.getId());
                                    frequency = ((Long) foodMap.get("frequency")).intValue();
                                    foodNameDetailed.setText(selectedItem.getFoodName());
                                    frequencyDetailed.setText(frequency);
                                }


                            }
                        }
                    }
                });

                buttonChangeFrequency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View frequencyChange = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup_frequency, null);
                        PopupWindow popupWindow = new PopupWindow(frequencyChange, 1000, 1000, true);
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(frequencyChange, Gravity.CENTER, 0, 0);

                        TextView frequencyDetailed = frequencyChange.findViewById(R.id.frequencyNumber);
                        Button acceptFrequency = frequencyChange.findViewById(R.id.acceptFrequency);
                        Button denyFrequency = frequencyChange.findViewById(R.id.denyFrequency);

                        Food selectedFood = (Food) parent.getItemAtPosition(position);

                        acceptFrequency.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!frequencyDetailed.getText().toString().isEmpty()) {
                                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                Map<String, Object> frequencyList = documentSnapshot.toObject(Map.class);
                                                if (frequencyList != null) {
                                                    Map<String, Object> foodMap = (Map<String, Object>) frequencyList.get("frequencyList");
                                                    if (foodMap != null) {
                                                        foodMap.put(String.valueOf(selectedFood.getId()), Integer.parseInt(frequencyDetailed.getText().toString()));
                                                        userRef.update("frequencyList", foodMap);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }

                                addSelectedItemToLayout(selectedFood, Integer.parseInt(frequencyDetailed.getText().toString()));
                                popupWindow.dismiss();
                            }
                        });

                        denyFrequency.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                            }
                        });

                    }
                });

                deleteFoodFrequency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedItemsList.remove(selectedItem);
                        deleteSelectedItemToFirestore(selectedItem);
                        selectedItemsAdapter.updateData(selectedItemsList);
                    }
                });
            }
        });

        return view;
    }

    private void fetchSelectedItemsFromFirestore() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> selectedItemsMap = (Map<String, Object>) documentSnapshot.get("frequencyList");
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
                }
            }
        });
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
                View frequencyView = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup_frequency, null);
                Button acceptFrequency = frequencyView.findViewById(R.id.acceptFrequency);
                Button denyFrequency = frequencyView.findViewById(R.id.denyFrequency);
                EditText frequencyNumber = frequencyView.findViewById(R.id.frequencyNumber);

                PopupWindow popupWindowQuantity = new PopupWindow(frequencyView, 750, 750, true);
                popupWindowQuantity.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                popupWindowQuantity.setOutsideTouchable(false);
                popupWindowQuantity.setFocusable(true);
                popupWindowQuantity.showAtLocation(frequencyView, Gravity.CENTER, 0, 0);
                acceptFrequency.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Food selectedFood = (Food) parent.getItemAtPosition(position);
                        addSelectedItemToLayout(selectedFood, Integer.parseInt(frequencyNumber.getText().toString()));
                        popupWindowQuantity.dismiss();
                        popupWindow.dismiss();
                    }

                });

                denyFrequency.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        popupWindowQuantity.dismiss();
                    }
                });
            }
        });
    }

    private void addSelectedItemToLayout(Food food, int frequency) {
        boolean isItemAlreadySelected = false;
        for (Food selectedFood : selectedItemsList) {
            if (selectedFood.getId().equals(food.getId())) {
                isItemAlreadySelected = true;
                break;
            }
        }

        if (!isItemAlreadySelected) {
            selectedItemsList.add(food);
            addSelectedItemToFirestore(food, frequency);
            selectedItemsAdapter.updateData(selectedItemsList);
        } else {
            Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSelectedItemToFirestore(Food food, int frequency) {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("foodId", food.getId());
        foodMap.put("foodName", food.getFoodName());
        foodMap.put("frequency", frequency);
        userRef.update("frequencyList." + food.getId(), foodMap);
    }

    private void deleteSelectedItemToFirestore(Food food){
        userRef.update("frequencyList", FieldValue.arrayRemove(food));
    }

    private void fetchSearchResults(String query, FoodListAdapter adapter) {
        String apiUrl = "https://sanger.dia.fi.upm.es/foodnorm/foods/" + query;
        FoodListConnection connection = new FoodListConnection(null, adapter);
        connection.execute(apiUrl);
    }
}