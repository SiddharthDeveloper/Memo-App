package com.siddharth.memoapp.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.siddharth.memoapp.Model.Memo;
import com.siddharth.memoapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Memo> memoItems;
    private LayoutInflater inflater;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    public RecyclerViewAdapter(Context context, List<Memo> memoItems) {

        this.context = context;
        this.memoItems = memoItems;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Memo memo = memoItems.get(position);

        holder.rowName.setText(memo.getTask());
        holder.rowDate.setText(memo.getDateAdded());

    }

    @Override
    public int getItemCount() {
        return memoItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView rowName;
        public TextView rowDate;
        public Button rowEdit;
        public Button rowDelete;
        public int id;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            rowName = itemView.findViewById(R.id.rowName);
            rowDate = itemView.findViewById(R.id.rowDate);
            rowEdit = itemView.findViewById(R.id.rowEdit);
            rowDelete = itemView.findViewById(R.id.rowDelete);

            rowEdit.setOnClickListener(this);
            rowDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.rowEdit:
                    int position = getAdapterPosition();
                    Memo memo = memoItems.get(position);
                    editItem(memo);
                    break;


                case R.id.rowDelete:
                    position = getAdapterPosition();
                    memo = memoItems.get(position);
                    deleteItem(memo.getId());
                    break;

            }

        }

        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.delete_memo, null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteMemo(id);
                    memoItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();


                }
            });
        }


        public void editItem(final Memo memo) {

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.add_memo_dialog, null);

            final TextView memoTitle = view.findViewById(R.id.memoTile);
            final EditText addMemo = view.findViewById(R.id.addMemo);

            memoTitle.setText("Edit Memo");
            Button saveMemo = view.findViewById(R.id.saveMemo);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update item
                    memo.setTask(addMemo.getText().toString());

                    if (!addMemo.getText().toString().isEmpty()) {
                        db.updateMemo(memo);
                        notifyItemChanged(getAdapterPosition(), memo);
                    } else {
                        Snackbar.make(view, "Task Succesfull", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }
    }

}
