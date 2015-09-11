package com.app.demos.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.app.demos.R;

/**
 * Created by tom on 15-9-5.
 */
public class RoundProgressBar extends View {
    private int a;
    private int b;
    private int c;
    private int d;
    private Paint e;
    private Paint f;
    private RectF g;

    public RoundProgressBar(Context paramContext)
    {
        this(paramContext, null);
    }

    public RoundProgressBar(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public RoundProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        a();
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundProgressBar, paramInt, 0);
        setMax(localTypedArray.getInt(0, this.b));
        setProgress(localTypedArray.getInt(1, this.c));
        setSize(localTypedArray.getDimensionPixelSize(2, this.d));
        localTypedArray.recycle();
        this.e = new Paint();
        this.e.setColor(this.a);
        this.e.setAntiAlias(true);
        this.e.setStyle(Paint.Style.FILL);
        this.f = new Paint();
        this.f.setColor(-1);
        this.f.setAntiAlias(true);
        this.f.setStyle(Paint.Style.STROKE);
        this.f.setStrokeWidth(8.0F);
    }

    private void a()
    {
        this.a = Color.argb(125, 0, 0, 0);
        this.b = 100;
        this.c = 0;
        this.d = 80;
    }

    public int getMax()
    {
        return this.b;
    }

    public int getProgress()
    {
        return this.c;
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        int i = this.d / 2;
        paramCanvas.drawCircle(i, i, i, this.e);
        if (this.g == null)
            this.g = new RectF(14.0F, 14.0F, -14 + i + i, -14 + i + i);
        paramCanvas.drawArc(this.g, 270.0F, 360 * this.c / this.b, false, this.f);
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
        setMeasuredDimension(resolveSize(this.d, paramInt1), resolveSize(this.d, paramInt2));
    }

    public void setMax(int paramInt)
    {
        if (paramInt < 0)
            paramInt = 0;
        if (paramInt == this.b)
            return;
        this.b = paramInt;
        if (this.c > paramInt)
            this.c = paramInt;
        invalidate();
    }

    public void setProgress(int paramInt)
    {
        if (paramInt < 0)
            paramInt = 0;
        if (paramInt > this.b)
            paramInt = this.b;
        if (paramInt == this.c)
            return;
        this.c = paramInt;
        invalidate();
    }

    public void setSize(int paramInt)
    {
        if (paramInt < 0)
            paramInt = 0;
        if (paramInt == this.d)
            return;
        this.d = paramInt;
        this.g = null;
        requestLayout();
    }
}