package com.example.anshultech.miriambakery;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.example.anshultech.miriambakery.Activities.BakeryHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ActivityHomeInstrumentedTest {
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        final BakeryHome bakeryHomeActivity = startTestActivity(instrumentation);

        //mIdlingResource=  bakeryHomeActivity.getIdlingResource();

        mIdlingResource = bakeryHomeActivity.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void isViewDiplayed() {
        try {
            //Delay to have list available for test
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.recipiesMasterListRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.recipiesMasterListRecyclerView))
                .check(matches(hasDescendant(withText("Brownies"))));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    private BakeryHome startTestActivity(final Instrumentation instrumentation) {
        final Intent intent = new Intent(instrumentation.getTargetContext(), BakeryHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return (BakeryHome) instrumentation.startActivitySync(intent);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}