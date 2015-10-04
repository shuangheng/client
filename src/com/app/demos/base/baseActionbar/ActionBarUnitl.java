package com.app.demos.base.baseActionbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tom on 15-9-30.
 */
public class ActionBarUnitl implements View.OnClickListener
{
    private Context context;
    private InputMethodManager inputMethodManager;
    private BarView barView;
    private List<Model> list;
    private OnxxClick onxxClick;
    private boolean aBoolean;
    private Model model;
    //private PupWindow pupWindow;
    private Model model2;

    public ActionBarUnitl(View paramView, Context paramContext, InputMethodManager paramInputMethodManager)
    {
        this.barView = new BarView(paramView);
        this.context = paramContext;
        this.inputMethodManager = paramInputMethodManager;
    }



    public void a(int paramInt)
    {
        this.barView.getHomeView().setImageResource(paramInt);
    }

    public void a(View.OnClickListener paramOnClickListener)
    {
        this.barView.getTitleParentView().setOnClickListener(paramOnClickListener);
    }

    public void addList(Model parama)
    {
        if (this.list == null) {
            this.list = new ArrayList();
        }
        if (!this.list.contains(parama)) {
            this.list.add(parama);
        }
    }



    public void a(String paramString)
    {
        this.barView.getTitleView().setText(paramString);
    }

    public void addMeunItem(String paramString, int paramInt, CharSequence paramCharSequence)
    {
        addList(new Model(paramString, paramInt, paramCharSequence));
    }



    public boolean a()
    {
        return this.aBoolean;
    }


    @Override
    public void onClick(View v) {

    }
}