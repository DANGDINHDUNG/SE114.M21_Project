package com.example.multiexpenserv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class new_expense_in extends AppCompatActivity {

    private EditText Amount,Description;
    private ImageView save,back,show_expenses;
    private Button DateButton;
    private DatePickerDialog datePicker;
    private TextView Date;
    private Spinner Type;

    private int finalDay, finalMonth, finalYear;

    Calendar myCalendar = java.util.Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense_in);

        //Getting elements by ID
        Type=findViewById(R.id.Type_holder);
        Amount=findViewById(R.id.Amount);
        Date=findViewById(R.id.Date);
        DateButton=findViewById(R.id.DateButton);
        Description=findViewById(R.id.Description);
        show_expenses=findViewById(R.id.show_expenses_btn);
        save=findViewById(R.id.save_btn);
        back=findViewById(R.id.back_btn);

        InitialSpinner();
        InitialDatePicker();

        //Show Expenses on click listnere
        show_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new_expense_in.this,Show_Expenses.class));
                finish();
            }
        });

        // On click listner for back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(new_expense_in.this,Home.class));
                finish();
            }
        });

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();

            }
        });

        // On click listner for the save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type,amount,day,month,year,description;
                boolean isBalanceConsistent=true;
                //Storing data from the edit text into the strings
                type=Type.getSelectedItem().toString();
                amount=Amount.getText().toString();
                description=Description.getText().toString();

                //Checking whether the data fields are filled
                if(!(type.isEmpty()||amount.isEmpty()||description.isEmpty())) {

                    //Declaring varibales
                    expense obj;
                    DataBaseHelper db;
                    boolean isSaved=false;

                    // Adding Shared Preferences
                    SharedPreferences sharedPreferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    String Orignal_Balance=sharedPreferences.getString("Current_Balance","");

                    //Subtracting Balance
                    int Balance_Integer = Integer.parseInt(Orignal_Balance);
                    String Amount_String = Amount.getText().toString();
                    if (Amount_String.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(v, "Please Fill the amount details !!!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {
                        int amount_t = Integer.parseInt(Amount_String);
                        if(Balance_Integer>=amount_t) {
                            Balance_Integer -= amount_t;
                            editor.putString("Current_Balance", Integer.toString(Balance_Integer));
                            //Creating object of expense and saving the data into it
                             obj = new expense(type, amount,
                                     Integer.toString(finalDay), Integer.toString(finalMonth), Integer.toString(finalYear),
                                     description);
                            //Creating database object
                             db = new DataBaseHelper(new_expense_in.this);
                            isSaved = db.addExpenseToDB(obj);
                            db.close();

                        }
                        else{
                            Toast.makeText(new_expense_in.this, "Insuffient Balance !", Toast.LENGTH_LONG).show();
                            isBalanceConsistent=false;
                        }
                    }
                        // Calling funtion to add the expense data

                    if (isSaved && isBalanceConsistent) {
                        editor.apply();
                        startActivity(new Intent(new_expense_in.this,Success.class));
                        finish();
                    }
                    else {
                        Snackbar snackbar=Snackbar.make(v,"Please Fill The Detaills Properly !",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                else {
                    //Giving warning to the user to fill all the details
                    Snackbar snackbar=Snackbar.make(v,"Please fill all the details !",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }



    void InitialSpinner()
    {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.SpendingTypes, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Type.setAdapter(adapter);
        Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void InitialDatePicker()
    {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                finalDay = dayOfMonth;
                finalMonth = monthOfYear + 1;
                finalYear = year;
                Date.setText("Date: " + dayOfMonth + "/" + month + "/" + year);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        finalDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        finalMonth = myCalendar.get(Calendar.MONTH) + 1;
        finalYear =myCalendar.get(Calendar.YEAR);
        Date.setText("Date: " + finalDay + "/" + finalMonth + "/" + finalYear);
        datePicker = new DatePickerDialog(this, style, datePickerListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

    }
}