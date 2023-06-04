package com.example.ibudget.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.example.ibudget.database.Payment;
import com.example.ibudget.ui.payee.PayeeInfoScreenActivity;

import java.util.List;
import java.lang.Math;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentListViewHolder> {

    private List<Payment> paymentData;

    private Context context;
    private String query;


    public static class PaymentListViewHolder extends RecyclerView.ViewHolder {
        private final TextView payee;
        private final TextView amount;
        private final TextView date;

        public PaymentListViewHolder(View v) {
            super(v);

            payee = v.findViewById(R.id.payee_item_name);
            amount = v.findViewById(R.id.payee_item_type);
            date = v.findViewById(R.id.payee_item_tags);
        }

        public TextView getPayee() {
            return payee;
        }

        public TextView getAmount() {
            return amount;
        }

        public TextView getDate() {
            return date;
        }
    }

    public PaymentListAdapter(List<Payment> paymentData, Context context) {
        this.paymentData = paymentData;
        this.context = context;
    }

    public void setPaymentData(List<Payment> newPaymentData) {
        paymentData = newPaymentData;
    }

    @NonNull
    @Override
    public PaymentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_payment_item, parent, false);
        return new PaymentListViewHolder(v);

    }

    public void setSearchQuery(String query) {
        this.query = query;
        notifyDataSetChanged();
    }


    @SuppressLint({"DefaultLocale"})
    @Override
    /**
     * Binds the data of a Payment object to a PaymentListViewHolder,
     * and filters the data based on the search query.
     *
     * @param holder   The PaymentListViewHolder to bind the data to
     * @param position The position of the Payment object in the paymentData list
     */
    public void onBindViewHolder(@NonNull PaymentListViewHolder holder, int position) {

        // Get the Payment object at the specified position
        Payment currentPayment = paymentData.get(position);

        // Set the payee and amount TextViews of the PaymentListViewHolder to the values of the Payment object
        holder.payee.setText(currentPayment.payeePaidTo);
        holder.amount.setText(String.format("$%.2f", currentPayment.amount));

        // Filter the data based on the search query
        if (query != null && !query.isEmpty()) {
            if (!currentPayment.payeePaidTo.toLowerCase().contains(query.toLowerCase())) {
                // If the payment's payee does not contain the search query, hide the PaymentListViewHolder
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                return;
            } else {
                // If the payment's payee contains the search query, show the PaymentListViewHolder
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        // Set the payee, amount, and date TextViews of the PaymentListViewHolder to the values of the Payment object
        String payee = currentPayment.payeePaidTo;
        double amount = currentPayment.amount;
        String date = currentPayment.date;

        holder.getPayee().setText(payee);

        holder.getAmount().setText(String.format(amount > 0 ? "$%.2f" : "-$%.2f", Math.abs(amount)));
        if (amount < 0) {
            holder.getAmount().setTextColor(Color.parseColor("#de574e"));
        } else {
            holder.getAmount().setTextColor(Color.parseColor("#6fd97d"));
        }

        holder.getDate().setText(date);
    }

    @Override
    public int getItemCount() {
        return paymentData.size();
    }

    public void removeItem(int position) {
        // Permanently remove from database
        AppDatabase.getInstance(context).paymentDao().delete(paymentData.get(position));

        // Remove from UI
        paymentData.remove(position);

        notifyItemRemoved(position);
    }
}
