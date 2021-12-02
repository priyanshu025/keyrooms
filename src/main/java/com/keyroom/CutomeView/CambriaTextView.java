package com.keyroom.CutomeView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.keyroom.R;

public class CambriaTextView extends AppCompatTextView {

    public CambriaTextView(Context context) {
        super(context);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.cambria);
        this.setTypeface(typeface);
    }

    public CambriaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.cambria);
        this.setTypeface(typeface);
    }

    public CambriaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.cambria);
        this.setTypeface(typeface);
    }
}
