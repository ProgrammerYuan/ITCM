package com.ntucap.itcm.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ntucap.itcm.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by ProgrammerYuan on 17/07/17.
 */

public class ITCMLoadingDialog extends DialogFragment {

    private AVLoadingIndicatorView mLoadingView;
    private TextView mTvHint;

    private static final int DEFAULT_LAYOUT_RES_ID = R.layout.dialog_loading;

    public static ITCMLoadingDialog newInstance(Bundle data) {
        ITCMLoadingDialog dialogFragment = new ITCMLoadingDialog();
        dialogFragment.setArguments(data);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(DEFAULT_LAYOUT_RES_ID, null);
        mLoadingView = (AVLoadingIndicatorView) view.findViewById(R.id.loading_dialog_loading);
        mTvHint = (TextView) view.findViewById(R.id.tv_hint_dialog_loading);

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
        mLoadingView.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(
                getResources().getDimensionPixelOffset(R.dimen.dialog_default_width),
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoadingView.hide();
    }
}
