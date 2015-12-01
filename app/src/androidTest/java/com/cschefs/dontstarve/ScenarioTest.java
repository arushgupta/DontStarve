package com.cschefs.dontstarve;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.ListView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


@RunWith(AndroidJUnit4.class)
public class ScenarioTest extends ActivityInstrumentationTestCase2<MainActivity>{


    public ScenarioTest() {super(MainActivity.class);}

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        getActivity();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testEnterInNewIngredient() throws Exception{
        // Test searching for recipes using button without ingredients
        onView(withId(R.id.recipe_menu)).perform(click());
        // Go back to main menu
        onView(withId(R.id.home_menu)).perform(click());

        // Click Search Button
        onView(withId(R.id.search_menu)).perform(click());

        // This view is in a different Activity, no need to tell Espresso.
        // Click to add new ingredient
        onView(withId(R.id.new_button_search)).perform(click());

        // Type in new item to add - chicken
        onView(withId(R.id.new_item_edit)).perform(typeText("chicken"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        // Add chicken to ingredients list
        onView(withId(R.id.input_search)).perform(typeText("chicken"),closeSoftKeyboard());
        onView(withId(R.id.add_button_search)).perform(click());

        // Check that chicken is there
        onView(withText("chicken")).check(matches(isDisplayed()));

        // Search for ingredients
        onView(withId(R.id.find_button)).perform(click());

        // Go back to main menu
        onView(withId(R.id.home_menu)).perform(click());


        // Clear ingredient
        onView(withId(R.id.clear_button)).perform(click());
        onView(withText("Yes")).perform(click());


    }
}
