package com.example.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class NotesAdapter extends RecyclerView.Adapter<NoteListsViewHolder>{
    private static final String TAG = "NotesAdapter";
    private final List<Notes> notesList;
    private final MainActivity main_activity;

    NotesAdapter(List<Notes> notesList, MainActivity main_activity) {
        this.notesList = notesList;
        this.main_activity = main_activity;
    }
    @NonNull
    @Override
    public NoteListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW NoteListsViewHolder");

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_layout, parent, false);

        view.setOnClickListener((View.OnClickListener) main_activity);
        view.setOnLongClickListener(main_activity);

        return new NoteListsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListsViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER with Notes " + position);
        String noteText = "";
        String noteTitle = "";
        Notes note = notesList.get(position);
        noteTitle = note.getNote_title();
        noteText = note.getNote_text();
        if(noteText.length() > 80){
            noteText = noteText.substring(0,80) + "...";
        }
        if(noteTitle.length() > 80){
            noteTitle = noteTitle.substring(0,80) + "...";
        }

        Date cd = new Date(Long.parseLong(note.getNote_date()));
        SimpleDateFormat dateFormat = new SimpleDateFormat ("E MMM dd',' hh:mm a ");
        //note.setNote_date(dateFormat.format(cd));

        holder.note_title.setText(noteTitle);
        holder.note_text.setText(noteText);
        holder.note_date.setText(dateFormat.format(cd));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
