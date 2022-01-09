package com.example.noteandroidapp;

import androidx.cardview.widget.CardView;

import com.example.noteandroidapp.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes , CardView cardView);


}
