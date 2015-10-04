package com.app.demos.base.baseActionbar;

/**
 * Created by tom on 15-9-30.
 */
public class Model
{
    private String TAG;
    private int imageId;
    private CharSequence text;
    private boolean d;

    public Model(String paramString, int paramInt, CharSequence paramCharSequence)
    {
        this.TAG = paramString;
        this.imageId = paramInt;
        this.text = paramCharSequence;
    }

    public Model(String paramString, CharSequence paramCharSequence)
    {
        this.TAG = paramString;
        this.text = paramCharSequence;
    }

    public String getTAG()
    {
        return this.TAG;
    }

    public void a(boolean paramBoolean)
    {
        this.d = paramBoolean;
    }

    public int getImageId()
    {
        return this.imageId;
    }

    public CharSequence getText()
    {
        return this.text;
    }

    public boolean d()
    {
        return this.d;
    }
}