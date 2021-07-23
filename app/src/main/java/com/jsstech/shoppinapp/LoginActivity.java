package com.jsstech.shoppinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsstech.shoppinapp.DB.TableKeys;
import com.jsstech.shoppinapp.Models.Users;

public class LoginActivity extends AppCompatActivity {
    private EditText login_phone, login_pass;
    private Button login_button;
    private CheckBox remember_me;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_phone = findViewById(R.id.etloginPhone);
        login_pass = findViewById(R.id.etlogin_pass);
        remember_me = findViewById(R.id.checkRemember);
        loadingBar = new ProgressDialog(this);
        login_button = findViewById(R.id.alreadylogin);
        Paper.init(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

    }

    private void logIn() {

        String phone = login_phone.getText().toString();
        String password = login_pass.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this,"Please write phone number...",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please write password...",Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Login Account...");
            loadingBar.setMessage("Please wait, while we creating the credientials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            permissionToAccessAccount(phone,password);

        }
    }

    private void permissionToAccessAccount(String phone,String password) {

        if (remember_me.isChecked()){
            Paper.book().write(TableKeys.UserPhoneKey,phone);
            Paper.book().write(TableKeys.UserPasswordKey,password);

        }
        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists()){
                    Users userData = snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Loggin successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password in incorrect...", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with "+ phone +" number do not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}