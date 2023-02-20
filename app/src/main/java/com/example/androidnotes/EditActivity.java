package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText note_title;
    private EditText note_description;
    private Notes note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        note_title =findViewById(R.id.editTextNoteTitle);
        note_description = findViewById(R.id.editTextNoteContent);
        Intent intent = getIntent();
        if(intent.hasExtra("EditNote")){
            // setting fields from object passed from main activity. This is to edit existing note
            note = (Notes)intent.getSerializableExtra("EditNote");
            note_description.setText(note.getNote_text());
            note_title.setText(note.getNote_title());
        }else {
            // new blank note
            note = new Notes();
        }
    }

    public void saveNote() {
        String title = note_title.getText().toString();
        String desc = note_description.getText().toString();
        if(title.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder.setTitle("No Title!");
            builder.setMessage("Cannot save a note without title");

            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(this, "Cannot save a note without title", Toast.LENGTH_SHORT).show();
            //finish();
        }else {
            note.setNote_title(title);
            note.setNote_text(desc);
            long time = System.currentTimeMillis();
            //Date cd = new Date(System.currentTimeMillis());
            //SimpleDateFormat dateFormat = new SimpleDateFormat ("E MMM dd',' hh:mm a ");
            //note.setNote_date(dateFormat.format(cd));
            note.setNote_date(Long.toString(time));
            Intent intent = getIntent();
            if (intent.hasExtra("EditNote")) {
                intent.putExtra("UpdatedNote", note);
            } else {
                intent.putExtra("Note", note);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editactivity_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selected = item.getItemId();
        if (selected == R.id.saveNote) {
            Toast.makeText(this, "You clicked save button", Toast.LENGTH_SHORT).show();
            saveNote();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(note.getNote_title().equals(note_title.getText().toString()) && note.getNote_text().equals(note_description.getText().toString())){
            super.onBackPressed();
        }else if(note_title.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            builder.setTitle("Just to let you know!");
            builder.setMessage("Cannot save a note without title");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveNote();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.setTitle("Note not Saved!");
            builder.setMessage("Press 'YES' to save. Press 'NO' to discard");

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}