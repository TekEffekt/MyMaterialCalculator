package com.example.kylez.calculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity {

    TextView calcDisplay;
    CalculatorModel model;
    Boolean splashVisible = false;

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
