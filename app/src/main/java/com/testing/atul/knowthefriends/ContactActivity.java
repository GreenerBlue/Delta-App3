package com.testing.atul.knowthefriends;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class ContactActivity extends AppCompatActivity {

    EditText nameBox ,numberBox;
    Button saveBtn, delBtn;
    ImageView icon;

    DatabaseWorker dbHelper;
    Intent i;
    int sno; String oldname, imgDecodableString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit the contact");
        nameBox = (EditText)findViewById(R.id.nameEt);
        numberBox = (EditText)findViewById(R.id.numberEt);
        icon = (ImageView)findViewById(R.id.iconIv) ;
        saveBtn = (Button) findViewById(R.id.saveBtn);
        delBtn = (Button) findViewById(R.id.delBtn);

        i = getIntent();
        oldname=i.getStringExtra("name");
        nameBox.setText(oldname);
        numberBox.setText(i.getStringExtra("number"));
        byte[] recPic = i.getByteArrayExtra("picture");
        ByteArrayInputStream bis = new ByteArrayInputStream(recPic);
        icon.setImageBitmap(BitmapFactory.decodeStream(bis));


        dbHelper = new DatabaseWorker(getApplicationContext());
        if(!oldname.equals(""))
            sno = dbHelper.getSno(oldname);
        else
            sno = -1;

        saveBtn.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            { saveRec(v);

            }
        });
        icon.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick(View v)
            { accessGallery(v); }
        });

        delBtn.setOnClickListener(new View.OnClickListener()
        {   @Override
        public void onClick(View v)
        { delRec(v);

        }
        });
    }

    public void saveRec(View v){
        if(sno > 0) {
            try
            {   dbHelper = new DatabaseWorker(getApplicationContext());
                Bitmap b = ((BitmapDrawable)icon.getDrawable()).getBitmap();
                dbHelper.updateRec(sno, nameBox.getText().toString(), numberBox.getText().toString(), b);
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
            try{
                dbHelper = new DatabaseWorker(getApplicationContext());
                Bitmap b = ((BitmapDrawable)icon.getDrawable()).getBitmap();
                dbHelper.insertRec(nameBox.getText().toString(), numberBox.getText().toString(), b);
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                }
            catch (SQLException e){
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void accessGallery(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);

                icon.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void delRec(View v){

        dbHelper = new DatabaseWorker(getApplicationContext());
        dbHelper.deleteRec(sno);
        Toast.makeText(getApplicationContext(), "Person Delete Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


}
