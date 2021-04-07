package com.example.booleancatastrophe;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * This fragment is a dialog to publish new reply for a specific question
 **/
public class NewForumReplyFragment extends DialogFragment {

    /* Required empty public constructor */
    public NewForumReplyFragment() {}

    /* Set up fragment UI and listener interface that needs to be implemented by the activity */
    private EditText etReplyContent;

    private NewForumReplyFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(String replyContent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof NewForumReplyFragment.OnFragmentInteractionListener) {
            listener = (NewForumReplyFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NewForumReplyFragment.OnFragmentInteractionListener");
        }
    }

    /* Create dialog and link UI elements to it */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_new_forum_reply, null);

        etReplyContent = (EditText) view.findViewById(R.id.et_new_reply_content);

        /* Build and return the dialog with listener on the OK button - will return a new
         * experiment on press */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle("Reply To This Question")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add Reply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /* As soon as OK is clicked, pass the new experiment with this dialog
                         * fragment's UI value selections */
                        String strReply = etReplyContent.getText().toString();

                        listener.onOkPressed(strReply);
                    }
                }).create();
    }

}