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
import com.example.tfg.food.Category;
import com.example.tfg.food.CategoryListAdapter;
import com.example.tfg.food.Food;
import com.example.tfg.food.FoodListAdapter;
import com.example.tfg.food.FoodListConnection;
import com.example.tfg.food.Micronutrients;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuantityFormFragment extends Fragment {

    //Category
    private CategoryListAdapter adapterCategory;
    private ListView categoryListView;
    private ArrayList<Category> categoryList;

    //List Food
    private FoodListAdapter adapter;
    private ArrayList<Food> selectedItemsList;
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

        //Category
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"Leche y productos lácteos"));
        categoryList.add(new Category(2,"Huevos y productos con huevo"));
        categoryList.add(new Category(3,"Carne y productos cárnicos"));
        categoryList.add(new Category(4,"Pescados, moluscos, reptiles y crustáceos"));
        categoryList.add(new Category(5,"Grasas y aceites"));
        categoryList.add(new Category(6,"Granos y productos con grano"));
        categoryList.add(new Category(7,"Semillas, nueces y otros productos"));
        categoryList.add(new Category(8,"Vegetales y productos con vegetales"));
        categoryList.add(new Category(9,"Frutas y productos con fruta"));
        categoryList.add(new Category(10,"Azucar, chocolate y otros productos relacionados"));
        categoryList.add(new Category(11,"Bebidas (sin leche)"));
        categoryList.add(new Category(12,"Comidas varias"));

        categoryListView = view.findViewById(R.id.foodListQuantity);
        Button addItemButton = view.findViewById(R.id.addItemQuantity);

        adapterCategory = new CategoryListAdapter(requireContext(), categoryList);
        categoryListView.setAdapter(adapterCategory);

        selectedItemsList = new ArrayList<>();

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showFoodsCategoryPopUp(categoryList.get(i));
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupListView();
            }
        });

        return view;
    }

    private void fetchSelectedItemsFromFirestore() {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Object frequencyListObj = documentSnapshot.get("frequencyList");
                    if (frequencyListObj != null) {
                        List<Food> selectedItems = new ArrayList<>();
                        Map<String, Object> selectedItemsMap = (Map<String, Object>) frequencyListObj;
                        for (Map.Entry<String, Object> entry : selectedItemsMap.entrySet()) {
                            String groupId = entry.getKey();
                            Map<String, Object> groupList = (Map<String, Object>) entry.getValue();
                            for (Map.Entry<String, Object> foodEntry : groupList.entrySet()) {
                                String foodId = foodEntry.getKey();
                                Map<String, Object> foodMap = (Map<String, Object>) foodEntry.getValue();

                                Map<String, Double> micronutrientMap = (Map<String, Double>) foodMap.get("micronutrientes");

                                Micronutrients micronutrients = new Micronutrients(micronutrientMap.get("proteina_total"), micronutrientMap.get("carbohidratos"),
                                        micronutrientMap.get("fibra_total"),micronutrientMap.get("azucares_totales"),micronutrientMap.get("grasa_total"),
                                        micronutrientMap.get("ag_saturados_total"),micronutrientMap.get("ag_poliinsaturados_total"),micronutrientMap.get("ag_monoinsaturados_total"),
                                        micronutrientMap.get("ag_trans_total"),micronutrientMap.get("colesterol"),micronutrientMap.get("sodio"),
                                        micronutrientMap.get("potasio"),micronutrientMap.get("vitamina_a"),micronutrientMap.get("vitamina_c"),
                                        micronutrientMap.get("calcio"),micronutrientMap.get("hierro_total"));

                                Food food = new Food(
                                        Integer.parseInt(foodId),
                                        (String) foodMap.get("foodName"),
                                        (Long) foodMap.get("groupId"),
                                        micronutrients
                                );
                                selectedItems.add(food);
                            }
                        }

                        selectedItemsAdapter.updateData(selectedItems);
                    }
                }
            }
        });
    }

    private void fetchCategoryItemsFromFirestore(long categoryId) {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Object frequencyListObj = documentSnapshot.get("frequencyList");
                    if (frequencyListObj != null) {
                        List<Food> selectedItems = new ArrayList<>();
                        Map<String, Object> frequencyList = (Map<String, Object>) frequencyListObj;

                        // Add the category ID to the frequencyList if it doesn't exist
                        if (!frequencyList.containsKey(String.valueOf(categoryId))) {
                            frequencyList.put(String.valueOf(categoryId), new HashMap<>());
                        }

                        Map<String, Object> selectedItemsMap = (Map<String, Object>) frequencyList.get(String.valueOf(categoryId));
                        for (Map.Entry<String, Object> entry : selectedItemsMap.entrySet()) {
                            String foodId = entry.getKey();
                            Map<String, Object> foodMap = (Map<String, Object>) entry.getValue();

                            Map<String, Double> micronutrientMap = (Map<String, Double>) foodMap.get("micronutrientes");

                            Micronutrients micronutrients = new Micronutrients(
                                    micronutrientMap.get("proteina_total"),
                                    micronutrientMap.get("carbohidratos"),
                                    micronutrientMap.get("fibra_total"),
                                    micronutrientMap.get("azucares_totales"),
                                    micronutrientMap.get("grasa_total"),
                                    micronutrientMap.get("ag_saturados_total"),
                                    micronutrientMap.get("ag_poliinsaturados_total"),
                                    micronutrientMap.get("ag_monoinsaturados_total"),
                                    micronutrientMap.get("ag_trans_total"),
                                    micronutrientMap.get("colesterol"),
                                    micronutrientMap.get("sodio"),
                                    micronutrientMap.get("potasio"),
                                    micronutrientMap.get("vitamina_a"),
                                    micronutrientMap.get("vitamina_c"),
                                    micronutrientMap.get("calcio"),
                                    micronutrientMap.get("hierro_total")
                            );

                            Food food = new Food(
                                    Integer.parseInt(foodId),
                                    (String) foodMap.get("foodName"),
                                    categoryId,
                                    micronutrients
                            );

                            selectedItems.add(food);
                        }

                        selectedItemsAdapter.updateData(selectedItems);
                    }
                }
            }
        });
    }



    private void showFoodsCategoryPopUp(Category category) {
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.foodcategory_detailed, null);
        TextView categoryText = popupView.findViewById(R.id.category_text);
        ListView foodCategoryList = popupView.findViewById(R.id.category_list);

        PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1750, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        categoryText.setText(category.getCategory_name());

        selectedItemsList = new ArrayList<>();
        selectedItemsAdapter = new FoodListAdapter(requireContext(), 8, selectedItemsList);
        foodCategoryList.setAdapter(selectedItemsAdapter);

        fetchCategoryItemsFromFirestore(category.getCategory_id());

        foodCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food selectedItem = (Food) parent.getItemAtPosition(position);
                View frequencyView = LayoutInflater.from(requireContext()).inflate(R.layout.food_frequency_detailed, null);
                PopupWindow popupWindow = new PopupWindow(frequencyView, 1000, 500 , true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorText)));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(frequencyView, Gravity.CENTER, 0, 0);

                TextView foodNameDetailed = frequencyView.findViewById(R.id.food_frequency_name_detailed);
                TextView frequencyDetailed = frequencyView.findViewById(R.id.food_frequency_detailed);
                Button buttonChangeFrequency = frequencyView.findViewById(R.id.buttonChangeFrequency);
                Button deleteFoodFrequency = frequencyView.findViewById(R.id.deleteFoodFrequency);

                foodNameDetailed.setText(selectedItem.getFoodName());

                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> frequencyList = (Map<String, Object>) documentSnapshot.getData().get("frequencyList");

                            if (frequencyList != null) {
                                int frequency = 0;

                                if (frequencyList.containsKey(String.valueOf(selectedItem.getGroup_id()))) {
                                    Map<String, Object> groupList = (Map<String, Object>) frequencyList.get(String.valueOf(selectedItem.getGroup_id()));
                                    if (groupList.containsKey(String.valueOf(selectedItem.getId()))) {
                                        Map<String, Object> foodMap = (Map<String, Object>) groupList.get(String.valueOf(selectedItem.getId()));
                                        frequency = ((Long) foodMap.get("frequency")).intValue();
                                    }
                                }

                                foodNameDetailed.setText(selectedItem.getFoodName());
                                frequencyDetailed.setText(String.valueOf(frequency));
                            }
                        }
                    }
                });

                buttonChangeFrequency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View frequencyChange = LayoutInflater.from(requireContext()).inflate(R.layout.food_inlist_popup_frequency, null);
                        PopupWindow popupWindowModify = new PopupWindow(frequencyChange, 1000, 500, true);
                        popupWindowModify.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                        popupWindowModify.setOutsideTouchable(true);
                        popupWindowModify.setFocusable(true);
                        popupWindowModify.showAtLocation(frequencyChange, Gravity.CENTER, 0, 0);

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
                                                Map<String, Object> frequencyList = (Map<String, Object>) documentSnapshot.get("frequencyList");
                                                if (frequencyList != null) {
                                                    Map<String, Object> groupList = (Map<String, Object>) frequencyList.get(String.valueOf(selectedFood.getGroup_id()));
                                                    if (groupList != null) {
                                                        Map<String, Object> foodMap = (Map<String, Object>) groupList.get(String.valueOf(selectedFood.getId()));
                                                        if (foodMap != null) {
                                                            foodMap.put("frequency", Integer.parseInt(frequencyDetailed.getText().toString()));
                                                            userRef.update("frequencyList." + selectedFood.getGroup_id() + "." + selectedFood.getId(), foodMap);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }

                                addSelectedItemToLayout(selectedFood, Integer.parseInt(frequencyDetailed.getText().toString()));
                                popupWindowModify.dismiss();
                                popupWindow.dismiss();
                            }
                        });

                        denyFrequency.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                popupWindowModify.dismiss();
                                popupWindow.dismiss();
                            }
                        });

                    }
                });

                deleteFoodFrequency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSelectedItemToFirestore(selectedItem);
                        popupWindow.dismiss();
                    }
                });
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
            fetchCategoryItemsFromFirestore(food.getGroup_id());
            selectedItemsAdapter.updateData(selectedItemsList);
        } else {
            Toast.makeText(requireContext(), "Ya ha añadido esa comida", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSelectedItemToFirestore(Food food, int frequency) {
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("foodId", food.getId());
        foodMap.put("foodName", food.getFoodName());
        foodMap.put("groupId", food.getGroup_id());

        Map<String, Double> micronutrientMap = new HashMap<>();
        micronutrientMap.put("proteina_total",food.getMicronutrientes().getProteina_total());
        micronutrientMap.put("carbohidratos",food.getMicronutrientes().getCarbohidratos());
        micronutrientMap.put("fibra_total",food.getMicronutrientes().getFibra_total());
        micronutrientMap.put("azucares_totales",food.getMicronutrientes().getAzucares_totales());
        micronutrientMap.put("grasa_total",food.getMicronutrientes().getGrasa_total());
        micronutrientMap.put("ag_saturados_total",food.getMicronutrientes().getAg_saturados_total());
        micronutrientMap.put("ag_poliinsaturados_total",food.getMicronutrientes().getAg_poliinsaturados_total());
        micronutrientMap.put("ag_monoinsaturados_total",food.getMicronutrientes().getAg_monoinsaturados_total());
        micronutrientMap.put("ag_trans_total",food.getMicronutrientes().getAg_trans_total());
        micronutrientMap.put("colesterol",food.getMicronutrientes().getColesterol());
        micronutrientMap.put("sodio",food.getMicronutrientes().getSodio());
        micronutrientMap.put("potasio",food.getMicronutrientes().getPotasio());
        micronutrientMap.put("vitamina_a",food.getMicronutrientes().getVitamina_a());
        micronutrientMap.put("vitamina_c",food.getMicronutrientes().getVitamina_c());
        micronutrientMap.put("calcio",food.getMicronutrientes().getCalcio());
        micronutrientMap.put("hierro_total",food.getMicronutrientes().getHierro_total());

        foodMap.put("micronutrientes", micronutrientMap);
        foodMap.put("frequency", frequency);
        userRef.update("frequencyList." + food.getGroup_id() + "." + food.getId(), foodMap);
    }

    private void deleteSelectedItemToFirestore(Food food) {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> frequencyList = (Map<String, Object>) documentSnapshot.get("frequencyList");
                    if (frequencyList != null) {
                        Long groupId = food.getGroup_id();
                        if (frequencyList.containsKey(String.valueOf(groupId))) {
                            Map<String, Object> groupList = (Map<String, Object>) frequencyList.get(String.valueOf(groupId));
                            if (groupList != null && groupList.containsKey(String.valueOf(food.getId()))) {
                                groupList.remove(String.valueOf(food.getId()));
                                userRef.update("frequencyList." + groupId, groupList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                selectedItemsList.remove(food);
                                                fetchCategoryItemsFromFirestore(food.getGroup_id());
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });
    }

    private void fetchSearchResults(String query, FoodListAdapter adapter) {
        String apiUrl = "https://sanger.dia.fi.upm.es/foodnorm/foods/" + query;
        FoodListConnection connection = new FoodListConnection(null, adapter);
        connection.execute(apiUrl);
    }
}