package com.example.android.popuvie.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popuvie.R;
import com.example.android.popuvie.model.Movie;
import com.example.android.popuvie.model.Trailer;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Asus X550ZE on 12/7/2016.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer>{

    public TrailerAdapter(Activity context, List<Trailer> trailer) {
        super(context, 0, trailer);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trailer_item, parent, false);
        }

        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_word);
        trailerName.setText(trailer.getName());

        return convertView;

    }
}
