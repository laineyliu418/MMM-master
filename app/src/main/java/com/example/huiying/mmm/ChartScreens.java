package com.example.huiying.mmm;

/**
 * Created by huiying on 4/21/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartScreens extends AppCompatActivity {

    private static String TAG = "Pie Chart";


    private List<Float> expense = new ArrayList<>();
    private List<String> category = new ArrayList<>();
    private TextView[] amountViews = new TextView[9];
    private String[] cateID = {
            "eatAmount","educationAmount","clothesAmount","travelAmount",
            "socialAmount","healthAmount","transportAmount","homeAmount","mobileAmount"};
    PieChart pieChart;
    SQLiteDatabase db;
    private String totalExp;


    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        Log.d(TAG,"OnCreate: starting to create a chart ");

        //Database
        db = openOrCreateDatabase("Account", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS account" +
                "(id integer primary key, type VARCHAR, amount VARCHAR,note VARCHAR, date VARCHAR);");


        pieChart = (PieChart) findViewById(R.id.idPieChart);

        //find textView
        int temp;
        for (int i=0; i<cateID.length; i++){
            temp = getResources().getIdentifier(cateID[i], "id", getPackageName());
            amountViews[i] = (TextView)findViewById(temp);
        }

        //Properties of the chart
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Total Expense");
        pieChart.setCenterTextSize(10);
        pieChart.getDescription().setText(" ");

        //Get total Expense
        Cursor cTotal = db.rawQuery("SELECT SUM (amount) as TotalExpenses FROM account",null);
        int totalExpColumn = cTotal.getColumnIndex("TotalExpenses");
        cTotal.moveToFirst();
        do{
            totalExp = cTotal.getString(totalExpColumn);
        } while (cTotal.moveToNext());
        pieChart.setCenterText("Total Expense\n" + "$"+ totalExp);



        //Get category array from database
        Cursor c = db.rawQuery("SELECT distinct type FROM account", null);
        int cateColumn =c.getColumnIndex("type");
        c.moveToFirst();
        String cate;
        //List <String> cateEntry = new ArrayList<>();
        //boolean differentName = false;
        do{
            cate = c.getString(cateColumn);
            category.add(cate);
        }while (c.moveToNext());

       /*category.add(cateEntry.get(0));

       for (int i = 1; i<cateEntry.size();i++) {
           for(int j = 0; j<category.size();j++) {
               if (!(cateEntry.get(i).equals(category.get(j)))) {
                   differentName = true;
               } else {differentName = false;}
           }
           if (differentName) {
               category.add(cateEntry.get(i));
           }
       }*/

        //Get data with ArrayList
        for(int n = 0; n<category.size(); n++){
            Cursor cExp = db.rawQuery(
                    "SELECT SUM (amount) as Total FROM account where type ='" + category.get(n) + "'", null);
            int totalColumn = cExp.getColumnIndex("Total");
            cExp.moveToFirst();
            String Total;
            do{
                Total = cExp.getString(totalColumn);
            } while (cExp.moveToNext());
            expense.add(Float.parseFloat(Total));
        }


        //Get data
        //Get total food expense
       /*Cursor cFood = db.rawQuery("SELECT SUM (amount) as foodTotal FROM expenses where category = 'Food'", null);
       int totalFoodColumn = cFood.getColumnIndex("foodTotal");
       cFood.moveToFirst();
       String foodTotal;
       do{
           foodTotal = cFood.getString(totalFoodColumn);
       } while (cFood.moveToNext());
       expense.add(Float.parseFloat(foodTotal));

       //Get total education expense
       Cursor cEdu = db.rawQuery("SELECT SUM (amount) as eduTotal FROM expenses where category = 'Education'", null);
       int totalEduColumn = cEdu.getColumnIndex("eduTotal");
       cEdu.moveToFirst();
       String eduTotal;
       do{
           eduTotal = cEdu.getString(totalEduColumn);
       } while (cEdu.moveToNext());
       expense.add(Float.parseFloat(eduTotal));

       //Get total cloths expense
       Cursor cCloths = db.rawQuery("SELECT SUM (amount) as clothsTotal FROM expenses where category = 'Cloths'", null);
       int totalClothsColumn = cCloths.getColumnIndex("clothsTotal");
       cCloths.moveToFirst();
       String clothsTotal;
       do{
           clothsTotal = cCloths.getString(totalClothsColumn);
       } while (cCloths.moveToNext());
       expense.add(Float.parseFloat(clothsTotal));*/

        addDataSet();
        addTextView();

    }

    private void addDataSet(){
        Log.d(TAG,"addDataSet started");

        ArrayList<PieEntry> expenseEntry = new ArrayList<>();
        ArrayList<String> cateEntry = new ArrayList<>();


        for (int i = 0; i<expense.size();i++) {
            expenseEntry.add(new PieEntry(expense.get(i), category.get(i)));

        }

       /*for (int i = 1; i<category.length;i++){
           cateEntry.add(category[i]);
       }*/

        //Create the data set
        PieDataSet pieDataSet = new PieDataSet(expenseEntry,"Categories");
        pieDataSet.setValueTextSize(12);


        //Add colors to data set
       /*ArrayList<Integer> colors = new ArrayList<>();
       colors.add(R.color.dining);
       colors.add(R.color.transport);
       colors.add(R.color.cloth);
       colors.add(R.color.study);

       pieDataSet.setColors(colors);*/

        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        //Add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        legend.setTextSize(10);


        //Create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void addTextView(){
        for(int i = 0; i<category.size(); i++){
            if (category.get(i).equals("Food&Drinks")){
                amountViews[0].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Education")){
                amountViews[1].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Clothes")){
                amountViews[2].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Travel")){
                amountViews[3].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Social")){
                amountViews[4].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Health")){
                amountViews[5].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Transport")){
                amountViews[6].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Home")){
                amountViews[7].setText(currencyFormat.format(expense.get(i)).toString());
            }
            else if (category.get(i).equals("Mobile")){
                amountViews[8].setText(currencyFormat.format(expense.get(i)).toString());
            }
        }
    }

}

