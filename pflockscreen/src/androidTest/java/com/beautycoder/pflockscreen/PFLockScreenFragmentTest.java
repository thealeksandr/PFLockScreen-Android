package com.beautycoder.pflockscreen;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by aleksandr on 2018/02/27.
 */
@RunWith(AndroidJUnit4.class)
public class PFLockScreenFragmentTest {

    @Rule
    public FragmentTestRule<PFLockScreenFragment> mFragmentTestRule
            = new FragmentTestRule<>(PFLockScreenFragment.class);


    @Test
    public void fragment_can_be_instantiated() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        Espresso.onView(withId(R.id.fragment_pf)).check(matches(isDisplayed()));
    }

}
