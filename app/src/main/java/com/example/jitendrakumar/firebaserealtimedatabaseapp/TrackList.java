package com.example.jitendrakumar.firebaserealtimedatabaseapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackList extends ArrayAdapter<Tracks> {

    private Activity context;
    private List<Tracks> trackList;

    public TrackList(Activity context, List<Tracks> trackList)
    {
        super(context, R.layout.track_list_layout, trackList);
        this.context = context;
        this.trackList = trackList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.track_list_layout, null, true );

        TextView tvTrackName = listViewItem.findViewById( R.id.tvTrackName );
        TextView tvTrackRating = listViewItem.findViewById( R.id.tvTrackRating );

        Tracks tracks = trackList.get( position );
        tvTrackName.setText( tracks.getTrackName() );
        tvTrackRating.setText( String.valueOf( tracks.getTrackRating()  ));

        return listViewItem;
    }
}
