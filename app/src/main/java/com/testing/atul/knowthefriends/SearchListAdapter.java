package com.testing.atul.knowthefriends;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Atul on 7/4/2016.
 */
public class SearchListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] itemnum;
    private final Bitmap[] imgid;

    public SearchListAdapter(Activity context, String[] itemname, String[] itemnum, Bitmap[] imgid) {
        super(context, R.layout.list_row, itemname);

        this.context=context;
        this.itemname=itemname;
        this.itemnum=itemnum;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null ,true);

        TextView name = (TextView) rowView.findViewById(R.id.name_list);
        ImageView photo = (ImageView) rowView.findViewById(R.id.photo_list);
        TextView number = (TextView) rowView.findViewById(R.id.number_list);

        name.setText(itemname[position]);
        photo.setImageBitmap(imgid[position]);
        number.setText(itemnum[position]);
        return rowView;

    }

}
