/**
 * Reference: https://www.digitalocean.com/community/tutorials/android-recyclerview-swipe-to-delete-undo
 */

package com.example.ibudget.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibudget.R;
import com.example.ibudget.database.AppDatabase;
import com.google.android.material.snackbar.Snackbar;

public class DeletePaymentCallback extends ItemTouchHelper.Callback {
    Context mContext;
    PaymentListAdapter paymentListAdapter;
    View homeScreenLayout;
    AlertDialog.Builder confirmDeleteDialog;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    public DeletePaymentCallback(Context context, PaymentListAdapter paymentListAdapter,
                                 View homeScreenLayout) {
        this.mContext = context;
        this.paymentListAdapter = paymentListAdapter;
        this.homeScreenLayout = homeScreenLayout;
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#de574e");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_delete_black_24);
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

        confirmDeleteDialog = new AlertDialog.Builder(mContext);
        confirmDeleteDialog.setTitle("Delete Payment");
        confirmDeleteDialog.setMessage("Are you sure you want to delete this payment?");
        confirmDeleteDialog.setIcon(R.drawable.ic_warning_black_24);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        confirmDeleteDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            paymentListAdapter.removeItem(position);

            Snackbar snackbar = Snackbar.make(homeScreenLayout, "Payment Deleted Successfully",
                    Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

            HomeFragment.updateTotalBalance();
        });

        confirmDeleteDialog.setNegativeButton("No", (dialogInterface, i) -> {
            paymentListAdapter.notifyItemChanged(position);
        });

        confirmDeleteDialog.show();
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        // Clear the underlying canvas if the swipe is no longer active
        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(),
                    (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        // Draw the red underlying background
        mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        // Draw the delete icon
        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}
