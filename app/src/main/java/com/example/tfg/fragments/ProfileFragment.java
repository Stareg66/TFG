package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.food.Food;
import com.example.tfg.food.Micronutrients;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    //User
    private String userId;
    private DocumentReference userRef;

    Double totalProtein = 0.0;
    Double totalCarbs = 0.0;
    Double totalFiber = 0.0;
    Double totalSugar = 0.0;
    Double totalFat = 0.0;
    Double totalAgSat = 0.0;
    Double totalAgPoli = 0.0;
    Double totalAgMono = 0.0;
    Double totalAgTrans = 0.0;
    Double totalColesterol = 0.0;
    Double totalSodium = 0.0;
    Double totalPotasium = 0.0;
    Double totalVitaminA = 0.0;
    Double totalVitaminC = 0.0;
    Double totalCalcium = 0.0;
    Double totalIron = 0.0;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        /*
        totalProtein = 0.0;
        totalCarbs = 0.0;
        totalFiber = 0.0;
        totalSugar = 0.0;
        totalFat = 0.0;
        totalAgSat = 0.0;
        totalAgPoli = 0.0;
        totalAgMono = 0.0;
        totalAgTrans = 0.0;
        totalColesterol = 0.0;
        totalSodium = 0.0;
        totalPotasium = 0.0;
        totalVitaminA = 0.0;
        totalVitaminC = 0.0;
        totalCalcium = 0.0;
        totalIron = 0.0;*/

        Button showRecommendations = view.findViewById(R.id.buttonRecommendation);
        TextView recommendationText = view.findViewById(R.id.textRecommended);

        showRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateTotalNutrients().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String totalNutrients = calculateRecommendation();
                        recommendationText.setText(totalNutrients);
                    }
                });
            }
        });



        return view;
    }

    private Task<Void> calculateTotalNutrients() {
        TaskCompletionSource<Void> completionSource = new TaskCompletionSource<>();
        List<Task<?>> tasks = new ArrayList<>();

        tasks.add(calculateNutrients("selectedItemsMorning1Day"));
        tasks.add(calculateNutrients("selectedItemsLunch1Day"));
        tasks.add(calculateNutrients("selectedItemsDinner1Day"));
        tasks.add(calculateNutrients("selectedItemsMorning2Day"));
        tasks.add(calculateNutrients("selectedItemsLunch2Day"));
        tasks.add(calculateNutrients("selectedItemsDinner2Day"));
        tasks.add(calculateNutrients("selectedItemsMorning3Day"));
        tasks.add(calculateNutrients("selectedItemsLunch3Day"));
        tasks.add(calculateNutrients("selectedItemsDinner3Day"));

        Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    completionSource.setResult(null);
                } else {
                    completionSource.setException(task.getException());
                }
            }
        });

        return completionSource.getTask();
    }

    private Task<Void> calculateNutrients(String selectedTable){
        TaskCompletionSource<Void> completionSource = new TaskCompletionSource<>();

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map<String, Object> selectedItemsMap = (Map<String, Object>) documentSnapshot.get(selectedTable);
                if (selectedItemsMap != null) {
                    List<Food> selectedItems = new ArrayList<>();

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

                        totalProtein += checkNull(micronutrients.getProteina_total());
                        totalCarbs += checkNull(micronutrients.getCarbohidratos());
                        totalFiber += checkNull(micronutrients.getFibra_total());
                        totalSugar += checkNull(micronutrients.getAzucares_totales());
                        totalFat += checkNull(micronutrients.getGrasa_total());
                        totalAgSat += checkNull(micronutrients.getAg_saturados_total());
                        totalAgMono += checkNull(micronutrients.getAg_monoinsaturados_total());
                        totalAgTrans += checkNull(micronutrients.getAg_trans_total());
                        totalColesterol += checkNull(micronutrients.getColesterol());
                        totalSodium += checkNull(micronutrients.getSodio());
                        totalPotasium += checkNull(micronutrients.getPotasio());
                        totalVitaminA += checkNull(micronutrients.getVitamina_a());
                        totalVitaminC += checkNull(micronutrients.getVitamina_c());
                        totalCalcium += checkNull(micronutrients.getCalcio());
                        totalIron += checkNull(micronutrients.getHierro_total());

                    }
                }
                completionSource.setResult(null);
            }
        });

        return completionSource.getTask();
    }

    public Double checkNull(Double nutrient){
        if(nutrient == null){
            return 0.0;
        } else {
            return nutrient;
        }
    }

    private String calculateRecommendation() {
        StringBuilder recommendations = new StringBuilder();
        if(totalProtein < 100){
            recommendations.append("Necesitas consumir mas proteina.\n");
        }
        if(totalCarbs < 200){
            recommendations.append("Necesitas consumir mas carbohidratos.\n");
        }
        if(totalFiber < 150){
            recommendations.append("Necesitas consumir mas fibra.\n");
        }
        if(totalSugar < 10){
            recommendations.append("Necesitas consumir mas azucar.\n");
        }
        if(totalFat < 10){
            recommendations.append("Necesitas consumir mas grasas.\n");
        }
        if(totalAgSat < 10){
            recommendations.append("Necesitas consumir mas ácidos grasos saturados.\n");
        }
        if(totalAgPoli < 200){
            recommendations.append("Necesitas consumir mas ácidos grasos poliinstaurados.\n");
        }
        if(totalAgMono < 200){
            recommendations.append("Necesitas consumir mas ácidos grasos monoinstaurados.\n");
        }
        if(totalAgTrans < 100){
            recommendations.append("Necesitas consumir mas ácidos grasos trans.\n");
        }
        if(totalColesterol < 10){
            recommendations.append("Necesitas consumir mas colesterol.\n");
        }
        if(totalSodium < 100){
            recommendations.append("Necesitas consumir mas sodio.\n");
        }
        if(totalPotasium < 300){
            recommendations.append("Necesitas consumir mas potasio.\n");
        }
        if(totalVitaminA < 100){
            recommendations.append("Necesitas consumir mas vitamina A.\n");
        }
        if(totalVitaminC < 150){
            recommendations.append("Necesitas consumir mas vitamina C.\n");
        }
        if(totalCalcium < 200){
            recommendations.append("Necesitas consumir mas cálcio.\n");
        }
        if(totalIron < 100){
            recommendations.append("Necesitas consumir mas hierro.\n");
        }

        return recommendations.toString();
    }
}