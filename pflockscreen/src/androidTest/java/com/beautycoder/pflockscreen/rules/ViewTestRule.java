package com.beautycoder.pflockscreen.rules;

import android.content.Context;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.view.ViewGroup;

import com.beautycoder.pflockscreen.activities.TestActivity;

/**
 * Created by aleksandr on 2018/03/11.
 */

public abstract class ViewTestRule <V extends View> extends ActivityTestRule<TestActivity> {

    private final Class<V> mViewClass;
    private V mView;

    public ViewTestRule(final Class<V> fragmentClass) {
        super(TestActivity.class, true, false);
        mViewClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //try {
                //Instantiate and insert the fragment into the container layout
                //FragmentManager manager = getActivity().getSupportFragmentManager();
                //FragmentTransaction transaction = manager.beginTransaction();
                mView = getView(getActivity()); //mFragmentClass.newInstance();
                ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mView.setLayoutParams(layoutParams);
                viewGroup.addView(mView);
                //transaction.replace(android.R.id.content, mFragment);
                //transaction.commit();
                /*} catch (InstantiationException | IllegalAccessException e) {
                    Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                            getClass().getSimpleName(),
                            mFragmentClass.getSimpleName(),
                            e.getMessage()));
                }*/
            }
        });
    }

    public abstract V getView(Context context);


}
