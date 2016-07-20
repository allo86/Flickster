package com.allo.flickster.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by ALLO on 18/7/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG_LOG = this.getClass().getCanonicalName();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

}
