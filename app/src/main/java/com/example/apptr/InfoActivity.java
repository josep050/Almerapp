package com.example.apptr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class InfoActivity extends AppCompatActivity {


    private TextInputLayout codi;
    private TextInputEditText codi1;
    private FloatingActionButton fab;

    private TextView Username,Email, Class, Name;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String uid;

    CircleImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Informació");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Name = (TextView)findViewById(R.id.name);
        Class = (TextView) findViewById(R.id.classe);
        imgview = findViewById(R.id.imageView);
        Username = findViewById(R.id.username);

        Email = (TextView) findViewById(R.id.email);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("InfoUsuaris").child(uid);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_LONG_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_LONG_YES:

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String nom = dataSnapshot.child("nom").getValue(String.class);
                            String classe = dataSnapshot.child("classe").getValue(String.class);
                            String imgurl = dataSnapshot.child("imageurl").getValue(String.class);
                            String email = user.getEmail();

                            Name.setText(nom);
                            Class.setText(classe);
                            Email.setText(email);
                            Username.setText(nom);
                            Picasso.get().load(imgurl).into(imgview);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case Configuration.SCREENLAYOUT_LONG_NO:
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String nom = dataSnapshot.child("nom").getValue(String.class);
                            String classe = dataSnapshot.child("classe").getValue(String.class);

                            Name.setText(nom);
                            Class.setText(classe);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            default:
                break;
        }


        codi = findViewById(R.id.textCode);
        codi1 = findViewById(R.id.txtfield_codi);
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Codi = codi.getEditText().getText().toString().trim();
                if (Codi.equals("2nADMIN")){
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            myRef.child("admin").setValue("true");
                            Toasty.success(InfoActivity.this, "Ara ets administrador", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toasty.error(InfoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                hideFAB();
            }
        });

        codi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealFAB();
            }
        });

    }
    private void revealFAB() {
        View view = findViewById(R.id.fab);
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void hideFAB() {
        final View view = findViewById(R.id.fab);
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;
        float initialRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }



}