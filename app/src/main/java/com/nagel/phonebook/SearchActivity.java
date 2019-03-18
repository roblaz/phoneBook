package com.nagel.phonebook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

/* SearchActivity is also simple and is used to enter search criteria, code is executed in SQL SELECT, but using the class People. */

public class SearchActivity extends AppCompatActivity {
    private EditText txtFname,txtLname,txtAddr,txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtFname = findViewById(R.id.etFirst_Name);
        txtLname = findViewById(R.id.etLast_Name);
        txtAddr = findViewById(R.id.etAddress);
        txtTitle = findViewById(R.id.etTitle);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void onClears(View view)
    {
        txtFname.setText("");
        txtLname.setText("");
        txtAddr.setText("");
        txtTitle.setText("");
    }

    public void onSearch(View view)
    {
        String fname = txtFname.getText().toString().trim();
        String lname = txtLname.getText().toString().trim();
        String addr = txtAddr.getText().toString().trim();
        String title = txtTitle.getText().toString().trim();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        People people = new People(db, fname, lname, addr, title);
        if (people.getPeople().size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.notf), Toast.LENGTH_LONG).show();
            db.close();
        }
        else if (people.getPeople().size() == 1) {
            Intent intent = new Intent(this, PersonActivity.class);
            intent.putExtra("person", people.getPeople().get(0));
            db.close();
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, PeopleActivity.class);
            intent.putExtra("people", people);
            db.close();
            startActivity(intent);
            finish();
        }
    }
}
