package com.beautycoder.pflockscreen.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.beautycoder.pflockscreen.views.PFCodeView;

import org.hamcrest.Description;

/**
 * Created by aleksandr on 2018/03/10.
 */

public class PFCodeViewMatcher extends BoundedMatcher<View, PFCodeView> {

    public PFCodeViewMatcher(Class<? extends PFCodeView> expectedType) {
        super(expectedType);
    }

    public PFCodeViewMatcher(Class<?> expectedType, Class<?> interfaceType1,
                             Class<?>[] otherInterfaces) {
        super(expectedType, interfaceType1, otherInterfaces);
    }

    @Override
    protected boolean matchesSafely(PFCodeView item) {
        return false;
    }

    @Override
    public void describeTo(Description description) {

    }

}
