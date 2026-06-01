package com.tuanjie.testrt;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.unity3d.renderservice.client.TuanjieView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final static String servicePackageName = "com.tuanjie.renderservice";
    private RenderService mRenderService = new RenderService(servicePackageName);
    private TuanjieView mTuanjieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        // 显示元素
        setContentView(R.layout.activity_main);
//        createButtons();

        // 启动URAS
        mRenderService.init(this);
        mRenderService.start();

        // 显示Tuanjie View 1
        TuanjieView mTuanjieView = createTuanjieView(1);
        ViewGroup view = findViewById(R.id.view);
        view.addView(mTuanjieView);
    }

    private void createButtons()
    {
        int buttonWidth = 300;
        int buttonHeight = 80;
        int buttonSpacing = 20;

        WindowMetrics windowMetrics = getWindowManager().getMaximumWindowMetrics();
        Rect bounds = windowMetrics.getBounds();
        int groupX = bounds.width() - buttonWidth - buttonSpacing;
        int groupY = (buttonHeight + buttonSpacing) * 2;

        Log.d(TAG, String.format("createButtons: screenWidth = %d\n", bounds.width()));

        Button btnShowView2 = new Button(this);
        btnShowView2.setText("Show Dis2");
        btnShowView2.setPadding(groupX, buttonSpacing, 0, 0);
        btnShowView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDisplay(1);
            }
        });

        Button btnShowView8 = new Button(this);
        btnShowView8.setText("Show Dis8");
        btnShowView8.setPadding(groupX, buttonHeight + buttonSpacing * 2, 0, 0);
        btnShowView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDisplay(7);
            }
        });

        ViewGroup parentView = findViewById(R.id.main);
        parentView.addView(btnShowView2, new ViewGroup.LayoutParams(buttonWidth, buttonHeight));
        parentView.addView(btnShowView8, new ViewGroup.LayoutParams(buttonWidth, buttonHeight));
    }

    private void switchDisplay(int i) {
        Log.d(TAG, "switchDisplay: " + i);

        // TODO
    }

    private TuanjieView createTuanjieView(int display) {
        TuanjieView tuanjieView = new TuanjieView(this, servicePackageName, display, 1, 0, true);
        tuanjieView.init(servicePackageName, display, 1, 0, true);
        return tuanjieView;
    }
}