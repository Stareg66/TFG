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
import com.example.tfg.food.FoodListConnection;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private ListView listView;
    private FoodListAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);

        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Buscar comida...");

        listView = view.findViewById(R.id.listView_food);
        adapter = new FoodListAdapter(getActivity(), 8, new ArrayList<>());
        listView.setAdapter(adapter);

        FoodListConnection connection = new FoodListConnection(this, adapter);
        connection.execute("https://sanger.dia.fi.upm.es/foodnorm/foods");

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
                String category = checkCategory(food.getGroup_id());

                Double protein = checkNull(food.getMicronutrientes().getProteina_total());
                Double carbos = checkNull(food.getMicronutrientes().getCarbohidratos());
                Double fibra = checkNull(food.getMicronutrientes().getFibra_total());
                Double azucar = checkNull(food.getMicronutrientes().getAzucares_totales());
                Double grasas = checkNull(food.getMicronutrientes().getGrasa_total());
                Double agsat = checkNull(food.getMicronutrientes().getAg_saturados_total());
                Double agpoli = checkNull(food.getMicronutrientes().getAg_poliinsaturados_total());
                Double agmono = checkNull(food.getMicronutrientes().getAg_monoinsaturados_total());
                Double agtrans = checkNull(food.getMicronutrientes().getAg_trans_total());
                Double coles = checkNull(food.getMicronutrientes().getColesterol());
                Double sodio = checkNull(food.getMicronutrientes().getSodio());
                Double potasio = checkNull(food.getMicronutrientes().getPotasio());
                Double vitaminaa = checkNull(food.getMicronutrientes().getVitamina_a());
                Double vitaminac = checkNull(food.getMicronutrientes().getVitamina_c());
                Double calcio = checkNull(food.getMicronutrientes().getCalcio());
                Double hierro = checkNull(food.getMicronutrientes().getHierro_total());

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, FoodDetailedFragment.newInstance(foodName, category, protein, carbos, fibra, azucar, grasas, agsat, agpoli, agmono, agtrans, coles,
                                sodio, potasio, vitaminaa, vitaminac, calcio, hierro))
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

    private String checkCategory(Long group_id) {
        String category = "";
        if(group_id==1){
            category="Leche y productos lácteos";
        } else if(group_id==2){
            category="Huevos y productos con huevo";
        } else if(group_id==3){
            category="Carne y productos cárnicos";
        } else if(group_id==4){
            category="Pescados, moluscos, reptiles y crustáceos";
        } else if(group_id==5){
            category="Grasas y aceites";
        } else if(group_id==6){
            category="Granos y productos con grano";
        } else if(group_id==7){
            category="Semillas, nueces y otros productos";
        } else if(group_id==8){
            category="Vegetales y productos con vegetales";
        } else if(group_id==9){
            category="Frutas y productos con fruta";
        } else if(group_id==10){
            category="Azucar, chocolate y otros productos relacionados";
        } else if(group_id==11){
            category="Bebidas (sin leche)";
        } else if(group_id==12){
            category="Comidas varias";
        }
        return category;
    }

    public Double checkNull(Double nutrient){
        if(nutrient == null){
            return 0.0;
        } else {
            return nutrient;
        }
    }
}