package com.example.AndroidDevelopmentClass.Catalog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "LoginActivity";
    public static final String LOGIN_ACTIVITY_PREFS = "login_Activity";
    public static final String DEFAULT_EMAIL_HOLDER = "Default Email";
/*    todo: part of autoComplete
    public static final String PREFS_NAME = "prefs";
    public static final String PREF_SEARCH_HISTORY = "SearchHistory";
    private SharedPreferences settings;
    private Set<String> history;


    */

    EditText et_email;
    EditText et_password;
/*  todo: part of autoComplete
    private void setAutoCompleteSource(){
        AutoCompleteTextView view = (AutoCompleteTextView) findViewById(R.id.textInput);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        view.setAdapter(adapter);
    }

    private void addSearchInput(String input){
        if(!history.contains(input)){
            history.add(input);
            setAutoCompleteSource();
        }
    }

    private void savePrefs(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREF_SEARCH_HISTORY, history);
    }

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  todo: part of autoComplete
        settings = getSharedPreferences(PREFS_NAME, 0);
        history = settings.getStringSet(PREF_SEARCH_HISTORY, new HashSet<String>());
        setAutoCompleteSource();

        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.i(ACTIVITY_NAME, "onCreate()");

        /*
        todo: try to clear the textfield and prompt the user for input.
        -Only setting the text to "" does not invoke the keyboard!
        +it is done:
        StackOverFlow: How to show soft-keyboard when edittext is focused
        To force android to show the soft kybrd, you can use the InputMethodManager:

        EditText et = (EditText)findViewById(R.id.editTextField);
        InputMethodManager inputmanager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

        To force hide the kybrd:
        inputmanager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);


         */
        final EditText editText = (EditText) findViewById(R.id.emailField);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });



        //create a button reference to the RegisterButton and add a listener
        Button button = (Button) findViewById(R.id.RegisterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create preferences and then the Editable object
                SharedPreferences preferences = getSharedPreferences(LOGIN_ACTIVITY_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                et_email = (EditText) findViewById(R.id.emailField);
                //retrieve data from preferences
                editor.putString(DEFAULT_EMAIL_HOLDER, et_email.getText().toString());
                /*
                it is better to use apply() instead of commit(). Commit() will write to persistent
                storage immediately while apply() will do it in the background.
                 */
                editor.commit();

                //start an intent
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart()");

        SharedPreferences preferences = getSharedPreferences(LOGIN_ACTIVITY_PREFS, Context.MODE_PRIVATE);
        // On start set the default email to default email
        String defaultEmail = preferences.getString(DEFAULT_EMAIL_HOLDER, "email@domain.com");
        EditText editText = (EditText) findViewById(R.id.emailField);
        editText.setText(defaultEmail);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ACTIVITY_NAME, "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy()");
    }

}//end class LoginActivity