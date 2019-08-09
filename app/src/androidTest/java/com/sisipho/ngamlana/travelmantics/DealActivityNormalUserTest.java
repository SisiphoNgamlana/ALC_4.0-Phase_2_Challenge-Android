package com.sisipho.ngamlana.travelmantics;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class DealActivityNormalUserTest {

    @Rule
    public ActivityTestRule<DealActivity> dealActivityActivityTestRule = new ActivityTestRule<>(DealActivity.class);

    @Test
    public void onCreate() {
        onView(withId(R.id.textView_title)).check(matches(not(isEnabled()))).check(matches(isDisplayed()));
        onView(withId(R.id.textView_description)).check(matches(not(isEnabled()))).check(matches(isDisplayed()));
        onView(withId(R.id.textView_price)).check(matches((not(isEnabled())))).check(matches(isDisplayed()));
    }
}