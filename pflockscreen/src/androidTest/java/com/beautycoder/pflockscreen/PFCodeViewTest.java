package com.beautycoder.pflockscreen;

import android.content.Context;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.runner.AndroidJUnit4;

import com.beautycoder.pflockscreen.actions.PFCodeViewActionDelete;
import com.beautycoder.pflockscreen.actions.PFCodeViewActionInput;
import com.beautycoder.pflockscreen.matchers.PFViewMatchers;
import com.beautycoder.pflockscreen.rules.ViewTestRule;
import com.beautycoder.pflockscreen.views.PFCodeView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Aleksandr Nikiforov on 2018/03/11.
 */
@RunWith(AndroidJUnit4.class)
public class PFCodeViewTest {

    private static final int CODE_LENGTH = 5;

    @Rule
    public ViewTestRule<PFCodeView> mViewTestRule
            = new ViewTestRule<PFCodeView>(PFCodeView.class) {
        @Override
        public PFCodeView getView(Context context) {
            PFCodeView view = new PFCodeView(context);
            view.setCodeLength(CODE_LENGTH);
            view.setId(R.id.code_view);
            return view;
        }
    };

    @Test
    public void pfCodeViewTest() {

        mViewTestRule.launchActivity(null);

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeValue("")));

        for (int i = 0; i < CODE_LENGTH * 2; i ++) {
            Espresso.onView(withId(R.id.code_view)).perform(new PFCodeViewActionInput(i));
        }

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeValue("01234")));

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeLength(CODE_LENGTH)));

        Espresso.onView(withId(R.id.code_view)).perform(new PFCodeViewActionDelete());

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeValue("0123")));

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeLength(CODE_LENGTH - 1)));

        for (int i = 0; i < CODE_LENGTH * 3; i ++) {
            Espresso.onView(withId(R.id.code_view)).perform(new PFCodeViewActionDelete());
        }

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeLength(0)));

        Espresso.onView(withId(R.id.code_view)).check(ViewAssertions.matches(
                PFViewMatchers.withCodeValue("")));


    }

}
