package com.pdm.sagaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEditName;
    private EditText mEditEmail;
    private EditText mEditPassword;
    private Button mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEditName = findViewById(R.id.edTxt_Name);
        mEditEmail = findViewById(R.id.edTxt_Email);
        mEditPassword = findViewById(R.id.edTxt_Password);
        mBtnSignUp = findViewById(R.id.btn_SignUp);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Teste", "Funciona!");
            }
        });
    }

    private void createUser() {
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(this, "O Email e a Senha devem ser preencindos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("Teste", task.getResult().getUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });
    }
}

