package com.mlz.shuiguang.util.widget.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mlz.shuiguang.util.Global;

/**
 * Created by dehualai on 1/8/17.
 */



public class NJEditText extends EditText {

    public NJEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NJEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NJEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Global.APP_FONT);
            setTypeface(tf);
        }
    }

}
