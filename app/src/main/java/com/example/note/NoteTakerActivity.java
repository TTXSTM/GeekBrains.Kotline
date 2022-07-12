package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.note.Model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_note;
    ImageView imageView_save;
    Note note;
    boolean isOldNote = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_note = findViewById(R.id.editText_note);


        note = new Note();
        try {
            note = (Note) getIntent().getSerializableExtra("old_not");
            editText_title.setText(note.getTitle());
            editText_note.setText(note.getNote());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String description = editText_note.getText().toString();

                if (description.isEmpty()){
                    Toast.makeText(NoteTakerActivity. this, "Please add some notes!", Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote){
                    note = new Note();
                }

                note.setTitle(title);
                note.setNote(description);
                note.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", note);
                setResult(Activity.RESULT_OK, intent);
                finish();
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
}