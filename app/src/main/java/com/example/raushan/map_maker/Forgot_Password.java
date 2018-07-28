package com.example.raushan.map_maker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {
    EditText em;
    Button smt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        em = (EditText)findViewById(R.id.reset_email);
        smt = (Button)findViewById(R.id.res_button);
        smt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eml = em.getText().toString();
                FirebaseAuth.getInstance().sendPasswordResetEmail(eml).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Forgot_Password.this,"Email have sent to your mail",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Forgot_Password.this,MainActivity.class));
                        }
                    }
                });
            }
        });
    }
}
