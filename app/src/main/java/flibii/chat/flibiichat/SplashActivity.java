package flibii.chat.flibiichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Splash Screen :

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent LoginIntent = new Intent(SplashActivity.this,LoginActivity.class);

                    startActivity(LoginIntent);
                }

            }
        };
        thread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
