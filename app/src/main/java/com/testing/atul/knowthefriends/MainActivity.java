package com.testing.atul.knowthefriends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    String[] names = {"Alpha", "Beta", "Cupcake", "Donut", "Eclair"};
    String[] nums = {"123", "234", "345", "456", "567"};
    Integer[] pics = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomListAdapter adapter = new CustomListAdapter(this, names, nums, pics);
        contactList = (ListView)findViewById(R.id.listView);
        contactList.setAdapter(adapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String Slecteditem= names[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });


    }
}
