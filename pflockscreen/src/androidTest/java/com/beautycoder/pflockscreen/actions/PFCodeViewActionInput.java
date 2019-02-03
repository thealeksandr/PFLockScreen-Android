package com.beautycoder.pflockscreen.actions;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;

import com.beautycoder.pflockscreen.views.PFCodeView;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by Aleksandr Nikiforov on 2018/03/19.
 */

public class PFCodeViewActionInput implements ViewAction {

    private int mInput;

    public PFCodeViewActionInput(int code) {
        mInput = code;
    }

    @Override
    public Matcher<View> getConstraints(){
        return isAssignableFrom(PFCodeView.class);
    }


    @Override
    public String getDescription(){
        return "Input code " + mInput;
    }

    @Override
    public void perform(UiController uiController, View view){
        PFCodeView codeView = (PFCodeView) view;
        codeView.input(String.valueOf(mInput));
    }

}
