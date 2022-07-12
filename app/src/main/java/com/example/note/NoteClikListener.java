package com.example.note;

import androidx.cardview.widget.CardView;

import com.example.note.Model.Note;

public interface NoteClikListener {
    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);

}
