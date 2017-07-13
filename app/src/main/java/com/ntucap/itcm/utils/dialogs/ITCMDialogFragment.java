package com.ntucap.itcm.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.ntucap.itcm.R;


/**
 * Created by ProgrammerYuan on 12/06/17.
 */

public class ITCMDialogFragment extends DialogFragment {

    private static final int DEFAULT_LAYOUT_RES_ID = R.layout.dialog_common;

    public ITCMDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        LayoutInflater inflater = LayoutInflater.from(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(DEFAULT_LAYOUT_RES_ID);
        return builder.create();
    }
}
