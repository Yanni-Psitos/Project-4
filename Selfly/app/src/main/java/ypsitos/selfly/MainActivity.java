package ypsitos.selfly;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import ypsitos.selfly.instagram.ApplicationData;
import ypsitos.selfly.instagram.InstagramApp;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.loginFab);

        final InstagramApp mApp = new InstagramApp(this,
                ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET,
                ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
                Intent toNextActivity = new Intent(MainActivity.this, BeginningActivity.class);
                startActivity(toNextActivity);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, "Failed To Login!", Toast.LENGTH_SHORT).show();
                ShowcaseView.Builder showCaseBuilder = new ShowcaseView.Builder(MainActivity.this);
                showCaseBuilder.setTarget(new ViewTarget(fab));
                showCaseBuilder.setContentTitle("WELCOME TO SELFLY!               Please Try Again!");
                showCaseBuilder.setContentText("Please Click And Login To Instagram To Allow Selfly To Access Your Photos! Without Access, We Cant See Your Beautiful Face!");
                showCaseBuilder.build();

            }
        });

        if (!mApp.hasAccessToken()) {
            ShowcaseView.Builder showCaseBuilder = new ShowcaseView.Builder(MainActivity.this);
            showCaseBuilder.setTarget(new ViewTarget(fab));
            showCaseBuilder.setContentTitle("WELCOME TO SELFLY!               Before We Get Started..");
            showCaseBuilder.setContentText("Please Click And Login To Instagram To Allow Selfly To Access Your Photos!");
            showCaseBuilder.build();
        }else if(mApp.hasAccessToken()){

            Intent toBeginningActivity = new Intent(MainActivity.this,BeginningActivity.class);
            startActivity(toBeginningActivity);
            Toast.makeText(MainActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();

        }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mApp.authorize();

//                Intent toNextActivity = new Intent(MainActivity.this, BeginningActivity.class);
//                startActivity(toNextActivity);
                }
            });

        }
    }
