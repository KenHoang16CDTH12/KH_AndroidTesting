package com.sun.kh_androidtesting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sun.kh_androidtesting.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.not;
import static android.app.Instrumentation.ActivityResult;

/**
 * Espresso tests for {@link IntentsAdvandcedActivity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntentsAdvancedTest {

    /**
     * A JUnit {@link Rule @Rule} to init and release Espresso Intents before and after each
     * test run.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * This rule is based on {@link ActivityTestRule} and will create and launch of the activity
     * for you and also expose the activity under test.
     */
    @Rule
    public IntentsTestRule<IntentsAdvandcedActivity> mIntentsRule = new IntentsTestRule<>(IntentsAdvandcedActivity.class);

    @Before
    public void stubCameraIntent() {
        ActivityResult result = createImageCaptureActivityResultStub();
        // Stub the Intent
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }

    @Test
    public void takePhoto_drawableIsApplied() {
        // Check that the ImageView doesn't have a drawable applied.
        onView(withId(R.id.imageView)).check(matches(not(hasDrawable())));
        // Click on the button that will trigger the stubbed intent.
        onView(withId(R.id.button_take_photo)).perform(click());
        // With no user interaction, the ImageView will have a drawable.
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
    }

    private ActivityResult createImageCaptureActivityResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentsAdvandcedActivity.KEY_IMAGE_DATA, BitmapFactory.decodeResource(
                mIntentsRule.getActivity().getResources(), R.drawable.ic_launcher_foreground
        ));
        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new ActivityResult(Activity.RESULT_OK, resultData);
    }
}
