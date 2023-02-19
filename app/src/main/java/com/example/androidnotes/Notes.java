package com.example.androidnotes;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Notes implements Serializable {
    private String note_title;
    private String note_date;
    private String note_text;

    Notes() {
        this.note_title = "";
        this.note_text = "";
        this.note_date = "";
    }

    public Notes(String note_title, String note_date, String note_text) {
        this.note_title = note_title;
        this.note_date = note_date;
        this.note_text = note_text;
    }

    public String getNote_date() {
        return note_date;
    }

    public String getNote_text() {
        return note_text;
    }

    public String getNote_title(){
        return note_title;
    }

    public void setNote_title(String note_title){
        this.note_title = note_title;
    }

    public void setNote_date(String note_date){
        this.note_date = note_date;
    }

    public void setNote_text(String note_text){
        this.note_text = note_text;
    }

    public JSONObject toJSON() {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", getNote_title());
            jsonObject.put("notetext", getNote_text());
            jsonObject.put("date", getNote_date());


            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "name='" + note_title + '\'' +
                ", description='" + note_text + '\'' +
                ", date=" + note_date +
                '}';
    }
}
