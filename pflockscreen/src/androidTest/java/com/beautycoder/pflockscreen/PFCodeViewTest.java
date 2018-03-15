package com.beautycoder.pflockscreen;

import android.content.Context;

import com.beautycoder.pflockscreen.rules.ViewTestRule;
import com.beautycoder.pflockscreen.views.PFCodeView;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by aleksandr on 2018/03/11.
 */

public class PFCodeViewTest {

    private static final int CODE_LENGTH = 5;

    @Rule
    public ViewTestRule<PFCodeView> mViewTestRule
            = new ViewTestRule<PFCodeView>(PFCodeView.class) {
        @Override
        public PFCodeView getView(Context context) {
            PFCodeView view = new PFCodeView(context);
            view.setCodeLength(CODE_LENGTH);
            return view;
        }
    };

    @Test
    public void pfCodeViewTest() {

    }

}
