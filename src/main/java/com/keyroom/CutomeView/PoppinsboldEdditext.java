package com.keyroom.CutomeView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.keyroom.R;

public class PoppinsboldEdditext extends AppCompatEditText {
    public PoppinsboldEdditext(Context context) {
        super(context);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppinsbold);
        this.setTypeface(typeface);
    }

    public PoppinsboldEdditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppinsbold);
        this.setTypeface(typeface);
    }

    public PoppinsboldEdditext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppinsbold);
        this.setTypeface(typeface);
    }
}
