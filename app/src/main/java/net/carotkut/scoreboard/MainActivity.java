package net.carotkut.scoreboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    AllPlayers allPlayers = new AllPlayers();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, allPlayers);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    FavPlayers favPlayers = new FavPlayers();
                    FragmentTransaction fFragmentTransaction = getFragmentManager().beginTransaction();
                    fFragmentTransaction.replace(R.id.fragmentContainer, favPlayers);
                    fFragmentTransaction.commit();
                    return true;
                default:
                    loadDefaultTab();
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDefaultTab();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void loadDefaultTab()
    {
        AllPlayers allPlayers = new AllPlayers();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, allPlayers);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        AllPlayers allPlayers = new AllPlayers();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, allPlayers);
        fragmentTransaction.commit();
    }
}
