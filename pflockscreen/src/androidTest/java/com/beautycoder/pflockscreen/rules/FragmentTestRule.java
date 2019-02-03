package com.beautycoder.pflockscreen.rules;


import android.content.Context;
import androidx.test.rule.ActivityTestRule;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.beautycoder.pflockscreen.activities.TestActivity;

/**
 * Created by Aleksandr Nikiforov on 2018/02/27.
 */

public abstract class FragmentTestRule <F extends Fragment> extends ActivityTestRule<TestActivity> {

    private final Class<F> mFragmentClass;
    private F mFragment;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(TestActivity.class, true, false);
        mFragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //try {
                    //Instantiate and insert the fragment into the container layout
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    mFragment = getInstance(getActivity()); //mFragmentClass.newInstance();
                    transaction.replace(android.R.id.content, mFragment);
                    transaction.commit();
                /*} catch (InstantiationException | IllegalAccessException e) {
                    Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                            getClass().getSimpleName(),
                            mFragmentClass.getSimpleName(),
                            e.getMessage()));
                }*/
            }
        });
    }

    public abstract F getInstance(Context context);


    public F getFragment(){
        return mFragment;
    }


}
