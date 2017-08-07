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
import android.widget.TextView;

import com.ntucap.itcm.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by ProgrammerYuan on 12/06/17.
 */

public class ITCMDialogFragment extends DialogFragment implements View.OnClickListener{

    private View.OnClickListener mConfirmListener, mCancelListener;
    private TextView mConfirmBtn, mCancelBtn, mTvHintText, mTvTitle;

    private static final int DEFAULT_LAYOUT_RES_ID = R.layout.dialog_common;
    public static final String DATA_KEY_TITLE = "title";
    public static final String DATA_KEY_HINT = "hint";
    public static final String DATA_KEY_CONFIRM = "confirm";
    public static final String DATA_KEY_CANCEL = "cancel";

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

        View view = inflater.inflate(DEFAULT_LAYOUT_RES_ID, null);
        mTvHintText = (TextView) view.findViewById(R.id.tv_content_dialog_common);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title_dialog_common);
        mConfirmBtn = (TextView) view.findViewById(R.id.tv_confirm_dialog_common);
        mCancelBtn = (TextView) view.findViewById(R.id.tv_cancel_dialog_common);
        if(mConfirmListener != null) mConfirmBtn.setOnClickListener(mConfirmListener);
        if(mCancelListener != null) mCancelBtn.setOnClickListener(mCancelListener);

        if (data != null) {
            mTvHintText.setText(data.getString(DATA_KEY_HINT, ""));
            mTvTitle.setText(data.getString(DATA_KEY_TITLE, ""));
            mConfirmBtn.setText(data.getString(DATA_KEY_CONFIRM, "Confirm"));
            mCancelBtn.setText(data.getString(DATA_KEY_CANCEL, "Cancel"));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.trans);
        return dialog;
    }

    public void setConfirmListener(View.OnClickListener confirmListener) {
        mConfirmListener = confirmListener;
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mCancelListener = cancelListener;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_confirm_dialog_common:
                if(mConfirmListener != null) mConfirmListener.onClick(v);
                dismiss();
                break;
            case R.id.tv_cancel_dialog_common:
                if(mCancelListener != null) mCancelListener.onClick(v);
                dismiss();
                break;
        }
    }
}
