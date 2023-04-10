package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText userRegister;
    private EditText passwordRegister;
    private EditText emailRegister;

    private Button registerButton;
    private TextView loginLink;

    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.button_register);
        loginLink = findViewById(R.id.textView_loginLink);

        userRegister = (EditText) findViewById(R.id.edit_UserRegister);
        emailRegister = (EditText) findViewById(R.id.edit_EmailRegister);
        passwordRegister = (EditText) findViewById(R.id.edit_PassRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        }

        void register(){
            String username = userRegister.getText().toString().trim();
            String email = emailRegister.getText().toString().trim();
            String password = passwordRegister.getText().toString().trim();
            if(username.isEmpty()){
                userRegister.setError("El nombre de usuario es requerido");
                userRegister.requestFocus();
                return;
            } else if(email.isEmpty()){
                emailRegister.setError("El correo electrónico es requerido");
                emailRegister.requestFocus();
                return;
            } else if(password.isEmpty()){
                passwordRegister.setError("La contraseña es requerida");
                passwordRegister.requestFocus();
                return;
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("users").document(userID);

                            Map<String,Object> user = new HashMap<>();
                            user.put("NombreUsuario", username);
                            user.put("CorreoElectronico", email);
                            user.put("Contrasena", password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: Datos registrados de " + userID);
                                }
                            });
                            Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }
}
