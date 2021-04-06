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

import com.example.booleancatastrophe.model.Experiment;
import com.example.booleancatastrophe.model.ExperimentType;
import com.example.booleancatastrophe.model.ForumQuestion;
import com.example.booleancatastrophe.model.User;

/**
 * This fragment is a dialog to publish new question for a specific experiment
 **/
public class NewForumQuestionFragment extends DialogFragment {

    /* Required empty public constructor */
    public NewForumQuestionFragment() {

    }

    /* Set up fragment UI elements and listener interface that needs to be implemented
     * by the activity */
    private EditText etQuestionContent;

    User user;
    private NewForumQuestionFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(String askedQuestionContent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof NewForumQuestionFragment.OnFragmentInteractionListener) {
            listener = (NewForumQuestionFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NewForumQuestionFragment.OnFragmentInteractionListener");
        }
    }

    /* Create dialog and link UI elements to it */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_new_forum_question, null);

        etQuestionContent = (EditText) view.findViewById(R.id.et_new_question_content);

        /* Build and return the dialog with listener on the OK button - will return a new
         * experiment on press */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle("Add Question to Experiment's Forum")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Publish Question", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /* As soon as OK is clicked, pass the new experiment with this dialog
                         * fragment's UI value selections */
                        String questionContent = etQuestionContent.getText().toString();

                        listener.onOkPressed(questionContent);
                    }
                }).create();
    }

}