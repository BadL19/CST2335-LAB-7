package com.example.AndroidDevelopmentClass.Catalog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    public static final String SQL_MESSAGE = "SQL MESSAGE:";
    public static final String COLUMN_COUNT = "Cursor\'s  column count= ";
    Cursor cursor;

    //ArrayList to hold the messages of the chat.
    private ArrayList<String> msgs = new ArrayList<>();

    /*
    Exposes methods to manage a SQLite database.
    SQLiteDatabase has methods to create, delete, execute SQL commands, and
    perform other common database management tasks.
     */
    SQLiteDatabase database;

    /*
    ChatDatabaseHelper extends the SQLiteOpenHelper class.
    A helper class to manage database creation and version management.
    You create a subclass implementing onCreate(SQLiteDatabase), onUpgrade(SQLiteDatabase, int, int)
    and optionally onOpen(SQLiteDatabase), and this class takes care of opening the database if it
    exists, creating it if it does not, and upgrading it as necessary. Transactions are used to make
    sure the database is always in a sensible state.
    This class makes it easy for ContentProvider implementations to defer opening and upgrading the
    database until first use, to avoid blocking application startup with long-running database
     upgrades.
     */
    ChatDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //instantiating a ChatDatabaseHelper object
        helper = new ChatDatabaseHelper(this);

        //instantiating a SQLiteDatabase object
        database = helper.getWritableDatabase();

        //verifying that we have successfully acquired a database.
        Toast.makeText(this, "made it", Toast.LENGTH_SHORT).show();


        ListView listView = (ListView) findViewById(R.id.listView);
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);
        Button button = (Button) findViewById(R.id.sendButton);
        final EditText editText = (EditText) findViewById(R.id.editText);

        /*
        We first add the user values into the database and after the setOnClickListener, we
        query the data at once--see lines 117 to 155
         */
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
                todo: perhaps we *want* to keep the carriage return character!
                 */
                int messageCharLength = aSingleMessage.trim().length();

                //using TextUtils , show an error pop up if user attempts to send an empty msg.
                if (TextUtils.isEmpty(aSingleMessage)) {
                    editText.setError("Empty message ignored");
                } else {
                    /*
                    add the msg to the ArrayList, set the hint to the number of msg
                    todo: if the ArrayList is empty, invite the user to type something in!
                     */
                    getMsgs().add(aSingleMessage);

                    // VVVVVV This class provides applications access to the content model.
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ChatDatabaseHelper.COLUMN_MESSAGE, editText.getText().toString());
                    database.insert(ChatDatabaseHelper.TABLE_NAME, "null", contentValues);
                    editText.setText("");
                    editText.setHint("So far " + getMsgs().size() + " messages");
                    messageAdapter.notifyDataSetChanged();
                }//end else
            }//end onClick
        });//end setOnClickListener

        //This creates a string array used for the Cursor object and its query(..) method signature.
        String[] allColumns = {ChatDatabaseHelper.COLUMN_ID, ChatDatabaseHelper.COLUMN_MESSAGE};

        /*
        query(..) signature:
        public Cursor query (boolean distinct, String table, String[] columns, String selection,
        String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
         */
        cursor = database.query(helper.TABLE_NAME, allColumns, null, null, null, null, null);

        //move the cursor to the first row
        cursor.moveToFirst();

        //while the cursor is not pointing to the position after the last row (
        while (!cursor.isAfterLast()) {

            /*
            within the while loop, the cursor keeps moving to the next value entered by the user..
            so at the current position, the cursor is pointing to the current value,
            and we want to put that value into the msgs ArrayList.
            So we get the String value from the cursor that is pointing at the column labeled "_msg"
            (otherwise known as COLUMN_MESSAGE). If we consider Key and Value pair, we are
            looking into the Value which is the message.
             */
            String newMessage = cursor.getString(cursor.getColumnIndex(
                    ChatDatabaseHelper.COLUMN_MESSAGE));

            //todo this could be adding twice with what I have in the the setOnClickListener for sendButton.
            //once we get the cursor's MESSAGE value, we add it to the msgs ArrayList.
            msgs.add(newMessage);

            //log the current MESSAGE retrieved from the MESSAGE column of the current
            // index in the CHATS table
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(
                    ChatDatabaseHelper.COLUMN_MESSAGE)));

            //move the cursor to the next row
            cursor.moveToNext();
        }//end of while

        //see comments for private void TableStat()
        TableStat();

    }//end onCreate


    /*
    Using the cursor to get the number of columns, and their names
     */
    private void TableStat() {
        for (int x = 0; x < cursor.getColumnCount(); x++) {
            Log.i("Cursor column name", cursor.getColumnName(x));
        }//end for
        Log.i(ACTIVITY_NAME, "Cursors column count =" + cursor.getColumnCount());
    }//end TableStat


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


    /*
    Takes every single simple element of the ListView view and gives it its own layout.
     */
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
        }//end getView


    }//end class ChatAdapter

    /*
    We want to close both the cursor and the database when the application is out of focus
    and/or destroyed. This will prevent database leaks!
     */
    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        cursor.close();
        database.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cursor.close();
        database.close();
    }
}//end class ChatWindow
