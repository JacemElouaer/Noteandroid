package com.example.noteandroidapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.noteandroidapp.Adapters.NotesListAdapter;
import com.example.noteandroidapp.Database.RoomDB;
import com.example.noteandroidapp.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes =  new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Notes SelectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // relating with element like floating botton
        recyclerView= findViewById(R.id.recycler_home);
        fab_add =  findViewById(R.id.fab_add);
        searchView_home =  findViewById(R.id.searchView_home);
        //initialise database
        database=RoomDB.getInstance(this);
        // Get data from database
        notes =  database.mainDAO().getAll();
        // install it in this class
        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            // the add bottom open a new activity
            @Override
            public void onClick(View view) {
                // objet Intent type explicit nous aide à transfer data on activity or open new activity
                Intent intent =  new Intent(MainActivity.this , NotesTackerActivity.class);
                // requestCode définit  le type de l intent envoyer
                startActivityForResult(intent ,101);
            }
        });
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener (){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    public void filter(String newText){
        List<Notes> filteredlist = new ArrayList<>();
        for (Notes singleNote: notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredlist.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // prendre donnée apartir de la ajout
        if(requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                //  getSerializableExtra methode pour prender l attribut dans l extra
                // cast data is important
                Notes new_notes  =  (Notes) data.getSerializableExtra("note");
                // inserer les donnée
                database.mainDAO().insert(new_notes);
                notes.clear();
                // ajouter au notes liste
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();

            }
        }
        else if(requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getId() , new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    // the method that allow us to
    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        // set layout manager is for set the division of space
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter =  new NotesListAdapter(MainActivity.this ,  notes ,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final  NotesClickListener notesClickListener =  new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this , NotesTackerActivity.class);
            intent.putExtra("old_note",  notes);
            startActivityForResult(intent ,  102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            SelectedNote =  new Notes();
            SelectedNote =  notes;
            showPopup(cardView);


        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,  cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menue);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.pin:
                if(SelectedNote.isPinned()){
                    database.mainDAO().pin(SelectedNote.getId() ,false);
                    Toast.makeText(MainActivity.this,  "Unpinned ! " , Toast.LENGTH_SHORT).show();
                }
                else{
                    database.mainDAO().pin(SelectedNote.getId() ,true);
                    Toast.makeText(MainActivity.this,  "Pinned ! " , Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return  true;
            case R.id.delete:
                database.mainDAO().delete(SelectedNote);
                notes.remove(SelectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,  "Note Deleted ! " ,  Toast.LENGTH_SHORT).show();
                return true;
            default:
                return  false;
        }
    }
}