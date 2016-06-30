package com.testing.atul.knowthefriends;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    String[] names;
    String[] nums;
    ArrayList<String> names1 = new ArrayList<>();
    ArrayList<String> nums1 = new ArrayList<>();
    Integer[] pics = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,};

    DatabaseWorker con = new DatabaseWorker(this);
    SQLiteDatabase condb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populate();

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getApplicationContext(), ContactActivity.class);
                i.putExtra("name", names[position]);
                i.putExtra( "number", nums[position]);
                startActivity(i);
                populate();
            }
        });
    }

    public void populate(){

        condb = con.getReadableDatabase();
        Cursor cursor = condb.rawQuery("SELECT * FROM Contacts", null);
        cursor.moveToFirst();

        int ctr = 0;
        do{
            String xname = cursor.getString(cursor.getColumnIndex("Name"));
            names1.add(xname);
            String xnum = cursor.getString(cursor.getColumnIndex("Number"));
            nums1.add(xnum);
        }while(cursor.moveToNext());

        names=names1.toArray(new String[ctr]);
        nums=nums1.toArray(new String[ctr]);

        CustomListAdapter adapter = new CustomListAdapter(this, names, nums, pics);
        contactList = (ListView)findViewById(R.id.listView);
        assert contactList != null;
        contactList.setAdapter(adapter);
    }
}
