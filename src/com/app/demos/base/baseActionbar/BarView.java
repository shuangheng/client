package com.app.demos.base.baseActionbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-9-30.
 */
public class BarView {
    private ImageView home;
    private TextView title;
    private View titleParent;
    private TextView subtitle;
    private LinearLayout rightView;

    public BarView(View paramView)
    {
        this.home = ((ImageView)paramView.findViewById(R.id.action_bar_home));
        this.title = ((TextView)paramView.findViewById(R.id.action_bar_title));
        this.subtitle = ((TextView)paramView.findViewById(R.id.action_bar_subtitle));
        this.titleParent = paramView.findViewById(R.id.action_bar_title_block);
        this.rightView = ((LinearLayout)paramView.findViewById(R.id.action_bar_actions));
    }

    public ImageView getHomeView()
    {
        return this.home;
    }

    public TextView getTitleView()
    {
        return this.title;
    }

    public View getTitleParentView()
    {
        return this.titleParent;
    }

    public TextView getSubTitilView()
    {
        return this.subtitle;
    }

    public LinearLayout getRightView()
    {
        return this.rightView;
    }
}