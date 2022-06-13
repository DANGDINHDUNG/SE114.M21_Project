package com.example.multiexpenserv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private BarChart barChart;
    private ImageView Back;
    private Spinner Period;

    private String chosenPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        barChart=findViewById(R.id.BarChart);
        Back=findViewById(R.id.back_btn_balance);
        Period=findViewById(R.id.Period_spinner);

        InitialSpinner();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this,Home.class));
                finish();
            }
        });

        ShowChart(data());
    }

    void InitialSpinner()
    {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.TimePeriod, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Period.setAdapter(adapter);
        Period.setSelection(3);
        Period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenPeriod = (String) adapterView.getItemAtPosition(i);
                ShowExpensesInPeriod();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void ShowExpensesInPeriod()
    {
        switch ( chosenPeriod ) {
            case  "Today":
                ShowChart(GetExpensesToday());
                break;
            case  "This week":
                ShowChart(GetExpensesThisWeek());
                break;
            case  "This month":
                ShowChart(GetExpensesThisMonth());
                break;
            case  "All time":
                ShowChart(data());
                break;
            default:
        }
    }

    List<expense> GetExpensesToday()
    {
        List<expense> results = new ArrayList<>();
        Calendar myCalendar = java.util.Calendar.getInstance();


        for (expense thing: data()) {
            int day = Integer.parseInt(thing.getDay());
            int month = Integer.parseInt(thing.getMonth());
            int year = Integer.parseInt(thing.getYear());
            if (day==myCalendar.get(Calendar.DAY_OF_MONTH)
                    && month==(myCalendar.get(Calendar.MONTH) + 1)
                    && year==myCalendar.get(Calendar.YEAR))
                results.add(thing);
        }

        return results;
    }

    List<expense> GetExpensesThisWeek()
    {
        List<expense> results = new ArrayList<expense>();
        Calendar myCalendar = java.util.Calendar.getInstance();
        Calendar temp = java.util.Calendar.getInstance();

        for (expense thing: data()) {
            int day = Integer.parseInt(thing.getDay());
            int month = Integer.parseInt(thing.getMonth()) - 1;
            int year = Integer.parseInt(thing.getYear());
            temp.set(year, month, day);
            int week = temp.get(Calendar.WEEK_OF_YEAR);
            if (week==myCalendar.get(Calendar.WEEK_OF_YEAR)
                    && year==myCalendar.get(Calendar.YEAR))
                results.add(thing);
        }

        return results;
    }

    List<expense> GetExpensesThisMonth()
    {
        List<expense> results = new ArrayList<expense>();
        Calendar myCalendar = java.util.Calendar.getInstance();

        for (expense thing: data()) {
            int month = Integer.parseInt(thing.getMonth());
            int year = Integer.parseInt(thing.getYear());
            if (month==(myCalendar.get(Calendar.MONTH) + 1)
                    && year==myCalendar.get(Calendar.YEAR))
                results.add(thing);
        }

        return results;
    }

    List<expense> data()
    {
        DataBaseHelper db = new DataBaseHelper(MainActivity2.this);
        List<expense> results = db.getAllExpenses();
        db.close();
        return results;
    }

    void ShowChart(List<expense> expenses)
    {
        int LivingExpenses, Food, SD, Leisure, Clothes, Hobby, Taxes, Others;
        LivingExpenses = 0;
        Food=0;
        SD=0;
        Leisure=0;
        Clothes=0;
        Hobby=0;
        Taxes=0;
        Others=0;
        for (expense thing: expenses) {
            switch (thing.getTitle()){
                case "LivingExpenses":
                    LivingExpenses+=Integer.parseInt(thing.getAmount());
                    break;
                case "Food":
                    Food+=Integer.parseInt(thing.getAmount());
                    break;
                case "Snacks/Drinks":
                    SD+=Integer.parseInt(thing.getAmount());
                    break;
                case "Leisure":
                    Leisure+=Integer.parseInt(thing.getAmount());
                    break;
                case "Clothes":
                    Clothes+=Integer.parseInt(thing.getAmount());
                    break;
                case "Hobby":
                    Hobby+=Integer.parseInt(thing.getAmount());
                    break;
                case "Taxes":
                    Taxes+=Integer.parseInt(thing.getAmount());
                    break;
                case "Others":
                    Others+=Integer.parseInt(thing.getAmount());
                    break;
                default:
                    break;
            }
        }

        ArrayList<BarEntry> bars = new ArrayList<>();
        bars.add(new BarEntry(0f, LivingExpenses));
        bars.add(new BarEntry(1f, Food));
        bars.add(new BarEntry(2f, SD));
        bars.add(new BarEntry(3f, Leisure));
        bars.add(new BarEntry(4f, Clothes));
        bars.add(new BarEntry(5f, Hobby));
        bars.add(new BarEntry(6f, Taxes));
        bars.add(new BarEntry(7f, Others));

        BarDataSet bds = new BarDataSet(bars, "Số tiền đã tiêu");
        bds.setColors(ColorTemplate.COLORFUL_COLORS);

        String[] labelNames=new String[]{
                "LivingExpenses", "Food", "Snack&Drinks", "Leisure", "Clothes", "Hobby", "Taxes", "Others"
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(300);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));

        BarData data = new BarData(bds);
        data.setBarWidth(0.9f);
        barChart.setDrawBorders(false);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setText("");
        barChart.invalidate();
    }
}