package com.example.AndroidDevelopmentClass.Catalog;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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


import com.example.AndroidDevelopmentClass.Catalog.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    // bringing over the variables from the ChatWindow.java
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
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


//        View recyclerView = findViewById(R.id.message_list);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);


        //instantiating a ChatDatabaseHelper object
        helper = new ChatDatabaseHelper(this);

        //instantiating a SQLiteDatabase object
        database = helper.getWritableDatabase();

        //verifying that we have successfully acquired a database.
        Toast.makeText(this, "made it", Toast.LENGTH_SHORT).show();


        listView = (ListView) findViewById(R.id.listView);
        final MessageListActivity.ChatAdapter messageAdapter = new MessageListActivity.ChatAdapter(this);
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


                    scrollMyListViewToBottom();
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


        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

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
        //        public DummyContent.DummyItem mItem;


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

            LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }


            TextView message = (TextView) result.findViewById(R.id.message_text);  //this is the msg from the chat_row_incoming/outing
//            message.setText(getItem(position));

            final String messageText = getItem(position);
            message.setText(messageText);


            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, messageText);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                        context.startActivity(intent);
                    }
                }
            });


            return result;
        }//end getView


    }//end class ChatAdapter


    //I believe this was auto generated
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }


    //to keep the listView scrolled at the bottom ONLY
    // when there is a new entry. Just like WhatsApp
    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(listView.getCount() - 1);
            }
        });
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
