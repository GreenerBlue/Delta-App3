package com.testing.atul.knowthefriends;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    EditText nameBox ,numberBox;
    Button saveBtn;

    DatabaseWorker dbHelper;
    Intent i;
    String oldname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        nameBox = (EditText)findViewById(R.id.nameEt);
        numberBox = (EditText)findViewById(R.id.numberEt);
        saveBtn = (Button) findViewById(R.id.saveBtn);

        i = getIntent();
        oldname = i.getStringExtra("name");
        nameBox.setText(oldname);
        numberBox.setText(i.getStringExtra("number"));

        saveBtn.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            { saveRec(v);

            }
        });

    }

    public void saveRec(View v){
        if(oldname != null) {
            try
            {   dbHelper = new DatabaseWorker(getApplicationContext());
                dbHelper.updateRec(nameBox.getText().toString(), numberBox.getText().toString());
                Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            catch (SQLException e)
            {
                 Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertRec(nameBox.getText().toString(), numberBox.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
                }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        }
    }

}
