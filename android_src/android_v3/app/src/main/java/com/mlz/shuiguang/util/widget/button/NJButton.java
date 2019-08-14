package com.mlz.shuiguang.util.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.widget.Button;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.util.Global;

/**
 * Created by dehualai on 12/25/16.
 */

public class NJButton extends Button {


    private final float DECREMENT_FACTOR = .1f;

    private float buttonHeight;
    private float cornerRadius;
    private int borderWidth;
    private int borderColor;
    private int backgroundColor;
    private int backgroundColorPressed;
    private int button_theme;


    public final static int THEME_DEFAULT = 0;
    public final static int THEME_TOTAL_TRANSPARENT = 1 ;
    public final static int THEME_WHITE = 2;
    public final static int THEME_WHITE_OUTLINE = 3;


    Context context;

    public NJButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context, attrs);
    }

    public NJButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public NJButton(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }


    public void updateTheme(int borderColor, int backgroundColor, int backgroundColorPressed){
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.backgroundColorPressed = backgroundColorPressed;
    }

    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.NJButton);
            button_theme = typedArray.getInt(R.styleable.NJButton_button_theme, 0);
            cornerRadius = typedArray.getFloat(R.styleable.NJButton_corner_radius, 0);
            buttonHeight = typedArray.getDimension(R.styleable.NJButton_button_height, 0);
            borderWidth = typedArray.getInt(R.styleable.NJButton_border_width, 0);
            borderColor = typedArray.getColor(R.styleable.NJButton_border_color, 0);
            backgroundColor = typedArray.getColor(R.styleable.NJButton_background_color, 0);
            backgroundColorPressed = typedArray.getColor(R.styleable.NJButton_background_color_pressed, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        // assign default values
        setThemeValue();
        this.setBackgroundDrawable(this.getSelectorDrawable());

    }

    public void setButtonTheme(int button_theme){
        this.button_theme = button_theme;
        setThemeValue();
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Global.APP_FONT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.setBackgroundDrawable(getSelectorDrawable());
        super.onDraw(canvas);
    }


    public StateListDrawable getSelectorDrawable() {
        StateListDrawable out = new StateListDrawable();
        out.addState(new int[]{android.R.attr.state_pressed}, createPressedDrawable());
        out.addState(StateSet.WILD_CARD, createNormalDrawable());
        return out;
    }

    public GradientDrawable createNormalDrawable() {
        final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();
        float radius;
        if (height <= 0) {
            radius = buttonHeight * cornerRadius;
        } else {
            radius = Math.min(height, width) * cornerRadius;
        }
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(backgroundColor);
        gd.setCornerRadius(radius);
        gd.setStroke(borderWidth, borderColor);
        return gd;
    }

    public GradientDrawable createPressedDrawable() {
        final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();
        float radius;
        if (height <= 0) {
            radius = buttonHeight * cornerRadius;
        } else {
            radius = Math.min(height, width) * cornerRadius;
        }
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(backgroundColorPressed);
        gd.setCornerRadius(radius);
        gd.setStroke(borderWidth, borderColor);
        return gd;
    }


    private void setThemeValue() {
        switch (button_theme) {
            case THEME_DEFAULT:
                if (borderColor <= 0) {
                    borderColor = context.getResources().getColor(R.color.theme_minor_color);
                }
                if (backgroundColor <= 0) {
                    backgroundColor = context.getResources().getColor(R.color.theme_minor_color);
                }
                if (backgroundColorPressed <= 0) {
                    backgroundColorPressed = context.getResources().getColor(R.color.theme_minor_color_2);
                }
                break;
            case THEME_TOTAL_TRANSPARENT:
                if (borderColor <= 0) {
                    borderColor = context.getResources().getColor(R.color.transparent);
                }
                if (backgroundColor <= 0) {
                    backgroundColor = context.getResources().getColor(R.color.transparent);
                }
                if (backgroundColorPressed <= 0) {
                    backgroundColorPressed = context.getResources().getColor(R.color.transparent);
                }
                break;
            case THEME_WHITE:
                if (borderColor <= 0) {
                    borderColor = context.getResources().getColor(R.color.white);
                }
                if (backgroundColor <= 0) {
                    backgroundColor = context.getResources().getColor(R.color.white);
                }
                if (backgroundColorPressed <= 0) {
                    backgroundColorPressed = context.getResources().getColor(R.color.njbutton_lightgray);
                }
                break;
            case THEME_WHITE_OUTLINE:
                if (borderColor <= 0) {
                    borderColor = context.getResources().getColor(R.color.white);
                }
                if (backgroundColor <= 0) {
                    backgroundColor = context.getResources().getColor(R.color.transparent);
                }
                if (backgroundColorPressed <= 0) {
                    backgroundColorPressed = context.getResources().getColor(R.color.transparent);
                }
                break;
            default:
                if (borderColor <= 0) {
                    borderColor = context.getResources().getColor(R.color.theme_minor_color);
                }
                if (backgroundColor <= 0) {
                    backgroundColor = context.getResources().getColor(R.color.theme_minor_color);
                }
                if (backgroundColorPressed <= 0) {
                    backgroundColorPressed = context.getResources().getColor(R.color.theme_minor_color_2);
                }
                break;
        }
        if (cornerRadius <= 0) {
            cornerRadius = 0.5f;
        }
        if (borderWidth <= 0) {
            borderWidth = 5;
        }
    }


    // auto adjust text


    private int convertPixelToDp(float pixel) {
        float TenDpPixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int targetDp = (int) (pixel / TenDpPixel * 10);
        return targetDp;
    }

    private synchronized void refitText(String text, int textWidth) {

        if (textWidth > 0) {

            // deal with multi-line text
            if (text.contains("\n")){
                String[] lines = text.split("\n");
                String longestLine = "";
                for (int i = 0 ; i < lines.length; i ++){
                    if (lines[i].length() >= longestLine.length()){
                        longestLine = lines[i];
                    }
                }
                text = longestLine;
            }


            float availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight();

            TextPaint tp = getPaint();
            Rect rect = new Rect();
            tp.getTextBounds(text, 0, text.length(), rect);
            float size = rect.width();


            while (size > availableWidth) {
                int currentDp = convertPixelToDp(getTextSize());
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, currentDp - 1);
                tp = getPaint();
                tp.getTextBounds(text, 0, text.length(), rect);
                size = rect.width();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        refitText(this.getText().toString(), parentWidth);

        if (parentWidth < getSuggestedMinimumWidth())
            parentWidth = getSuggestedMinimumWidth();

        if (parentHeight < getSuggestedMinimumHeight())
            parentHeight = getSuggestedMinimumHeight();

        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        super.onTextChanged(text, start, before, after);

        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw)
            refitText(this.getText().toString(), w);
    }

}