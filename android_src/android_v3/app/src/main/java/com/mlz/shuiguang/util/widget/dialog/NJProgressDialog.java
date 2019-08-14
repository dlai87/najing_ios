package com.mlz.shuiguang.util.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.mlz.shuiguang.R;


/**
 * Created by dehualai on 1/14/17.
 */



public class NJProgressDialog extends Dialog {


    public static NJProgressDialog show(Context context){
        return show(context, "", "");
    }

    public static NJProgressDialog show(Context context, CharSequence title,
                                        CharSequence message) {
        return show(context, title, message, false);
    }

    public static NJProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static NJProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static NJProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate,
                                        boolean cancelable, OnCancelListener cancelListener) {
        NJProgressDialog dialog = new NJProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        ProgressBar progressBar = new ProgressBar(context);
        dialog.addContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }




    public NJProgressDialog(Context context) {
        super(context, R.style.NewDialog);
    }
}