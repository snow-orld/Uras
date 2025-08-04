package com.tuanjie.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.renderservice.client.TuanjieView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<TuanjieView> mAllTuanjieViews = new ArrayList<>();
    TuanjieView mTuanjieViewFromXML;
    Spinner mTargetDisplaySpinner;
    Spinner mCarColorSpinner;
    private static final String TAG = "URASClient";
    private static final String servicePackageName = "com.tuanjie.renderservice";
    private final RenderService mRenderService = new RenderService(servicePackageName);

    private void changeTargetDisplay(TuanjieView targetView, int i) {
        // Change tuanjieView's target display
        // The targetdisplay of tuanjieview corresponds to the target display of the camera in the Editor,
        // now counting from 0 to 7.
        // Eg. "targetView.changeDisplay(0);" will make targetView output the
        // content of the camera whose target display is 0 in Tuanjie Editor.

        // If two tuanjieViews have the same target display, only one will get rendered.
        if (targetView != null)
            targetView.changeDisplay(i);
    }

    private TuanjieView createTuanjieView() {
        TuanjieView tuanjieView = new TuanjieView(this, servicePackageName, mAllTuanjieViews.size(), 1, 0, true);
        // init tuanjieView before used
        // args: ServicePkgName, DisplayIndex, RenderInterval, ViewType, EnablePersistentSurface
        // If two tuanjieView has the same display index, only one will get rendered.
        // DisplayIndex now starts from 0
        tuanjieView.init(servicePackageName, mAllTuanjieViews.size(), 1, 0, true);
        return tuanjieView;
    }

    // below is mainly android logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("debug.mono.log", "");
        super.onCreate(savedInstanceState);

        // Init render service instance
        mRenderService.init(this);

        // In layout, we create a tuanjieView
        setContentView(R.layout.main_layout);

        // Create and add the first TuanjieView programmatically
        mTuanjieViewFromXML = createTuanjieView();
        LinearLayout view1 = findViewById(R.id.view1);
        view1.addView(mTuanjieViewFromXML);
        mAllTuanjieViews.add(mTuanjieViewFromXML);

        // Setup buttons
        configureButton();

        // Setup spinner
        configureTargetDisplaySpinner(mTuanjieViewFromXML, R.id.spinnerTargetDisplay1);
    }

    private void configureButton() {
        Button startRenderService = findViewById(R.id.start_renderservice);
        startRenderService.setOnClickListener(view -> {
            Log.d(TAG, "start render service");
            // Start render service
            mRenderService.start();
        });

        Button stopRenderService = findViewById(R.id.stop_renderservice);
        stopRenderService.setOnClickListener(view -> {
            Log.d(TAG, "stop render service");
            // Stop render service
            mRenderService.stop();
        });

        Button addView = findViewById(R.id.add_tuanjieview);
        addView.setOnClickListener(view -> {
            Log.d(TAG, "add tuanjieview");
            // Check if already exceeds 8 views
            if (mAllTuanjieViews.size() >= 8) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("Maximum TuanjieView Limit Reached");
                normalDialog.setMessage("You can create up to 8 TuanjieViews (display0-7), each linked to a unique target display.");
                normalDialog.setNegativeButton("OK", null);
                normalDialog.show();
                return;
            }

            // Add new tuanjie view
            TuanjieView tuanjieView = createTuanjieView();
            mAllTuanjieViews.add(tuanjieView);
            // Congire Spinner to modify targetDisplay of tuanjieView.Displaying usage of API
            int id = mAllTuanjieViews.size() == 2 ? R.id.spinnerTargetDisplay2 :R.id.spinnerTargetDisplay3;
            configureTargetDisplaySpinner(tuanjieView, id);

            int idParentTuanjie = mAllTuanjieViews.size() == 2 ? R.id.view2 :R.id.view3;
            LinearLayout parent = findViewById(idParentTuanjie);
            parent.setVisibility(View.VISIBLE);
            parent.addView(tuanjieView);

            // Redistribute layout for all views after adding a new one
            redistributeLayout();
        });

        Button removeView = findViewById(R.id.remove_tuanjieview);
        removeView.setOnClickListener(view -> {
            Log.d(TAG, "remove tuanjieview");
            // Check if there are any TuanjieViews to remove
            if (mAllTuanjieViews.size() <= 1) {
                // Only the XML-defined TuanjieView remains, can't remove it
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(MainActivity.this);
                normalDialog.setTitle("Cannot Remove View");
                normalDialog.setMessage("At least one TuanjieView must remain.");
                normalDialog.setNegativeButton("OK", null);
                normalDialog.show();
                return;
            }

            // Get the last added TuanjieView
            TuanjieView tuanjieViewToRemove = mAllTuanjieViews.get(mAllTuanjieViews.size() - 1);

            // Remove the TuanjieView from its parent
            ViewGroup parent = (ViewGroup) tuanjieViewToRemove.getParent();
            if (parent != null) {
                parent.removeView(tuanjieViewToRemove);

                // Set the specific container to GONE
                parent.setVisibility(View.GONE);
            }

            // Remove the TuanjieView from our list
            mAllTuanjieViews.remove(tuanjieViewToRemove);

            // Redistribute layout for remaining views
            redistributeLayout();
        });

    }

    // Use Spinner (defined by id) to control targetDisplay of targetView.
    private void configureTargetDisplaySpinner(TuanjieView targetView, int id) {
        // 8 target displays (0-7) can be used
        String[] availableTargetDisplays = {"display0", "display1", "display2", "display3", "display4", "display5", "display6", "display7"};
        // android spinner logic
        ArrayAdapter<String> dataAdapter  = new ArrayAdapter<String>(this, R.layout.spinner_item, availableTargetDisplays);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        mTargetDisplaySpinner = findViewById(id);
        mTargetDisplaySpinner.setPrompt("select target display");
        mTargetDisplaySpinner.setAdapter(dataAdapter);
        mTargetDisplaySpinner.setSelection(targetView.getDisplayIndex());
        mTargetDisplaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // tuanjieView's target display starts from 0;
                changeTargetDisplay(targetView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    /**
     * Redistributes layout for all remaining TuanjieViews and updates their display resolution
     */
    private void redistributeLayout() {
        // Get references to view containers
        LinearLayout view1 = findViewById(R.id.view1);
        LinearLayout view2 = findViewById(R.id.view2);
        LinearLayout view3 = findViewById(R.id.view3);

        // Set layout based on number of remaining views
        switch (mAllTuanjieViews.size()) {
            case 1:
                // Only XML-defined view remains, make it full screen
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1.0f
                );
                mTuanjieViewFromXML.setLayoutParams(params);

                // Hide view2 and view3
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                break;

            case 2:
                // Two views remain, adjust their layout weights
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        0.5f
                );
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        0.5f
                );

                mTuanjieViewFromXML.setLayoutParams(params1);
                mAllTuanjieViews.get(1).setLayoutParams(params2);

                // Make sure view2 is visible and view3 is gone
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.GONE);
                break;

            case 3:
                // Three views remain, adjust their layout weights
                LinearLayout.LayoutParams params1Three = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        0.33f
                );
                LinearLayout.LayoutParams params2Three = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        0.33f
                );
                LinearLayout.LayoutParams params3Three = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        0.33f
                );

                mTuanjieViewFromXML.setLayoutParams(params1Three);
                mAllTuanjieViews.get(1).setLayoutParams(params2Three);
                mAllTuanjieViews.get(2).setLayoutParams(params3Three);

                // Make sure both view2 and view3 are visible
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                break;
        }

        // Force refresh layout for all remaining views and update display resolution
        for (TuanjieView remainingView : mAllTuanjieViews) {
            ViewGroup parentLayout = (ViewGroup) remainingView.getParent();
            if (parentLayout != null) {
                // Request layout refresh
                remainingView.requestLayout();
                parentLayout.requestLayout();
            }

            // Update display resolution for this view
//            int displayIndex = remainingView.getTargetDisplay();
//            mRenderService.updateDisplayResolution(displayIndex);
        }
    }
}