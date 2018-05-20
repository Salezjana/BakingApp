package mrodkiewicz.pl.bakingapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {


    @Test
    public void clickListViewItem() {
        onView(allOf(withId(R.id.recipe_recycleviewlist),
                isDisplayed()));

    }


}