package com.eshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eshop.Prevalent.Prevalent;
import com.eshop.adapter.CartViewHolder;
import com.eshop.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessButton;
    private TextView txtTotalAmount, txtMsg;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.card_list);
        txtTotalAmount = findViewById(R.id.total_price);
        NextProcessButton = findViewById(R.id.next_process_btn);
        txtMsg = findViewById(R.id.msg1);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        NextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ConfirmFinalOrder.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class)
                                .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                        cartViewHolder.txtProductName.setText("Item " +cart.getPname());
                        cartViewHolder.txtProductQuantity.setText("Quantity " +cart.getQuantity());
                        cartViewHolder.txtProductPrice.setText("Price " +cart.getPrice());

                        int oneTypeProductPrice = ((Integer.valueOf(cart.getPrice())) * (Integer.valueOf(cart.getQuantity())));
                        overTotalPrice = overTotalPrice + oneTypeProductPrice;
                        txtTotalAmount.setText("Total Price = KES " +String.valueOf(overTotalPrice));

                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]
                                        {

                                                "Edit",
                                                "Remove"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options: ");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (which == 0){
                                            Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                            intent.putExtra("pid", cart.getPid());
                                            startActivity(intent);
                                        }
                                        if (which == 1){

                                            cartListRef.child("User View")
                                                    .child(Prevalent.currentOnlineUser.getPhone())
                                                    .child("Products")
                                                    .child(cart.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()){

                                                                Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(getApplicationContext(), Home.class));
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_list, parent , false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String username = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){

//                        txtTotalAmount.setText("Total Amount = KES "+overTotalPrice);
                        txtTotalAmount.setText("Dear "+username+ "\n order has been shipped successfully");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg.setVisibility(View.VISIBLE);
                        txtMsg.setText("Congratulations, your final order has been placed successfully. Soon you will receive your order");
                        NextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "You can purchase , once  you received your first final order", Toast.LENGTH_LONG).show();
                    }
                    else if(shippingState.equals("not shipped")){


                        txtTotalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg.setVisibility(View.VISIBLE);
                        NextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "You can purchase , once  you received your first final order", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}