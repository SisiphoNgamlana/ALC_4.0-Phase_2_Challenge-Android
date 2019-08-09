package com.sisipho.ngamlana.travelmantics;

import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

public class DealActivityAdminUserTest {

    @BeforeClass
    public static void setup2() {
        FireBaseUtil.isAdmin = true;
    }

    @Rule
    public ActivityTestRule<DealActivity> dealActivityActivityTestRule = new ActivityTestRule<>(DealActivity.class);

    @Test
    public void onCreate() {
        onView(withId(R.id.textView_title)).check(matches(isEnabled())).check(matches(isDisplayed()));
        onView(withId(R.id.textView_description)).check(matches(isEnabled())).check(matches(isDisplayed()));
        onView(withId(R.id.textView_price)).check(matches(isEnabled())).check(matches(isDisplayed()));
    }

    @Test
    public void onCreateOptionsMenu() {
        onView(withText(R.string.menu_save)).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_delete)).check(matches(isDisplayed()));
    }

}