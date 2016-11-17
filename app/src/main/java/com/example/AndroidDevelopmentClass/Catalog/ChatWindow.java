package com.example.AndroidDevelopmentClass.Catalog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "Query";
    public static final String SQL_MESSAGE="SQL MESSAGE:";
    public static final String COLUMN_COUNT = "Cursor\'s  column count= ";
    Cursor cursor;

    //ArrayList to hold the messages of the chat.
    private ArrayList<String> msgs = new ArrayList<>();

    SQLiteDatabase database;
    ChatDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        helper = new ChatDatabaseHelper(this);
        database = helper.getWritableDatabase();
        Toast.makeText(this, "made it", Toast.LENGTH_SHORT).show();


        //This creates a string array for the
        String[] allColumns = { ChatDatabaseHelper.COLUMN_ID, ChatDatabaseHelper.COLUMN_MESSAGE };
        cursor = database.query(helper.TABLE_NAME,allColumns, null, null, null, null, null);


        ListView listView = (ListView) findViewById(R.id.listView);
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


        Button button = (Button) findViewById(R.id.sendButton);
        final EditText editText = (EditText) findViewById(R.id.editText);


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

                    getMsgs().add(aSingleMessage);
                    editText.setText("");
                    editText.setHint("So far " + getMsgs().size() + " messages");
                    messageAdapter.notifyDataSetChanged();

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

    /*

    getters and setters for the ArrayList msgs.
    It was first declared inside the onCreate() of the ChatWindow class, but since
    the ChatAdapter class needed it, decided to give accessibility to it so that it can be accessed
    from anywhere within the ChatWindow class :)

     */
    public ArrayList<String> getMsgs() {
        return msgs;
    }

    public void setMsgs(ArrayList<String> msgs) {
        this.msgs = msgs;
    }


    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return getMsgs().size();
        }

        public String getItem(int position) {
            return getMsgs().get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }


            TextView message = (TextView) result.findViewById(R.id.message_text);  //this is the msg from the chat_row_incoming/outing
            message.setText(getItem(position));
            return result;

        }


    }//end class ChatAdapter


}//end class ChatWindow
