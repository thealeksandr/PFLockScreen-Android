package com.beautycoder.pflockscreen;

import android.content.Context;
import androidx.test.espresso.Espresso;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;

import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.rules.FragmentTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by aleksandr on 2018/03/10.
 */
@RunWith(AndroidJUnit4.class)
public class PFLockScreenFragmentAuthTest {

    private static final String LEFT_BUTTON = "Can't remember";
    private static final int CODE_LENGTH = 5;

    @Rule
    public FragmentTestRule<PFLockScreenFragment> mFragmentTestRule
            = new FragmentTestRule<PFLockScreenFragment>(PFLockScreenFragment.class) {
        @Override
        public PFLockScreenFragment getInstance(Context context) {
            final PFLockScreenFragment fragment = new PFLockScreenFragment();
            final PFFLockScreenConfiguration.Builder builder =
                    new PFFLockScreenConfiguration.Builder(context)
                            .setCodeLength(CODE_LENGTH)
                            .setLeftButton(LEFT_BUTTON, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setUseFingerprint(true)
                            .setMode(PFFLockScreenConfiguration.MODE_AUTH);
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
        Espresso.onView(withId(R.id.button_finger_print)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_delete)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.button_left)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_next)).check(matches(not(isDisplayed())));

        Espresso.onView(withId(R.id.button_left)).check(matches(withText(LEFT_BUTTON)));


        for (int i = 0; i < CODE_LENGTH; i++) {
            Espresso.onView(withId(R.id.button_1)).perform(click());
            Espresso.onView(withId(R.id.button_finger_print)).check(matches(not(isDisplayed())));
            Espresso.onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
        }

        for (int i = 0; i < CODE_LENGTH - 1; i++) {
            Espresso.onView(withId(R.id.button_delete)).perform(click());
            Espresso.onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
            Espresso.onView(withId(R.id.button_finger_print)).check(matches(not(isDisplayed())));
        }

        Espresso.onView(withId(R.id.button_delete)).perform(click());
        Espresso.onView(withId(R.id.button_finger_print)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_delete)).check(matches(not(isDisplayed())));


        Espresso.onView(withId(R.id.button_next)).check(matches(not(isDisplayed())));


    }


}
