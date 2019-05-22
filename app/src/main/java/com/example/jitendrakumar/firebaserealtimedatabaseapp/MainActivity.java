package com.example.jitendrakumar.firebaserealtimedatabaseapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    private EditText etName;
    private Spinner spinner;
    private Button btnAdd;
    private ListView lvArtists;
    List<Artist> artistList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        artistList = new ArrayList<>(  );

        databaseReference = FirebaseDatabase.getInstance().getReference("artists");

        etName = findViewById( R.id.etName );
        spinner = findViewById( R.id.spinner );
        btnAdd = findViewById( R.id.btnAdd );
        lvArtists = findViewById( R.id.lvArtists );


        btnAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        } );


        lvArtists.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get( position );

                showUpdateDialog( artist.getArtistId(), artist.getArtistName() );
                return true;
            }
        } );

        lvArtists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get( position );

                Intent intent = new Intent( getApplicationContext(), AddTrackActivity.class );

                intent.putExtra( ARTIST_ID, artist.getArtistId() );
                intent.putExtra( ARTIST_NAME, artist.getArtistName() );
                startActivity( intent );
            }
        } );





    }

    private void addArtist(){
        String name = etName.getText().toString().trim();
        String genre = spinner.getSelectedItem().toString();

        if(!TextUtils.isEmpty( name ))
        {
           /// store the data to firebase
           String id = databaseReference.push().getKey();
            Artist artist = new Artist( id, name, genre );
            databaseReference.child( id ).setValue( artist );
            Toast.makeText( this, "Artist added", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                artistList.clear();

                for(DataSnapshot artistSnapshot: dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add( artist );
                }

                ArtistList adapter = new ArtistList( MainActivity.this, artistList );
                lvArtists.setAdapter( adapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    private void showUpdateDialog(final String artistId, String artistName)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder( this );
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate( R.layout.update_dialog, null );
        dialogBuilder.setView( dialogView );

        final EditText etName = dialogView.findViewById( R.id.etName );
        final Spinner spinnerUpdate = dialogView.findViewById( R.id.spinnerUpdate );

        final  Button btnUpdate = dialogView.findViewById( R.id.btnUpdate );
        final Button btnDelete = dialogView.findViewById( R.id.btnDelete );

        dialogBuilder.setTitle( "Updating Artist "+  artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String genre = spinnerUpdate.getSelectedItem().toString();

                if(TextUtils.isEmpty( name ))
                {
                    etName.setError( "Name Required" );
                    return;
                }

                updateArtist( artistId, name, genre );
                alertDialog.dismiss();
            }
        } );

        btnDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }
        } );

    }

    private boolean updateArtist(String id, String name, String genre)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child( id );
        Artist artist = new Artist( id, name, genre );
        databaseReference.setValue( artist );

        Toast.makeText( this, "Artist Updated Successfully ", Toast.LENGTH_SHORT ).show();
        return true;
    }

    private  void deleteArtist(String artistId)
    {
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("artists").child( artistId );
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("Tracks").child( artistId );

        drArtist.removeValue();
        drTracks.removeValue();

        Toast.makeText( this, "Artist is deleted ", Toast.LENGTH_SHORT ).show();
    }

}
