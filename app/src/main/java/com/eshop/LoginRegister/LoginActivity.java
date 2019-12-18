package com.eshop.LoginRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eshop.Admin.AdminCategoryActivity;
import com.eshop.Home;
import com.eshop.Prevalent.Prevalent;
import com.eshop.R;
import com.eshop.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button Loginbtn;
    private EditText inputPassword, inputPhoneNumber;
    private ProgressDialog loadingBar;
    private CheckBox chkBox;
    private TextView Adminlink, notAdmin;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        Loginbtn = findViewById(R.id.login_btn);
        inputPassword = findViewById(R.id.login_password_input);
        inputPhoneNumber = findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);
        chkBox = findViewById(R.id.remember_me_chkb);
        Adminlink = findViewById(R.id.admin_panel_link);
        notAdmin = findViewById(R.id.not_admin_panel_link);


        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUsers();
            }
        });

        Adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login Admin");
                Adminlink.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login");
                Adminlink.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void LoginUsers() {

        String phone = inputPhoneNumber.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAcc(phone, password);
        }
    }

    private void AllowAccessToAcc(final String phone, final String password) {

        if (chkBox.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);


                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {

                           if (parentDbName.equals("Admins")){

                               Toast.makeText(getApplicationContext(),"Logged in Successfully Admin...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                               Prevalent.currentOnlineUser = usersData;
                               startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(), Home.class));


                           } else if (parentDbName.equals("Users")){


                               Toast.makeText(getApplicationContext(),"Logged in Successfully...", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, Home.class);
                               Prevalent.currentOnlineUser = usersData;
                               startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(), Home.class));
                           }



                        }
                        else
                            {

                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "The user with " + phone + " does not exist", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "You can create a New Account", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
