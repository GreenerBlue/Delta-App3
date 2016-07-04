package com.testing.atul.knowthefriends;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    String[] names;
    String[] nums;
    ArrayList<String> names1 = new ArrayList<>();
    ArrayList<String> nums1 = new ArrayList<>();
    Bitmap[] pics2 = new Bitmap[10];

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

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pics2[position].compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] picSend = bos.toByteArray();
                i.putExtra( "picture", picSend);
                startActivity(i);
                populate();
            }
        });
    }

    public void populate(){

        condb = con.getReadableDatabase();
        Cursor cursor = condb.rawQuery("SELECT * FROM Contacts ORDER BY Name", null);
        cursor.moveToFirst();
        int ctr = con.numberOfRows();
        int z = 0;

        do{
            String xname = cursor.getString(cursor.getColumnIndex("Name"));
            names1.add(xname);
            String xnum = cursor.getString(cursor.getColumnIndex("Number"));
            nums1.add(xnum);
            byte[] xpic = cursor.getBlob(cursor.getColumnIndex("Photo"));
            pics2[z] = BitmapFactory.decodeByteArray(xpic,0, xpic.length); z++;

        }while(cursor.moveToNext());

        names=names1.toArray(new String[ctr]);
        nums=nums1.toArray(new String[ctr]);

        CustomListAdapter adapter = new CustomListAdapter(this, names, nums, pics2);
        contactList = (ListView)findViewById(R.id.listView);
        assert contactList != null;
        contactList.setAdapter(adapter);

        condb.close();
        con.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i1 = new Intent(getApplicationContext(), ContactActivity.class);
                i1.putExtra("name", "");
                i1.putExtra("number", "");
                Bitmap b= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] picSend = bos.toByteArray();
                i1.putExtra( "picture", picSend);
                startActivity(i1);
                break;

            case R.id.help:
                Intent i2 = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i2);
                break;

            case R.id.search:
                Intent i3 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i3);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
