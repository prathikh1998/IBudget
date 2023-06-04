package com.example.ibudget.ui.payee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payee;

import java.util.List;

public class PayeeListAdapter extends RecyclerView.Adapter<PayeeListAdapter.PayeeListViewHolder> {
    private List<Payee> payeeData;
    private Context context;
    private String query;

    public static class PayeeListViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView tags;
        private final TextView type;

        public PayeeListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.payee_item_name);
            tags = itemView.findViewById(R.id.payee_item_tags);
            type = itemView.findViewById(R.id.payee_item_type);
        }

        public TextView getName() {
            return name;
        }

        public TextView getTags() {
            return tags;
        }

        public TextView getType() {
            return type;
        }

    }


    public PayeeListAdapter(List<Payee> payeeData, Context context){
        this.payeeData = payeeData;
        this.context = context;
    }

    public void setPayeeData(List<Payee> newPayeeData){
        payeeData = newPayeeData;
    }

    @NonNull
    @Override
    public PayeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payee_item,parent,false);
        return new PayeeListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PayeeListAdapter.PayeeListViewHolder holder, int position) {
        Payee current = payeeData.get(position);
        holder.name.setText(current.name);
        holder.tags.setText(current.tags);
        holder.type.setText(current.type);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),
                        PayeeInfoScreenActivity.class);
                intent.putExtra("name", current.name);
                intent.putExtra("address", current.address);
                intent.putExtra("phone", current.phoneNumber);
                intent.putExtra("account", current.accountNumber);
                intent.putExtra("billAmount", current.billAmount);
                intent.putExtra("billDueDate", current.billDueDate);
                intent.putExtra("website", current.websiteUrl);
                intent.putExtra("email", current.email);
                intent.putExtra("termLength", current.termLength);
                intent.putExtra("notes", current.notes);
                intent.putExtra("type", current.type);
                intent.putExtra("tags", current.tags);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return payeeData.size();
    }

    public void removeItem(int position) {
        // Permanently remove from database
        AppDatabase.getInstance(context).payeeDao().delete(payeeData.get(position));

        // Remove from UI
        payeeData.remove(position);

        notifyItemRemoved(position);
    }

}
