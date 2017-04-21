package com.example.huiying.mmm;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.huiying.mmm.Adapters.RecordListAdapter;
import com.example.huiying.mmm.GetternSetter.records;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    TextView selectedDate;
    Button pickDate;
    SQLiteDatabase db;

    private List<String> typeList = new ArrayList<>();
    private List<Integer> iconList = new ArrayList<>();
    private List<String> noteList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<String> amountList = new ArrayList<>();

    //Create an arraylist for records
    public ArrayList<records> recordList = new ArrayList<>();

    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");

        pickDate = (Button) findViewById(R.id.pickDate);
        selectedDate = (TextView) findViewById(R.id.selectedDateStart);

        pickDate.setOnClickListener(this);

        ListView lv = (ListView) findViewById(R.id.expenseRecord);

        db = openOrCreateDatabase("Account", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS account" + "(id integer primary key, type VARCHAR, amount VARCHAR, note VARCHAR, date VARCHAR);");

        //Get expense type
        Cursor t = db.rawQuery("SELECT type FROM account", null);
        int typeColumn = t.getColumnIndex("type");
        t.moveToFirst();
        String type;
        do {
            type = t.getString(typeColumn);
            typeList.add(type);
        } while (t.moveToNext());

        Cursor a = db.rawQuery("SELECT amount FROM account", null);
        int amountColumn = a.getColumnIndex("amount");
        a.moveToFirst();
        String amount;
        do {
            amount = a.getString(amountColumn);
            amountList.add(amount);
        } while (a.moveToNext());

        Cursor n = db.rawQuery("SELECT note FROM account", null);
        int noteColumn = n.getColumnIndex("note");
        n.moveToFirst();
        String note;
        do {
            note = n.getString(noteColumn);
            noteList.add(note);
        } while (n.moveToNext());

        Cursor d = db.rawQuery("SELECT date FROM account", null);
        int dateColumn = d.getColumnIndex("date");
        d.moveToFirst();
        String date;
        do {
            date = d.getString(dateColumn);
            dateList.add(date);
        } while (d.moveToNext());

        //Get icon based on type
        for (int i = 0; i < typeList.size(); i++) {
            if (typeList.get(i).equals("Food&Drinks")) {
                iconList.add(R.drawable.icon_eat);
            } else if (typeList.get(i).equals("Education")) {
                iconList.add(R.drawable.study_icon);
            } else if (typeList.get(i).equals("Shopping")) {
                iconList.add(R.drawable.icon_shopping);
            } else if (typeList.get(i).equals("Travel")) {
                iconList.add(R.drawable.icon_travel);
            } else if (typeList.get(i).equals("Social")) {
                iconList.add(R.drawable.icon_social);
            } else if (typeList.get(i).equals("Health")) {
                iconList.add(R.drawable.icon_health);
            } else if (typeList.get(i).equals("Transportation")) {
                iconList.add(R.drawable.icon_car);
            } else if (typeList.get(i).equals("Home")) {
                iconList.add(R.drawable.icon_home);
            } else if (typeList.get(i).equals("Mobile Phone")) {
                iconList.add(R.drawable.icon_tel);
            } else if (typeList.get(i).equals("Salary")) {
                iconList.add(R.drawable.icon_salary);
            } else if (typeList.get(i).equals("Investment")) {
                iconList.add(R.drawable.pigbank_icon);
            } else if (typeList.get(i).equals("Pocket Money")) {
                iconList.add(R.drawable.gift_icon);
            }
        }

        for (int j = (typeList.size() - 1); j >= 0; j--) {
            if (typeList.get(j).equals("Investment") || typeList.get(j).equals("Salary") || typeList.get(j).equals("Pocket Money")) {
                records r = new records(iconList.get(j), noteList.get(j), dateList.get(j), "+" + amountList.get(j));
                recordList.add(r);
            } else {
                records r = new records(iconList.get(j), noteList.get(j), dateList.get(j), "-" + amountList.get(j));
                recordList.add(r);
            }
        }


        RecordListAdapter adapter = new RecordListAdapter(this, R.layout.recordlist_content, recordList);
        lv.setAdapter(adapter);


        ImageButton charts = (ImageButton) findViewById(R.id.chartsTab);
        charts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChartsTab = new Intent(MainActivity.this, ChartScreens.class);
                startActivity(intentChartsTab);
            }
        });

        ImageButton add = (ImageButton) findViewById(R.id.addTab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddTab = new Intent(MainActivity.this, AddScreens.class);
                startActivity(intentAddTab);
            }
        });

        ImageButton coupons = (ImageButton) findViewById(R.id.couponsTab);
        coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCouponTab = new Intent(MainActivity.this, CouponScreens.class);
                startActivity(intentCouponTab);
            }
        });

        ImageButton settings = (ImageButton) findViewById(R.id.settingsTab);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetting = new Intent(MainActivity.this, SettingScreens.class);
                startActivity(intentSetting);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = new Date(year - 1900, month, dayOfMonth);
                String dateForDB = DateFormat.format("yyyy-MM-dd", date).toString();
                selectedDate.setText(dateForDB);
            }
        }
                , day, month, year);
        datePickerDialog.show();
    }

}
