package com.sisipho.ngamlana.travelmantics;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNot.not;


public class ListActivityTest {

    @Rule
    public ActivityTestRule<ListActivity> listActivityTestRule = new ActivityTestRule(ListActivity.class);
    private static List<TravelDeal> travelDealList;

    @Before
    public void setup() {
        travelDealList = FireBaseUtil.travelDealList;
    }

    @Test
    public void onResume() {

        for (int index = 0; index < travelDealList.size(); index++) {
            TravelDeal travelDeal = travelDealList.get(index);

            onView(withId(R.id.recyclerView_trade_deals)).perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
            onView(withId(R.id.textView_item_title)).check(matches(withText(travelDeal.getTitle())));
            onView(withId(R.id.textView_item_title)).check(matches(not(isEnabled())));
            onView(withId(R.id.textView_item_description)).check(matches(withText(travelDeal.getDescription())));
            onView(withId(R.id.textView_item_description)).check(matches(not(isEnabled())));
            onView(withId(R.id.textView_item_price)).check(matches(withText(travelDeal.getPrice())));
            onView(withId(R.id.textView_item_price)).check(matches(not(isEnabled())));

            pressBack();
        }
    }

    @Test
    public void onCreateOptionsMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.text_logout)).check(matches(isDisplayed()));
    }

}