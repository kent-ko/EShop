package com.eshop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.eshop.Home;
import com.eshop.LoginRegister.LoginActivity;
import com.eshop.R;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView tShirts, sportsTshirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, walletsBags, shoes;
    private ImageView headPhonesHandsFree, laptops, watches,mobilePhones;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        LogoutBtn = findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = findViewById(R.id.check_order_btn);
        maintainProductBtn = findViewById(R.id.maintain_btn);

        tShirts = findViewById(R.id.t_shirts);
        sportsTshirts = findViewById(R.id.sports_t_shirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweaters = findViewById(R.id.sweaters);

        glasses = findViewById(R.id.glasses);
        hatsCaps = findViewById(R.id.hats_cap);
        walletsBags = findViewById(R.id.purses_bags_wallets);
        shoes = findViewById(R.id.shoes);

        headPhonesHandsFree = findViewById(R.id.headphones_handfree);
        laptops = findViewById(R.id.laptop_pc);
        watches = findViewById(R.id.watches);
        mobilePhones = findViewById(R.id.mobilephones);

        maintainProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, Home.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });


        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "tshirts");
                startActivity(intent);
            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "SportsTshirts");
                startActivity(intent);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Female Dresses");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Hat Caps");
                startActivity(intent);
            }
        });
        walletsBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Wallets, Bags Purses");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });
        headPhonesHandsFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Headphones HandsFree");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Watches");
                startActivity(intent);
            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });

    }
}
