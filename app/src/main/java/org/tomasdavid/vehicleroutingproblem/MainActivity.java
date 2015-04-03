package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getFragmentManager().beginTransaction().add(R.id.activity_main, fragment).commit();
        }
    }
}
