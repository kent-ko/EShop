package com.eshop.LoginRegister;

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

import com.eshop.MainActivity;
import com.eshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputPhoneNumber, inputPassword;
    private Button createAccountButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = findViewById(R.id.register_btn);
        inputName = findViewById(R.id.register_username_input);
        inputPhoneNumber = findViewById(R.id.register_phone_number_input);
        inputPassword = findViewById(R.id.register_password_input);

        progressDialog = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = inputName.getText().toString().trim();
        String phone = inputPhoneNumber.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your name...",  Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please write your phone number...",  Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please input your password...",  Toast.LENGTH_SHORT).show();
        }else{

            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please Wait, Checking Credentials...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            validatephonenNumber(name, phone, password);
        }
    }

    private void validatephonenNumber(final String name, final String phone, final String password) {

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Created acc Successfully", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Network Error, try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this, "The"+phone+"already exist",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try with another phone number",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
