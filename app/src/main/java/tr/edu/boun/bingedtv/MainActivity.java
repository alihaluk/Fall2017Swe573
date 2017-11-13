package tr.edu.boun.bingedtv;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            Fragment frag = null;
            switch(item.getItemId())
            {
                case R.id.navigation_home:
                    frag = ShowsFragment.newInstance(getString(R.string.hello_blank_fragment), "lists tv shows being watched");
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
            }

            if (frag != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.content, frag, frag.getTag());
                ft.commit();

                return true;
            }
            else {
                return false;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
