package com.seemantshekhar.shoppingmenuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class OffersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.nav_home:{
                        Toast.makeText(OffersActivity.this, "Dashboard is Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OffersActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    }

                    case R.id.nav_message:{
                        Toast.makeText(OffersActivity.this, "Cart is Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OffersActivity.this, CartActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.synch: {
                        Toast.makeText(OffersActivity.this, "Offers is Clicked", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(OffersActivity.this, OffersActivity.class);
//                        startActivity(intent);
                        break;
                    }
                    case R.id.trash:{
                        Toast.makeText(OffersActivity.this, "Orders is Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OffersActivity.this, OffersActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.settings: {
                        Toast.makeText(OffersActivity.this, "Settings is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OffersActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_login:
                        Toast.makeText(OffersActivity.this, "Login is Clicked",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_share:
                        Toast.makeText(OffersActivity.this, "Share is clicked",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_rate:
                        Toast.makeText(OffersActivity.this, "Rate us is Clicked",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;

                }
                return true;
            }
        });


    }
}