package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.tfg.R;
import com.example.tfg.food.Food;
import com.example.tfg.food.FoodListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);

        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Buscar comida...");

        ListView listView = view.findViewById(R.id.listView_food);

        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(1, "Pizza", 285));
        foodList.add(new Food(2, "Burger", 354));
        foodList.add(new Food(3, "Pasta", 221));
        foodList.add(new Food(4, "Prueba1", 285));
        foodList.add(new Food(5, "Prueba2", 354));
        foodList.add(new Food(6, "Prueba3", 221));
        foodList.add(new Food(7, "Prueba4", 221));
        foodList.add(new Food(8, "Prueba5", 221));
        foodList.add(new Food(9, "Prueba6", 221));
        foodList.add(new Food(10, "Prueba7", 221));
        foodList.add(new Food(11, "Prueba8", 221));
        foodList.add(new Food(12, "Prueba9", 221));

        FoodListAdapter adapter = new FoodListAdapter(getActivity(), 8, foodList);
        listView.setAdapter(adapter);

        Button previousButton = view.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.goToPreviousPage();
            }
        });

        Button nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.goToNextPage();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Food food = adapter.getItem(position);
                String foodName = food.getFoodName();
                String foodCalories = String.valueOf(food.getKcal());

                // Create the detail fragment and add it to the activity
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, FoodDetailedFragment.newInstance(foodName, foodCalories))
                        .addToBackStack(null)
                        .commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    adapter.clearFilter();
                }
                return true;
            }
        });

        return view;
    }

}