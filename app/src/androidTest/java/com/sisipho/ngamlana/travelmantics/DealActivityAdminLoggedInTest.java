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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;

public class DealActivityAdminLoggedInTest {

    @BeforeClass
    public static void setup2() {
        FireBaseUtil.isAdmin = true;
    }

    @Rule
    public ActivityTestRule<DealActivity> dealActivityActivityTestRule = new ActivityTestRule(DealActivity.class);
    private static List<TravelDeal> travelDealList;

    @Before
    public void setup() {
        travelDealList = FireBaseUtil.travelDealList;
    }

    @Test
    public void onCreate() {
//        onView(withId(R.id.recyclerView_trade_deals)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
//        onView(withId(R.id.textView_title)).check(matches(withText(travelDeal.getTitle())));
//        onView(withId(R.id.textView_title)).check(matches(not(isEnabled())));
//        onView(withId(R.id.textView_description)).check(matches(withText(travelDeal.getDescription())));
//        onView(withId(R.id.textView_description)).check(matches(not(isEnabled())));
//        onView(withId(R.id.textView_price)).check(matches(withText(travelDeal.getPrice())));
//        onView(withId(R.id.textView_price)).check(matches(not(isEnabled())));

    }
//
//    @Test
//    public void onOptionsItemSelected() {
//    }

    @Test
    public void onCreateOptionsMenu() {
        onView(withText(R.string.menu_save)).check(matches(isDisplayed()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_delete)).check(matches(isDisplayed()));
    }

    @Test
    public void onActivityResult() {
    }

    @AfterClass
    public static void tearDown() {
        FireBaseUtil.isAdmin = false;
    }
}