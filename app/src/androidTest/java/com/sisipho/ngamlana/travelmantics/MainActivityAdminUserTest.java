package com.sisipho.ngamlana.travelmantics;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;

public class MainActivityAdminUserTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private static List<TravelDeal> travelDealList;

    @Before
    public void setup() {
        travelDealList = FireBaseUtil.travelDealList;
    }

    @Test
    public void onResume() {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withText(R.string.menu_send)).check(matches(isEnabled()));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));

        for (int index = 0; index < travelDealList.size(); index++) {
            TravelDeal travelDeal = travelDealList.get(index);

            onView(withId(R.id.recyclerView_trade_deals)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
            onView(withId(R.id.textView_title)).check(matches(withText(travelDeal.getTitle()))).check(matches(not(isEnabled())));
            onView(withId(R.id.textView_description)).check(matches(withText(travelDeal.getDescription()))).check(matches(not(isEnabled())));
            onView(withId(R.id.textView_price)).check(matches(withText(travelDeal.getPrice()))).check(matches(not(isEnabled())));

            pressBack();
        }
    }

    @Test
    public void onCreateOptionsMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.text_logout)).check(matches(isDisplayed()));
    }
}