package com.example.kylez.calculator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

public class MainActivity extends AppCompatActivity {

    TextView calcDisplay;
    CalculatorModel model;
    Boolean splashVisible = false;

    public void resizeLayouts()
    {
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float density  = getResources().getDisplayMetrics().density;
//        float dpHeight = outMetrics.heightPixels / density;
//        float dpWidth  = outMetrics.widthPixels / density;
//
//        float contentPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)(dpHeight * 0.25), getResources().getDisplayMetrics());
//        float numpadPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)(dpHeight * 0.73), getResources().getDisplayMetrics());
//
//        LinearLayout view_instance = (LinearLayout)findViewById(R.id.what);
//        ViewGroup.LayoutParams params = view_instance.getLayoutParams();
//        params.height = (int)contentPixels;
//        view_instance.setLayoutParams(params);
//
//        FrameLayout numpadLayout = (FrameLayout)findViewById(R.id.test);
//
//        ViewGroup.LayoutParams params2 = numpadLayout.getLayoutParams();
//        params2.height = (int)numpadPixels;
//        numpadLayout.setLayoutParams(params2);
//
//        TextView text = (TextView)findViewById(R.id.calc_display);
//        ViewGroup.LayoutParams params3 = text.getLayoutParams();
//        params3.height = (int)contentPixels;
//
//        text.setTextSize(dpWidth/5);
//        text.setLayoutParams(params3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.calcDisplay = (TextView)findViewById(R.id.calc_display);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.equalButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOperation();
                performSplash();
            }
        });

        model = new CalculatorModel();

                Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        Log.v("DPs",dpHeight+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear) {
            this.calcDisplay.setText("0");
            model.computeQueue.clear();
            if(splashVisible) {
                removeSplash();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonTouched(View v)
    {
        String str = (String) v.getTag();

        if(this.calcDisplay.getText().equals("0"))
        {
            this.calcDisplay.setText("");
        }

        String newText = calcDisplay.getText()+str;
        this.calcDisplay.setText(newText);

        model.computeQueue.add(str);

        if(splashVisible)
        {
            removeSplash();
        }
    }

    public void backTouched(View v)
    {
        if(model.computeQueue.size() > 0)
        {
            model.computeQueue.removeFirst();
        }

        if(calcDisplay.getText().length() > 0)
        {
            calcDisplay.setText(((String) calcDisplay.getText()).substring(1));
        }

        if(calcDisplay.getText().length() == 0)
        {
            calcDisplay.setText("0");
        }

        if(splashVisible)
        {
            removeSplash();
        }
    }


    public void performSplash()
    {
        final View calcDisplay = findViewById(R.id.splash);
        calcDisplay.setVisibility(View.VISIBLE);

        int cx = (calcDisplay.getLeft() + calcDisplay.getRight());
        int cy = calcDisplay.getBottom();

        int radius = Math.max(calcDisplay.getWidth(), calcDisplay.getHeight());

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(calcDisplay, cx, cy, 0, radius);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
        splashVisible = true;
    }

    public void removeSplash()
    {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        final View splash = findViewById(R.id.splash);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splash.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        splash.startAnimation(fadeInAnimation);

        splashVisible = false;
    }


    public void doOperation()
    {
        String result = model.computeResult();
        calcDisplay.setText(result);
        model.computeQueue.add(result);
    }


}
