package com.example.noteandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noteandroidapp.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTackerActivity extends AppCompatActivity {
    EditText editText_title  ,  editText_notes ;
    ImageView  imageView_save;
    Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_tacker);

        editText_title = findViewById(R.id.editText_title);
        editText_notes =  findViewById(R.id.editText_notes);

        imageView_save =  findViewById(R.id.imageView_save);

        notes  =  new Notes();
        boolean  isOldNote = false;


        try{
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote = true;
        }catch(Exception e){
            e.printStackTrace();
        }


        boolean finalIsOldNote = isOldNote;
        // set activity on click add bottom
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prendre des elements à partir de l'activité layout
                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();
                //  condition sur le type des elements
                if(description.isEmpty()){
                    //  toast message et comme l'alert dans js
                    Toast.makeText(NotesTackerActivity.this, "Please add some notes" , Toast.LENGTH_SHORT);
                    return ;
                }
                SimpleDateFormat formatter =  new SimpleDateFormat("EEE,d MMM yyyy HH:mm , a ");
                Date date =  new Date();
                if(!finalIsOldNote){
                    notes  =  new Notes();
                }

                //  former un type de note
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));
                // create intent to transfer data
                Intent intent  =  new Intent();
                // metter donnée dans un extra
                intent.putExtra("note" , notes);
                // envoyer
                setResult(Activity.RESULT_OK ,  intent);
                // finish et sortir de l activité
                // la class note doit implementer de serializable
                finish();

            }
        });

    }
}