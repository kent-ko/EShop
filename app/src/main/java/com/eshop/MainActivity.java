package com.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eshop.LoginRegister.LoginActivity;
import com.eshop.LoginRegister.RegisterActivity;
import com.eshop.Prevalent.Prevalent;
import com.eshop.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button joinNow, loginbtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNow = findViewById(R.id.main_join_now_btn);
        loginbtn = findViewById(R.id.main_login_btn);

        Paper.init(this);
        loadingBar = new ProgressDialog(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }
    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String parentDbName = "Users";
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {

                            Toast.makeText(getApplicationContext(), "Logged in Successfully...", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

                            Prevalent.currentOnlineUser = usersData;
                            startActivity(new Intent(getApplicationContext(), Home.class));

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
