package com.example.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.RoomDatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.note.Adapters.NoteListAdapter;
import com.example.note.Database.RoomDB;
import com.example.note.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity<editText_note> extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    List<Note> note = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Note selectedNote;
    EditText editText_title, editText_note;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_title = findViewById(R.id.editText_title);
        editText_note = findViewById(R.id.editText_note);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);


        database = RoomDB.getInstance(this);
        note = database.main().getAll();

        updateRecycler(note);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Note from the Note appendix");
        intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(editText_title));
        intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(editText_note));
        startActivity(Intent.createChooser(intent,"Share Note"));
        return super.onOptionsItemSelected(item);

    }

    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for(Note singleNote : note){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNote().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        noteListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                database.main().insert(new_note);
                note.clear();
                note.addAll(database.main().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102){
            if(requestCode==Activity.RESULT_OK){
                Note new_note = (Note) data.getParcelableExtra("note");
                database.main().update(new_note.getID(), new_note.getTitle(), new_note.getNote());
                note.clear();
                note.addAll(database.main().getAll());
                noteListAdapter.notifyDataSetChanged();

            }
        }
    }

    private void updateRecycler(List<Note> note) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this, note, noteClikListener);
        recyclerView.setAdapter(noteListAdapter);
    }

    private final NoteClikListener noteClikListener = new NoteClikListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this, NoteTakerActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = new Note();
            selectedNote = note;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.pin:
                if (selectedNote.isPinned()){
                    database.main().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.main().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "Pinned!", Toast.LENGTH_SHORT).show();
                }

                note.clear();
                note.addAll(database.main().getAll());
                noteListAdapter.notifyDataSetChanged();
                return true;

            case  R.id.delete:
                database.main().delete(selectedNote);
                note.remove(selectedNote);
                noteListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Note Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}