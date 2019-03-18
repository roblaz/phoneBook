package com.nagel.phonebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.*;
import android.content.Intent;
import java.util.*;

/* Binds all together */
public class MainActivity extends AppCompatActivity {
    // menu variables (remember this is the trickier way of creating a menu -> instead of building your own menu xml files
    public static final int MENU_CREATE = Menu.FIRST + 1;
    public static final int MENU_SHOW = Menu.FIRST + 2;
    public static final int MENU_SEARCH = Menu.FIRST + 3;

    private People people;

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private ListView lstCodes;
    private EditText txtCode,txtCity;
    private ArrayAdapter<Zipcode> adapter;
    private Zipcodes zipcodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        zipcodes = new Zipcodes(db);
        lstCodes = findViewById(R.id.lstCodes);
        txtCode = findViewById(R.id.etCode);
        txtCity = findViewById(R.id.etCity);
        registerForContextMenu(lstCodes);
        displayCodes("", "");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(Menu.NONE, MENU_SHOW, Menu.NONE, getResources().getString(R.string.shw));
        menu.add(Menu.NONE, MENU_CREATE, Menu.NONE, getResources().getString(R.string.crt));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Zipcode zipcode = adapter.getItem(menuInfo.position);
        switch (item.getItemId()) {
            case MENU_SHOW:
                showPersons(zipcode);
                return (true);
            case MENU_CREATE:
                createPerson(zipcode);
                return (true);
        }
        return (super.onContextItemSelected(item));
    }

    private void showPersons(Zipcode zipcode)
    {
        people = new People(db, zipcode);
        if (people.getPeople().size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.notf), Toast.LENGTH_LONG).show();
        }
        else if (people.getPeople().size() == 1) {
            Intent intent = new Intent(this, PersonActivity.class);
            intent.putExtra("person", people.getPeople().get(0));
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, PeopleActivity.class);
            intent.putExtra("people", people);
            startActivity(intent);
        }
    }

    public void onClear(View view)
    {
        txtCode.setText("");
        txtCity.setText("");
    }

    public void onOk(View view)
    {
        displayCodes(txtCode.getText().toString(), txtCity.getText().toString());
    }

    public List<Zipcode> getCodes(String code, String city) {
        List<Zipcode> list = new ArrayList();
        for (Zipcode zipcode : zipcodes.getZipcodes())
            if (zipcode.getCode().startsWith(code) && zipcode.getCity().contains(city)) list.add(zipcode);
        return list;
    }

    public void displayCodes(String code, String city) {
        lstCodes.setAdapter(adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCodes(code, city)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_SEARCH, Menu.NONE, getResources().getString(R.string.src));
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SEARCH:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    private void createPerson(Zipcode zipcode)
    {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("zipcode", zipcode);
        startActivity(intent);
    }
}
