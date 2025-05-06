package com.example.coffeemark.search;

import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CenterSnapScrollListener extends RecyclerView.OnScrollListener {

    public interface OnCenterItemSelected {
        void onItemSelected(int position);
    }

    private final RecyclerView recyclerView;
    private final LinearSnapHelper snapHelper;
    private final OnCenterItemSelected listener;
    private final Handler handler = new Handler();
    private Runnable selectionRunnable;
    private final int delayMillis;

    public CenterSnapScrollListener(RecyclerView recyclerView, LinearSnapHelper snapHelper,
                                    OnCenterItemSelected listener, int delayMillis) {
        this.recyclerView = recyclerView;
        this.snapHelper = snapHelper;
        this.listener = listener;
        this.delayMillis = delayMillis;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {
        super.onScrollStateChanged(rv, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
            if (centerView != null) {
                int position = recyclerView.getLayoutManager().getPosition(centerView);

                if (selectionRunnable != null) handler.removeCallbacks(selectionRunnable);
                selectionRunnable = () -> listener.onItemSelected(position);
                handler.postDelayed(selectionRunnable, delayMillis);
            }
        } else {
            if (selectionRunnable != null) handler.removeCallbacks(selectionRunnable);
        }
    }
}
