package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteListsViewHolder extends RecyclerView.ViewHolder {
    TextView note_title;
    TextView note_date;
    TextView note_text;

    NoteListsViewHolder(View view){
        super(view);
        note_title = view.findViewById(R.id.noteTitle);
        note_date = view.findViewById(R.id.noteDate);
        note_text = view.findViewById(R.id.noteText);
    }
}
