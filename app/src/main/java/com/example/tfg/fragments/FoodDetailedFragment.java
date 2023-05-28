package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfg.R;

public class FoodDetailedFragment extends Fragment {

    private TextView mFoodName;
    private TextView category;
    private TextView proteina;
    private TextView carbos;
    private TextView fibra;
    private TextView azucar;
    private TextView grasas;
    private TextView agsat;
    private TextView agpoli;
    private TextView agmono;
    private TextView agtrans;
    private TextView coles;
    private TextView sodio;
    private TextView potasio;
    private TextView vitaminaa;
    private TextView vitaminac;
    private TextView calcio;
    private TextView hierro;
    //private TextView mFoodCalories;

    public FoodDetailedFragment() {
        // Required empty public constructor
    }

    public static FoodDetailedFragment newInstance(String foodName, String category, Double protein, Double carbos, Double fibra, Double azucar, Double grasas, Double agsat, Double agpoli, Double agmono,
                                                   Double agtrans, Double coles, Double sodio, Double potasio, Double vitaminaa, Double vitaminac, Double calcio, Double hierro) {
        FoodDetailedFragment fragment = new FoodDetailedFragment();
        Bundle args = new Bundle();
        args.putString("foodName", foodName);
        args.putString("category", category);
        args.putDouble("protein", protein);
        args.putDouble("carbos", carbos);
        args.putDouble("fibra", fibra);
        args.putDouble("azucar", azucar);
        args.putDouble("grasas", grasas);
        args.putDouble("agsat", agsat);
        args.putDouble("agpoli", agpoli);
        args.putDouble("agmono", agmono);
        args.putDouble("agtrans", agtrans);
        args.putDouble("coles", coles);
        args.putDouble("sodio", sodio);
        args.putDouble("potasio", potasio);
        args.putDouble("vitaminaa", vitaminaa);
        args.putDouble("vitaminac", vitaminac);
        args.putDouble("calcio", calcio);
        args.putDouble("hierro", hierro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_detailed, container, false);

        mFoodName = rootView.findViewById(R.id.foodName_detailed);
        category = rootView.findViewById(R.id.foodGroup);
        proteina = rootView.findViewById(R.id.foodProtein_detailed);
        carbos = rootView.findViewById(R.id.foodcarbs);
        fibra = rootView.findViewById(R.id.foodfiber);
        azucar = rootView.findViewById(R.id.foodsugar);
        grasas = rootView.findViewById(R.id.foodgrasa);
        agsat = rootView.findViewById(R.id.agsatu);
        agpoli = rootView.findViewById(R.id.agpoli);
        agmono = rootView.findViewById(R.id.agmono);
        agtrans = rootView.findViewById(R.id.agtrans);
        coles = rootView.findViewById(R.id.foodcolesterol);
        sodio = rootView.findViewById(R.id.foodsodio);
        potasio = rootView.findViewById(R.id.foodpotasio);
        vitaminaa = rootView.findViewById(R.id.foodvitaminaa);
        vitaminac = rootView.findViewById(R.id.foodvitaminac);
        calcio = rootView.findViewById(R.id.foodcalcio);
        hierro = rootView.findViewById(R.id.foodhierro);

        if (getArguments() != null) {
            mFoodName.setText(getArguments().getString("foodName"));
            category.setText(getArguments().getString("category"));
            proteina.setText(Double.toString(getArguments().getDouble("protein")));
            carbos.setText(Double.toString(getArguments().getDouble("carbos")));
            fibra.setText(Double.toString(getArguments().getDouble("fibra")));
            azucar.setText(Double.toString(getArguments().getDouble("azucar")));
            grasas.setText(Double.toString(getArguments().getDouble("grasas")));
            agsat.setText(Double.toString(getArguments().getDouble("agsat")));
            agpoli.setText(Double.toString(getArguments().getDouble("agpoli")));
            agmono.setText(Double.toString(getArguments().getDouble("agmono")));
            agtrans.setText(Double.toString(getArguments().getDouble("agtrans")));
            coles.setText(Double.toString(getArguments().getDouble("coles")));
            sodio.setText(Double.toString(getArguments().getDouble("sodio")));
            potasio.setText(Double.toString(getArguments().getDouble("potasio")));
            vitaminaa.setText(Double.toString(getArguments().getDouble("vitaminaa")));
            vitaminac.setText(Double.toString(getArguments().getDouble("vitaminac")));
            calcio.setText(Double.toString(getArguments().getDouble("calcio")));
            hierro.setText(Double.toString(getArguments().getDouble("hierro")));
        }

        return rootView;
    }
}