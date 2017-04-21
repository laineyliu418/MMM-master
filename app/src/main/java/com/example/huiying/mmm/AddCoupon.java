package com.example.huiying.mmm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;


public class AddCoupon extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    Button chooseDate;
    TextView selectedDate;

    /*
    // currency and string formatter objects
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();

    //define database
    SQLiteDatabase db = null;
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_page_coupon);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button takePhotoButton = (Button)findViewById(R.id.openCamera);
        imageView = (ImageView)findViewById(R.id.showPhoto);
        takePhotoButton.setOnClickListener(new btnTakePhotoClicker());

        FloatingActionButton saveCoupon = (FloatingActionButton)findViewById(R.id.saveFloatingActionButton);
        saveCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCoupon.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
            }
        });

        chooseDate = (Button)findViewById(R.id.expiry);
        selectedDate = (TextView) findViewById(R.id.selectedDate);
        chooseDate.setOnClickListener(this);


    }

    //return the image taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    class btnTakePhotoClicker implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //take a picture and pass to onActivityResult
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
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
                String dateForDB = DateFormat.format("yyyy-MM-dd", date).toString();;
                selectedDate.setText(dateForDB);
            }
        }
                , day, month, year);
        datePickerDialog.show();
    }
}
