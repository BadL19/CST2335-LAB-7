package com.example.AndroidDevelopmentClass.Catalog;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatWindow extends AppCompatActivity {


    ArrayList<String> msgs;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        ListView listView = (ListView) findViewById(R.id.listView);


        Button button = (Button) findViewById(R.id.sendButton);
        final EditText editText = (EditText) findViewById(R.id.editText);


        //ArrayList to hold the messages of the chat.
        msgs = new ArrayList<>();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                get the string representation of whatever is written in the EditText, and
                add it to the ArrayList.
                 */
                String aSingleMessage = editText.getText().toString();

                /*
                todo: the trim() VVVVVVV doesn't get rid of the carriage return!
                 */
                int messageCharLength = aSingleMessage.trim().length();


                if (TextUtils.isEmpty(aSingleMessage)) {
                    editText.setError("Empty message ignored");
                } else {
                    /*
                    add the msg to the ArrayList
                    set the hint to the number of msg
                    todo: if the ArrayList is empty, invite the user to type something in!
                     */

                    msgs.add(aSingleMessage);
                    editText.setText("");
                    editText.setHint("So far " + msgs.size() + " messages");

                }


/*
            A dirty way of checking if the EditText field is empty
            if so, pop a toast ignoring the msg,
            and avoid adding to the ArrayList msgs.

            if not, add the msg to the ArrayList msgs and show the number of msgs inserted so far.

                if (messageCharLength <= 0) {
                    Toast.makeText(
                            getApplicationContext(),
                            "EMPTY MSG IGNORED ",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {
                    msgs.add(aSingleMessage);

                    //display the last msg msgs.size()-1 in the array of msgs.
                    //                               ^^^^

                    Toast.makeText(
                            getApplicationContext(),
                            msgs.get(msgs.size() - 1),
                            Toast.LENGTH_SHORT
                    ).show();
                    editText.setText(null);

                }
*/


            }
        });
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return msgs.size();
        }

        public String getItem(int position) {
            return msgs.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }


            TextView message = (TextView) findViewById(R.id.message_text);  //this is the msg from the chat_row_incoming/outing
            message.setText(getItem(position));
            return result;

        }


    }//end class ChatAdapter


}//end class ChatWindow
