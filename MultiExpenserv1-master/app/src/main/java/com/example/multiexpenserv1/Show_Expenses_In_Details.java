package com.example.multiexpenserv1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Show_Expenses_In_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses_in_details);
        //Getting Data from the intent
        Intent intent=getIntent();
        expense obj = (expense) intent.getSerializableExtra(ShowExpensesAdapter.ShowExpenseKey);
        String Title=intent.getStringExtra(ShowExpensesAdapter.ShowExpenseTitleKey);
        String Amount=intent.getStringExtra(ShowExpensesAdapter.ShowExpenseAmountKey);
        String Date=intent.getStringExtra(ShowExpensesAdapter.ShowExpenseDateKey);
        String Description=intent.getStringExtra(ShowExpensesAdapter.ShowExpenseDescriptionKey);

        //Getting Elements from xml
        TextView title = findViewById(R.id.Title_Show_Expenses_in_Detail);
        TextView amount = findViewById(R.id.Amount_show_expsense_in_details);
        TextView date = findViewById(R.id.Date_show_Expenses_in_details);
        TextView desc=findViewById(R.id.Description_show_expenses_in_detail);
        ImageView back=findViewById(R.id.back_Show_expenses_in_detail);
        ImageView delete = findViewById(R.id.delete_btn);

        //Exporting Data
        title.setText(Title);
        amount.setText(Amount);
        date.setText(Date);
        desc.setText(Description);

        //Setting on click listner for back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DataBaseHelper db = new DataBaseHelper(Show_Expenses_In_Details.this);

                                //refund
                                SharedPreferences sharedPreferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();

                                String Orignal_Balance=sharedPreferences.getString("Current_Balance","");
                                int Balance_Integer = Integer.parseInt(Orignal_Balance);
                                String Amount_String = obj.getAmount();
                                int amount_t = Integer.parseInt(Amount_String);
                                Balance_Integer += amount_t;
                                editor.putString("Current_Balance", Integer.toString(Balance_Integer));
                                editor.apply();

                                //remove
                                boolean isSaved = db.removeExpenseFromDB(obj);
                                db.close();
                                if (isSaved)
                                {
                                    startActivity(new Intent(Show_Expenses_In_Details.this,Success.class));
                                }
                                //startActivity(new Intent(Show_Expenses_In_Details.this,Show_Expenses.class));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                builder.setMessage("Are you sure to remove this expense and refund the money amount?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
}