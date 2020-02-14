package com.apkglobal.memoapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apkglobal.memoapp.Data.DatabaseHandler;
import com.apkglobal.memoapp.Model.Memo;
import com.apkglobal.memoapp.R;

public class MainActivity extends AppCompatActivity {


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText addMemo;
    private Button saveMemo;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        byPassActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });
    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_memo_dialog, null);

        addMemo = view.findViewById(R.id.addMemo);
        saveMemo = view.findViewById(R.id.saveMemo);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo: Save to db
                //Todo: Go to next screen

                if (!saveMemo.getText().toString().isEmpty()) {
                    saveMemoToDB(v);
                }
            }
        });
    }


    private void saveMemoToDB(View v) {

        Memo memo = new Memo();

        String newMemo = addMemo.getText().toString();


        memo.setTask(newMemo);

        //Save to DB method...
        db.addMemo(memo);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 2000); //  2 second.

    }

    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getMemoCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }
}