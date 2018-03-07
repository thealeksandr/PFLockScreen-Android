package com.beautycoder.pflockscreen;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


/**
 * Created by aleksandr on 2018/02/27.
 */
@RunWith(AndroidJUnit4.class)
public class PFLockScreenFragmentTest {

    private static final String LEFT_BUTTON = "Can't remember";

    @Rule
    public FragmentTestRule<PFLockScreenFragment> mFragmentTestRule
            = new FragmentTestRule<PFLockScreenFragment>(PFLockScreenFragment.class) {
        @Override
        public PFLockScreenFragment getInstance(Context context) {
            PFLockScreenFragment fragment = new PFLockScreenFragment();
            PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(context)
                    .setTitle("Unlock with your pin code or fingerprint")
                    .setCodeLength(6)
                    .setLeftButton(LEFT_BUTTON, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setUseFingerprint(true)
                    .setMode(PFFLockScreenConfiguration.MODE_CREATE);
            fragment.setConfiguration(builder.build());
            return fragment;
        }
    };


    @Test
    public void fragment_can_be_instantiated() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        Espresso.onView(withId(R.id.fragment_pf)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_finger_print)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.button_delete)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.button_left)).check(matches(not(isDisplayed())));

        Espresso.onView(withId(R.id.button_left)).check(matches(withText(LEFT_BUTTON)));



    }

}
