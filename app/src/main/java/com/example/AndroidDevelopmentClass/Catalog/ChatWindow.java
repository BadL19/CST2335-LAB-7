package com.example.AndroidDevelopmentClass.Catalog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
    }

    ListView listView = (ListView) findViewById(R.id.listView);
    Button button = (Button) findViewById(R.id.sendButton);
    EditText editText = (EditText) findViewById(R.id.editText);

}
