package com.jsstech.shoppinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JoinUsActivity extends AppCompatActivity {
    EditText inputName,inputPhone,inputPass;
    Button buttonRegister;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        inputName=findViewById(R.id.edtuserName);
        inputPhone=findViewById(R.id.edtPhoneNo);
        inputPass=findViewById(R.id.edtPassword);
        buttonRegister=findViewById(R.id.register_buttn);
        loadingBar = new ProgressDialog(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String name=inputName.getText().toString().trim();
        String phone=inputPhone.getText().toString().trim();
        String password=inputPass.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account...");
            loadingBar.setMessage("Please wait, while we checking credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name, phone, password);
        }




    }

    private void ValidatePhoneNumber(final String name,final String phone,final String password) {

    final DatabaseReference databaseReference;
    databaseReference= FirebaseDatabase.getInstance().getReference();

    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.child("Users").child(phone).exists()) {
                HashMap<String, Object> userdatamap = new HashMap<>();
                userdatamap.put("phone",phone);
                userdatamap.put("password",password);
                userdatamap.put("name",name);

                databaseReference.child("Users").child(phone).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(JoinUsActivity.this,"Congratulations, your account has been created.",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(JoinUsActivity.this,LoginActivity.class);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(JoinUsActivity.this,"Ntework Error : Please try again after some time...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {

                Toast.makeText(JoinUsActivity.this,"This " + phone + " already exists.",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Toast.makeText(JoinUsActivity.this,"Please try again with another number",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JoinUsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }
}