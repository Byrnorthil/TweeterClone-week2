package com.codepath.apps.restclienttemplate.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.activities.ComposeActivity;

public class SaveDraftDialogueFragment extends DialogFragment {

    public interface OnSaveDraftListener {
        void onSaveDraft(boolean saved);
    }

    public SaveDraftDialogueFragment() {
        //needed for library
    }

    public static SaveDraftDialogueFragment newInstance() {
        SaveDraftDialogueFragment frag = new SaveDraftDialogueFragment();
        Bundle args = new Bundle();
        args.putString("title", "Save draft?");
        args.putString("message", "Do you want to save this tweet as a draft? If you do not save, it will be discarded.");
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        final OnSaveDraftListener listener = (OnSaveDraftListener) getActivity();
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onSaveDraft(true);
                dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onSaveDraft(false);
                dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}
