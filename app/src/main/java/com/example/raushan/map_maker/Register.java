package com.example.raushan.map_maker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText reg_id,reg_pwd,reg_fstnm,reg_lstnm,reg_phon,reg_cnf_pwd;
    Button reg,bck;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        reg_id = (EditText)findViewById(R.id.register_id);
        reg_pwd = (EditText)findViewById(R.id.register_password);
        reg_fstnm = (EditText)findViewById(R.id.register_firstname);
        reg_lstnm = (EditText)findViewById(R.id.register_lastname);
        reg_phon = (EditText)findViewById(R.id.register_phoneno);
        reg_cnf_pwd = (EditText)findViewById(R.id.register_password_cnf);
        reg = (Button)findViewById(R.id.register_button);
        bck =(Button)findViewById(R.id.reg_btn);
        databaseReference = database.getInstance().getReference().child("profile");
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = reg_id.getText().toString();
                String pwd = reg_pwd.getText().toString();
                String pwd_cnf = reg_cnf_pwd.getText().toString();
                final String fst = reg_fstnm.getText().toString();
                final String lst = reg_lstnm.getText().toString();
                final String phn = reg_phon.getText().toString();
                if (pwd.equals(pwd_cnf)) {
                    mAuth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final DatabaseReference newPost = databaseReference.push();
                                newPost.child("FirstName").setValue(fst);
                                newPost.child("LastName").setValue(lst);
                                newPost.child("PhoneNo").setValue(phn);
                                newPost.child("Email").setValue(id);
                                //Intent to main activity

                                startActivity(new Intent(Register.this, MainActivity.class));
                            } else {
                                Toast.makeText(Register.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Register.this,"Password did not matched",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        });
    }
}
