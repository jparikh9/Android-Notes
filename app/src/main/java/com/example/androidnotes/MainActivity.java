package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener{

    private final List<Notes> notes_list = new ArrayList<>();

    private RecyclerView recycler_view;
    private Notes note;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private NotesAdapter noteAdapter;
    private static final String TAG = "MainActivity";
    private int position_for_editNote = 0;
    private boolean flag_editNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // intializing recycler view
        recycler_view = findViewById(R.id.recycle_view_layout);

        // Data to recyclerview adapter
        noteAdapter = new NotesAdapter(notes_list, this);
        recycler_view.setAdapter(noteAdapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        // intialising laucher which is used between activities
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::resultReturnedFromActivity);

        //saveNotesToFile();
        //loadNotesFromFile();
        //long milli = System.currentTimeMillis();
        //String millistr = Long.toString(milli);
        //note = new Notes("jinit",millistr,"fsdnklanf");
        loadNotesFromFile();
        //saveNotesToFile();

    }

    @Override
    protected void onPause() {
        // notes list will get stored in local file
        saveNotesToFile();
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveNotesToFile();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // select layout for menu items to show
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // actions to perform when a menu item is clicked
        //textView.setText(String.format("You picked: %s", item.getTitle()));
        int selected = item.getItemId();

        if (selected == R.id.aboutMenuItem) {
            //Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (selected == R.id.newnoteMenuItem) {
            //Toast.makeText(this, "You clicked new note", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,EditActivity.class);
            // result launcher used to get results from invoked activity
            activityResultLauncher.launch(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadNotesFromFile(){
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream input_stream = getApplicationContext().openFileInput(getString(R.string.fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input_stream, StandardCharsets.UTF_8));

            StringBuilder string_builder = new StringBuilder();
            String string_line;
            while ((string_line = reader.readLine()) != null) {
                string_builder.append(string_line);
            }
            Log.d(TAG, "string line = " + string_builder.toString());
            JSONArray jsonArray = new JSONArray(string_builder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                // taking each element from array into json object
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String notetext = jsonObject.getString("notetext");
                String date = jsonObject.getString("date");
                //Long millisec = Long.parseLong(date);

                note = new Notes(title,date,notetext);
                notes_list.add(note);
                Log.d(TAG, "loadFile: " + note);
            }
            this.setTitle("AndroidNotes (" + notes_list.size() + ")");
            //return note;


        } catch (FileNotFoundException e) {
            Log.d(TAG, "exception returned (file_not_found): " + e);
            Toast.makeText(this, getString(R.string.noFile), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "exception returned: " + e.getStackTrace().toString());
            e.printStackTrace();
        }
        //return null;
    }

    private void saveNotesToFile(){
        Log.d(TAG, "saveNotes: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.fileName), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < notes_list.size(); i++) {
                JSONObject jsonObject = notes_list.get(i).toJSON();
                jsonArray.put(jsonObject);
            }

            Log.d(TAG, "save Note: " + jsonArray);
            printWriter.print(jsonArray);
            printWriter.close();
            fos.close();

            //Log.d(TAG, "save note:\n" + note.toJSON());
            //Toast.makeText(this, getString(R.string.fileName), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void resultReturnedFromActivity(ActivityResult result) {

        if (result == null || result.getData() == null) {
            return;
        }
        Notes note;
        Intent intent = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            if(intent.hasExtra("UpdatedNote")){
                if(flag_editNote) {
                    note = (Notes) intent.getSerializableExtra("UpdatedNote");
                    //Notes temp_note = notes_list.get(this.position_for_editNote);
                    notes_list.set(position_for_editNote, note);

                    String s = "";

                    Collections.sort(notes_list, new Comparator<Notes>() {
                        @Override
                        public int compare(Notes notes, Notes t1) {
                            return t1.getNote_date().compareTo(notes.getNote_date());
                        }
                    });
                    flag_editNote = false;

                    Log.d(TAG, "updated note in list at position" + position_for_editNote);

                    //noteAdapter.notifyItemChanged(position_for_editNote);
                    noteAdapter.notifyDataSetChanged();
                    position_for_editNote = 0;
                }
            }else {
                note = (Notes) intent.getSerializableExtra("Note");


                notes_list.add(0,note);
                Log.d(TAG, "list size" + notes_list.size());
                noteAdapter.notifyItemInserted(0);
            }


        } else {
            Toast.makeText(this, "OTHER result not OK!", Toast.LENGTH_SHORT).show();
        }
        saveNotesToFile();
        this.setTitle("AndroidNotes (" + notes_list.size() + ")");
    }

    @Override
    public boolean onLongClick(View view) {
        // get item position to be removed
        int position = recycler_view.getChildLayoutPosition(view);
        //remove from recycler view's list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notes_list.remove(position);
                noteAdapter.notifyItemRemoved(position);
                setTitle("AndroidNotes (" + notes_list.size() + ")");
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //return false;
            }
        });
        builder.setTitle("Delete Note?");
        builder.setMessage("Press 'YES' to delete. Press 'NO' to keep");

        AlertDialog dialog = builder.create();
        dialog.show();


        return false;
    }

    @Override
    public void onClick(View view) {
        // get item position to be edited
        int position = recycler_view.getChildLayoutPosition(view);
        //remove from recycler view's list
        Notes note = notes_list.get(position);
        Intent intent = new Intent(this, EditActivity.class);
        // setting position in global variable to know which note to update after edit activity
        intent.putExtra("EditNote", note);
        position_for_editNote = position;
        flag_editNote = true;
        //intent.putExtra("Position",position);
        activityResultLauncher.launch(intent);
        //return false;
    }
}