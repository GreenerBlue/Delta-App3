package com.testing.atul.knowthefriends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Atul on 7/3/2016.
 */
public class SearchActivity extends AppCompatActivity{

    EditText srchBox;
    ListView contactList;

    DatabaseWorker con = new DatabaseWorker(this);
    SQLiteDatabase condb;

    String srchText;
    String[] names;
    String[] nums;
    ArrayList<String> names1 = new ArrayList<>();
    ArrayList<String> nums1 = new ArrayList<>();
    Bitmap[] pics2 = new Bitmap[10];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Search");

        srchBox = (EditText)findViewById(R.id.searchBox);
    }

    public void search(View v){

        srchText = srchBox.getText().toString();
        //Toast.makeText(getApplicationContext(),srchText,Toast.LENGTH_SHORT).show();

        condb = con.getReadableDatabase();
        Cursor cur = condb.rawQuery("SELECT * FROM Contacts WHERE Name LIKE ? ORDER BY Name",new String[]{"%"+srchText+"%"});

        if((cur!=null)&&(cur.getCount()>0)) {
            cur.moveToFirst();
            int ctr = con.numberOfRows();
            int z = 0;

            names1.clear();
            nums1.clear();

            do{
                String xname = cur.getString(cur.getColumnIndex("Name"));
                names1.add(xname);
                String xnum = cur.getString(cur.getColumnIndex("Number"));
                nums1.add(xnum);
                byte[] xpic = cur.getBlob(cur.getColumnIndex("Photo"));
                pics2[z] = BitmapFactory.decodeByteArray(xpic,0, xpic.length); z++;

            }while(cur.moveToNext());

            names=names1.toArray(new String[ctr]);
            nums=nums1.toArray(new String[ctr]);

            CustomListAdapter adapter = new CustomListAdapter(this, names, nums, pics2);
            contactList = (ListView)findViewById(R.id.srchlistView);
            assert contactList != null;
            contactList.setAdapter(adapter);
        }
        else {
            Toast.makeText(getApplicationContext(),"Sorry, the contact "+srchText+" is not found",Toast.LENGTH_SHORT).show();
        }

        cur.close();
        condb.close();

    }
}
