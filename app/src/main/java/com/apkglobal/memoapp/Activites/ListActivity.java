package com.apkglobal.memoapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apkglobal.memoapp.Data.DatabaseHandler;
import com.apkglobal.memoapp.Data.RecyclerViewAdapter;
import com.apkglobal.memoapp.Model.Memo;
import com.apkglobal.memoapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Memo> memoList;
    private List<Memo> memoItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText addMemo;
    private Button saveMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopDialog();


            }
        });


        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        memoList = new ArrayList<>();
        memoItems = new ArrayList<>();


        // Get items from database
        // calling method..
        memoList = db.getAllMemo();

        for (Memo m : memoList) {
            Memo memo = new Memo();
            memo.setTask(m.getTask());
            memo.setId(m.getId());
            memo.setDateAdded("Added on: " + m.getDateAdded());

            memoItems.add(memo);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, memoItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }


    private void createPopDialog() {

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
                saveMemoToDB(v);
            }
        });

    }

    private void saveMemoToDB(View v) {

        Memo memo = new Memo();

        String newMemo = addMemo.getText().toString();

        memo.setTask(newMemo);

        //Save to DB
        db.addMemo(memo);

        Snackbar.make(v, "Task Saved!", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);


    }

}

