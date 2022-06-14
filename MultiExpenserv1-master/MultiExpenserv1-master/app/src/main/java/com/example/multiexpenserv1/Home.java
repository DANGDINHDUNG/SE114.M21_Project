package com.example.multiexpenserv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.w3c.dom.Text;

public class Home extends AppCompatActivity {

    private TextView name,balance;
    private  String fname;
    private String cbalance;
    private ImageView share,newexpense,Balance_in,Goals,Gifts;
    private ImageView graph, history;
    private InterstitialAd mInterstitialAd;
    private TextView reset;
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
        reset=findViewById(R.id.Reset);
        newexpense=findViewById(R.id.newexpense_button);
        Balance_in=findViewById(R.id.Balance_button);
        Goals=findViewById(R.id.Goals_button);
        Gifts=findViewById(R.id.Gifts_Button);
        graph=findViewById(R.id.Graphs);
        history=findViewById(R.id.Expenses_Btn);
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

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,MainActivity2.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Show_Expenses.class));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                DataBaseHelper db = new DataBaseHelper(Home.this);
                                db.Reset();
                                db.close();
                                startActivity(new Intent(Home.this,MainActivity.class));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                builder.setMessage("Are you sure to reset all data?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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