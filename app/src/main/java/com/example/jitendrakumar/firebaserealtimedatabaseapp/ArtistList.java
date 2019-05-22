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

public class ArtistList extends ArrayAdapter<Artist> {
    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList)
    {
        super(context, R.layout.artist_list_layout, artistList);
        this.context = context;
        this.artistList = artistList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate( R.layout.artist_list_layout, null, true );

        TextView tvName = listViewItem.findViewById( R.id.tvName );
        TextView tvGenre = listViewItem.findViewById( R.id.tvGenre );

        Artist artist = artistList.get( position );
        tvName.setText( artist.getArtistName() );
        tvGenre.setText( artist.getArtistGenre() );

        return listViewItem;
    }
}
