package com.example.coffeemark.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.coffeemark.R;
import com.example.coffeemark.view.CustomButton;

public class ConfirmDialogFragment extends DialogFragment {

    private final Select select;
    private final String message;

    public ConfirmDialogFragment(String message,Select select) {
        this.select = select;
        this.message = message;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_dialog); // твій XML-файл
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.messageText);
        CustomButton yesButton = dialog.findViewById(R.id.yes);
        CustomButton noButton = dialog.findViewById(R.id.no);
        textView.setText(message);
        yesButton.setOnClickListener(v -> {
            select.onYes();
            dismiss();
        });

        noButton.setOnClickListener(v -> {
            select.onNo();
            dismiss();
        });

        return dialog;
    }

    public interface Select {
        void onYes();

        void onNo();
    }

}
