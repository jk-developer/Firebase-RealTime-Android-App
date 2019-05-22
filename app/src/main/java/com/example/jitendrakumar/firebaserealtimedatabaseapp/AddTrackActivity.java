package com.example.jitendrakumar.firebaserealtimedatabaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {

    TextView tvArtistName;
    EditText etTrackName;
    SeekBar seekBarRating;
    ListView lvTracks;
    Button btnAddTrack;

    List<Tracks> tracksList;

    DatabaseReference databaseTracksReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_track );

        tracksList = new ArrayList<Tracks>(  );

        tvArtistName = findViewById( R.id.tvArtistName );
        etTrackName = findViewById( R.id.etTrackName );
        seekBarRating = findViewById(  R.id.seekBarRating );
        lvTracks  = findViewById( R.id.lvTracks );
        btnAddTrack = findViewById( R.id.btnAddTrack );

        Intent intent = getIntent();
        String id = intent.getStringExtra( MainActivity.ARTIST_ID );
        String artistname = intent.getStringExtra( MainActivity.ARTIST_NAME );

        tvArtistName.setText( artistname );
        // creating a new node in database and connecting Tracks node with current artist id
        databaseTracksReference = FirebaseDatabase.getInstance().getReference("Tracks").child( id );

        btnAddTrack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveTrack();
            }
        } );
    }

    private void saveTrack()
    {
        String trackName = etTrackName.getText().toString().trim();
        int trackRating = seekBarRating.getProgress();

        if(!TextUtils.isEmpty( trackName ))
        {
            String trackId = databaseTracksReference.push().getKey();

            Tracks tracks = new Tracks( trackId, trackName, trackRating );

            databaseTracksReference.child( trackId ).setValue( tracks );
            Toast.makeText( this, "tracks saved succesfully ", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText( this, " please enter track name ", Toast.LENGTH_SHORT ).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracksReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tracksList.clear();

                for(DataSnapshot trackSnapshot: dataSnapshot.getChildren())
                {
                    Tracks track = trackSnapshot.getValue(Tracks.class);
                    tracksList.add( track );
                }

                TrackList trackListAdapter = new TrackList( AddTrackActivity.this, tracksList );
                lvTracks.setAdapter( trackListAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }
}
