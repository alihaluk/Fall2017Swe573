package tr.edu.boun.bingedtv.controls;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class LoginActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), TraktAuthActivity.class);
                startActivityForResult(i, 101);

                TraktService.getInstance(getApplicationContext()).Authorize();
            }
        });

        // control credentials
        SharedPreferences sp = getApplicationContext().getSharedPreferences("credentials", MODE_PRIVATE);
        if (!sp.getString("access_token","").isEmpty())
        {
            // have credentials nav to main.
            navToMain();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 101)
        {
            if (resultCode == RESULT_OK)
            {
                // nav to main
                navToMain();
            }
        }
    }

    private void navToMain()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
