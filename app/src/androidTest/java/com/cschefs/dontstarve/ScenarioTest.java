/* Scenario Tests
 * Scenario 1: Adding a new ingredient to item list and basket
 * Given the home page is open
 * When the user clicks the search button
 * And the user clicks New Item
 * And the user types "chicken"
 * And the user clicks Add
 * And the user type chicken into the search bar
 * And the user clicks Add
 * Then the app will go back to the home screen and chicken will be displayed
 *
 * Scenario 2: Clear button
 * Given that the home page is open
 * And chicken is added to the basket
 * When the user clicks the Clear All button
 * And the user clicks Yes
 * Then the basket will be empty
 *
 * Scenario 3: Searching for recipes with empty basket
 * Given that the home page is open
 * And the basket is empty
 * When the user clicks Find Recipes
 * Then the app will go the the recipe screen and the top 30 recipes will be dislayed
 */

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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
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


    //Scenario Test 1: Add a new ingredient to item list and basket
    @Test
    public void testEnterInNewIngredient() throws Exception{
        // Given the home page is open
        // When the user clicks the search button
        onView(withId(R.id.search_menu)).perform(click());

        // And the user clicks New Item
        onView(withId(R.id.new_button_search)).perform(click());

        // And the user types "chicken"
        // And the user clicks Add
        onView(withId(R.id.new_item_edit)).perform(typeText("chicken"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        // And the user type chicken into the search bar
        // And the user clicks Add
        onView(withId(R.id.input_search)).perform(typeText("chicken"),closeSoftKeyboard());
        onView(withId(R.id.add_button_search)).perform(click());

        // Then the app will go back to the home screen and chicken will be displayed
        onView(withText("chicken")).check(matches(isDisplayed()));

    }

    //Scenario Test 2: Adds multiple ingredients to the list
    @Test
    public void testSelectIngredient() throws Exception {
        //Given the home page is open
        //When the user clicks the search button
        onView(withId(R.id.search_menu)).perform(click());

        //And the user selects an ingredient from the provided list by tapping on an ingredient
        //And the user clicks Add
        onData(anything()).inAdapterView(withId(R.id.list_ingredients_search)).atPosition(0).perform(click());
        onView(withId(R.id.add_button_search)).perform(click());

        //And when the user clicks on the search button again it redirects the user back to search
        onView(withId(R.id.search_menu)).perform(click());

        // And the user clicks New Item
        onView(withId(R.id.new_button_search)).perform(click());

        // And the user types "lemon"
        // And the user clicks Add
        onView(withId(R.id.new_item_edit)).perform(typeText("lemon"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        // And the user type chicken into the search bar
        // And the user clicks Add
        onView(withId(R.id.input_search)).perform(typeText("lemon"),closeSoftKeyboard());
        onView(withId(R.id.add_button_search)).perform(click());

        // Then the app will go back to the home screen and all of the added items will be displayed
        onView(withText("onions")).check(matches(isDisplayed()));
        onView(withText("lemon")).check(matches(isDisplayed()));
    }

    //Scenario Test 3: Searching for recipes with multiple items
    @Test
    public void testFindRecipeMultipleItems() throws Exception{
        // Given that the home page is open
        // And the basket has multiple items
        // When the user clicks Find Recipes
        onView(withId(R.id.find_button)).perform(click());

        // Then the app will go the the recipe screen and the top 30 recipes will be displayed
        onView(withId(R.id.list_recipes)).check(matches(isDisplayed()));
    }

    //Scenario Test 4: Clear using long tap
    @Test
    public void testClearTap() throws Exception{
        // Given that the home page is open
        // And chicken is added to the basket
        // When the user clicks the Clear All button
        onData(anything()).inAdapterView(withId(R.id.list_ingredients)).atPosition(0).perform(longClick());

        // And the user clicks Yes
        onView(withText("Delete")).perform(click());
    }

    //Scenario Test 5: Clear All Button
    @Test
    public void testClearButton() throws Exception{
        // Given that the home page is open
        // And chicken is added to the basket

        // When the user clicks the Clear All button
        onView(withId(R.id.clear_button)).perform(click());

        // And the user clicks Yes
        onView(withText("Yes")).perform(click());

        // Then the basket will be empty
        onView(withId(R.id.list_ingredients)).check(matches(not(isDisplayed())));
    }

    //Scenario Test 6: Searching for recipes with empty basket and going to a recipe page
    @Test
    public void testFindRecipeEmptyBasket() throws Exception{
        //Given that the home page is open
        //And the basket is empty
        //And the user clicks Find Recipes
        onView(withId(R.id.find_button)).perform(click());

        //Checks presence of a list of recipes
        onView(withId(R.id.list_recipes)).check(matches(isDisplayed()));

        //When the user does a long tap
        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(0).perform(longClick());

        //And clicks View Recipe
        onView(withText("View Recipe")).perform(click());

        //Then the user is redirected to the recipe's web page
    }
}
