package com.beautycoder.pflockscreen;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;

import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.rules.FragmentTestRule;
import com.beautycoder.pflockscreen.security.PFFingerprintPinCodeHelper;
import com.beautycoder.pflockscreen.security.PFSecurityException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by aleksandr on 2018/02/27.
 */
@RunWith(AndroidJUnit4.class)
public class PFLockScreenFragmentCreateTest {

    private static final int CODE_LENGTH = 5;

    @Rule
    public FragmentTestRule<PFLockScreenFragment> mFragmentTestRule
            = new FragmentTestRule<PFLockScreenFragment>(PFLockScreenFragment.class) {
        @Override
        public PFLockScreenFragment getInstance(Context context) {
            PFLockScreenFragment fragment = new PFLockScreenFragment();
            PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(context)
                    .setTitle("Unlock with your pin code or fingerprint")
                    .setCodeLength(CODE_LENGTH)
                    .setUseFingerprint(true)
                    .setMode(PFFLockScreenConfiguration.MODE_CREATE);
            fragment.setConfiguration(builder.build());
            return fragment;
        }
    };


    @Test
    public void fragment_can_be_instantiated() {
        try {
            PFFingerprintPinCodeHelper.getInstance().delete();
            boolean isAliasFalse = PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist();
            assertFalse(isAliasFalse);
        } catch (PFSecurityException e) {
            e.printStackTrace();
            assertFalse(true);
        }


        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        Espresso.onView(withId(R.id.fragment_pf)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_finger_print)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.button_delete)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.button_left)).check(matches(not(isDisplayed())));

        //INPUT CODE_LENGTH
        for (int i = 0; i < CODE_LENGTH; i ++) {
            Espresso.onView(withId(R.id.button_next)).check(matches(not(isDisplayed())));
            Espresso.onView(withId(R.id.button_1)).perform(click());
            Espresso.onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
        }

        //NEXT BUTTON SHOULD BE ON SCREEN
        Espresso.onView(withId(R.id.button_next)).check(matches(isDisplayed()));

        //DELETE WHOLE CODE
        for (int i = 0; i < CODE_LENGTH; i ++) {
            Espresso.onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
            Espresso.onView(withId(R.id.button_delete)).perform(click());
            Espresso.onView(withId(R.id.button_next)).check(matches(not(isDisplayed())));
        }

        //DELETE BUTTON HAS TO BE HIDDEN
        Espresso.onView(withId(R.id.button_delete)).check(matches(not(isDisplayed())));

        //INPUT CODE_LENGTH
        for (int i = 0; i < CODE_LENGTH; i ++) {
            Espresso.onView(withId(R.id.button_next)).check(matches(not(isDisplayed())));
            Espresso.onView(withId(R.id.button_1)).perform(click());
            Espresso.onView(withId(R.id.button_delete)).check(matches(isDisplayed()));
        }

        //NEXT BUTTON SHOULD BE ON SCREEN
        Espresso.onView(withId(R.id.button_next)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.button_next)).perform(click());


        try {
            boolean isAliasFalse = PFFingerprintPinCodeHelper.getInstance().isPinCodeEncryptionKeyExist();
            assertTrue(isAliasFalse);
        } catch (PFSecurityException e) {
            e.printStackTrace();
            assertFalse(true);
        }

    }


}
