package com.tuanjie.client;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    // below is mainly android logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("debug.mono.log", "");
        super.onCreate(savedInstanceState);

        // Init render service instance

        // In layout, we create a tuanjieView
        setContentView(R.layout.main_layout);

        // Create and add teh first TuanjieView programmatically

        // Setup buttons
        configureButton();

        // Setup spinner
    }

    private void configureButton() {
        Button startRenderService = findViewById(R.id.start_renderservice);
        startRenderService.setOnClickListener(view -> {
            Log.d(TAG, "start render service");
            // Start render service
        });

        Button stopRenderService = findViewById(R.id.stop_renderservice);
        stopRenderService.setOnClickListener(view -> {
            Log.d(TAG, "stop render service");
            // Stop render service
        });

        Button addView = findViewById(R.id.add_tuanjieview);
        addView.setOnClickListener(view -> {
            Log.d(TAG, "add tuanjieview");
            // Check if already exceeds 8 views

            // Add new tuanjie view

        });

        Button removeView = findViewById(R.id.remove_tuanjieview);
        removeView.setOnClickListener(view -> {
            Log.d(TAG, "remove tuanjieview");
            // Check if there are any TuanjieViews to remove

            // Get the last added TuanjieView

            // Remove the TuanjieView from its parent
        });

        // Remove the TuanjieView from our list

        // Redistribute layout for remaining views
    }
}