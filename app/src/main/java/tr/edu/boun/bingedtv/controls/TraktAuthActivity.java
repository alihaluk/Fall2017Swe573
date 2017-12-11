package tr.edu.boun.bingedtv.controls;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import tr.edu.boun.bingedtv.R;
import tr.edu.boun.bingedtv.services.restservices.RestConstants;
import tr.edu.boun.bingedtv.services.restservices.TraktService;

public class TraktAuthActivity extends AppCompatActivity
{
    EditText edtPinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trakt_auth);

        edtPinCode = (EditText) findViewById(R.id.edt_pinCode);

        Button btnVerifyCode = (Button) findViewById(R.id.btn_verifyCode);
        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (edtPinCode.getText() != null && edtPinCode.getText().length() > 0)
                    GetToken(edtPinCode.getText().toString());
            }
        });
    }

    public void GetToken(String pinCode)
    {
        /**
         * {
         "code": "fd0847dbb559752d932dd3c1ac34ff98d27b11fe2fea5a864f44740cd7919ad0",
         "client_id": "9b36d8c0db59eff5038aea7a417d73e69aea75b41aac771816d2ef1b3109cc2f",
         "client_secret": "d6ea27703957b69939b8104ed4524595e210cd2e79af587744a7eb6e58f5b3d2",
         "redirect_uri": "urn:ietf:wg:oauth:2.0:oob",
         "grant_type": "authorization_code"
         }
         */

        String url = "https://api.trakt.tv/oauth/token";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("code", pinCode);
        params.put("client_id", RestConstants.clientID);
        params.put("client_secret", RestConstants.clientSecret);
        params.put("redirect_uri", RestConstants.redirectUri);
        params.put("grant_type", "authorization_code");

        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Process os success response
                        try
                        {
                            Log.d("", response.getString("access_token"));
                            String access_token = response.getString("access_token");
                            String refresh_token = response.getString("refresh_token");

                            // save tokens persistent
                            SharedPreferences sp = getApplicationContext().getSharedPreferences("credentials", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("access_token", access_token);
                            editor.putString("refresh_token", refresh_token);
                            editor.apply();

                        } catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("",error.getMessage());
            }
        });

        TraktService.getInstance(this).addToRequestQueue(request_json);
    }
}
