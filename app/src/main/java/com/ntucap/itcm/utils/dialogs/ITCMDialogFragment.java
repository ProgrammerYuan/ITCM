package com.ntucap.itcm.utils.dialogs;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Loader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ntucap.itcm.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.ArrayList;


/**
 * Created by ProgrammerYuan on 12/06/17.
 */

public class ITCMDialogFragment extends DialogFragment {

    private static final int DEFAULT_LAYOUT_RES_ID = R.layout.dialog_common;

    public static ITCMDialogFragment newInstance(Bundle data) {
        ITCMDialogFragment dialogFragment = new ITCMDialogFragment();
        dialogFragment.setArguments(data);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        Bundle data = getArguments();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.dialog_common, null);

        if (data != null) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.trans);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DIALOG LOG:", "ONSTART!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
