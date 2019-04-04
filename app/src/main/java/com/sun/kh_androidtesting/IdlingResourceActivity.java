package com.sun.kh_androidtesting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.test.espresso.IdlingResource;
import com.sun.kh_androidtesting.utils.MessageDelayer;
import com.sun.kh_androidtesting.utils.SimpleIdlingResource;

/**
 * Gets a text String from the user and displays it back after a while.
 */
public class IdlingResourceActivity extends Activity implements View.OnClickListener,
        MessageDelayer.DelayerCallback {


    // The TextView used to display the message inside the Activity.
    private TextView mTextView;

    // The EditText where the user types the message.
    private EditText mEditText;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idling_resource_activity);

        // Set the listeners for the buttons.
        findViewById(R.id.changeTextBt).setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.textToBeChanged);
        mEditText = (EditText) findViewById(R.id.editTextUserInput);
    }

    @Override
    public void onClick(View view) {
        final String text = mEditText.getText().toString();

        if (view.getId() == R.id.changeTextBt) {
            // Set a temporary text.
            mTextView.setText(getString(R.string.waiting_message));
            // Submit the message to the delayer.
            MessageDelayer.processMessage(text, this, mIdlingResource);
        }
    }

    @Override
    public void onDone(String text) {
        // The delayer notifies the activity via a callback.
        mTextView.setText(text);
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null)
            mIdlingResource = new SimpleIdlingResource();
        return mIdlingResource;
    }
}
