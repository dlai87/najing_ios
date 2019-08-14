package com.mlz.shuiguang.util.widget.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mlz.shuiguang.util.Global;

/**
 * Created by dehualai on 1/8/17.
 */

public class NJTextView extends TextView {



        public NJTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public NJTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public NJTextView(Context context) {
            super(context);
            init();
        }

        private void init() {
            if (!isInEditMode()) {
               // Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fang_zheng_you_xian.ttf");
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), Global.APP_FONT);
                setTypeface(tf);
            }
        }


}
