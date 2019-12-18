package com.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eshop.Admin.AdminMaintainProductActivity;
import com.eshop.Prevalent.Prevalent;
import com.eshop.adapter.ProductViewHolder;
import com.eshop.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){

            type = getIntent().getExtras().get("Admin").toString();
        }

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        iniViews();
        initDrawer();

    }

    private void iniViews() {
        toolbar = findViewById(R.id.Toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_VieW);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fab = findViewById(R.id.floating);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    startActivity(new Intent(getApplicationContext(), CartActivity.class));

                }

            }
        });


    }

    private void initDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);

        if (!type.equals("Admin")){
            username.setText(Prevalent.currentOnlineUser.getName());
//        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                productViewHolder.txtProdcutName.setText(products.getPname());
                productViewHolder.txtProductPrice.setText("Price = KES " +products.getPrice());
                productViewHolder.txtProductDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView);



                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type.equals("Admin")){

                            Intent intent = new Intent(getApplicationContext(), AdminMaintainProductActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);


                        }else {

                            Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);

                        }
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new ProductViewHolder(holder);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){

            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){

            case R.id.nav_Settings:

                if (!type.equals("Admin")) {
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                break;
            case R.id.nav_cart:

                if (!type.equals("Admin")) {
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                }

                break;
//
            case R.id.nav_search:

                if (!type.equals("Admin")) {

                    startActivity(new Intent(this, SearchProductsActivity.class));
                }

                break;

            case R.id.nav_logout:
                if (!type.equals("Admin")) {
                    Paper.book().destroy();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                break;

//            case R.id.nav_fixtures:
//                startActivity(new Intent(this, ScheduleActivity.class));
//                break;
//
//            case R.id.nav_donate:
//                startActivity(new Intent(this, Donate.class));
//                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

