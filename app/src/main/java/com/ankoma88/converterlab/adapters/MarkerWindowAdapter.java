package com.ankoma88.converterlab.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private Context mContext;

    public MarkerWindowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.marker_am, null);

        final TextView markerTitle = (TextView) view.findViewById(R.id.title_marker_AM);
        final TextView markerSnippet = (TextView) view.findViewById(R.id.snippet_marker_AM);

        markerTitle.setText(marker.getTitle());
        markerSnippet.setText(marker.getSnippet());
        return view;
    }
}
