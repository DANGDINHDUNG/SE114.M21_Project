package com.example.multiexpenserv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;

public class Home extends AppCompatActivity {

    private TextView name,balance;
    private  String fname;
    private String cbalance;
    private ImageView share,newexpense,Balance_in,Goals,Gifts;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPreferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        InitialAd();

        //Accessing the text view by id from backend
        name=findViewById(R.id.Welcome);
        balance=findViewById(R.id.Home_Balance);
        share=findViewById(R.id.share);
        newexpense=findViewById(R.id.newexpense_button);
        Balance_in=findViewById(R.id.Balance_button);
        Goals=findViewById(R.id.Goals_button);
        Gifts=findViewById(R.id.Gifts_Button);
        //Getting the user data from the sharedPreferences
        fname=sharedPreferences.getString("First_Name","");
        cbalance=sharedPreferences.getString("Current_Balance","");

        // Setting textview on the home screen
        name.setText("Welcome "+fname);
        balance.setText("RS "+cbalance);

        //Share activity
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Download MultiExpenser ");
                intent.putExtra(Intent.EXTRA_TEXT, "Regards of the day , try this amazing app to save your money named as MultiExpenser. Download it from the playStore");
                startActivity(Intent.createChooser(intent, "choose one"));

            }
        });

        //New expense activity
        newexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAd();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Home.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(new Intent(Home.this,new_expense_in.class));
                            mInterstitialAd=null;
                            LoadAd();
                        }
                    });
                } else {
                    startActivity(new Intent(Home.this,new_expense_in.class));
                }
            }
        });

        //Balance Activity
        Balance_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, com.example.multiexpenserv1.Balance_in.class));
            }
        });

        //Goals Activity
        Goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoadAd();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Home.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(new Intent(Home.this,New_Goal.class));
                            mInterstitialAd=null;
                            LoadAd();
                        }
                    });
                } else {
                    startActivity(new Intent(Home.this,New_Goal.class));
                }
            }
        });

        //Gifts Activity
        Gifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoadAd();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Home.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(new Intent(Home.this,Gifts_in.class));
                            mInterstitialAd=null;
                            LoadAd();
                        }
                    });
                } else {
                    startActivity(new Intent(Home.this,Gifts_in.class));
                }
            }
        });
    }

    void InitialAd()
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    void LoadAd()
    {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,getString(R.string.AdUnit), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}