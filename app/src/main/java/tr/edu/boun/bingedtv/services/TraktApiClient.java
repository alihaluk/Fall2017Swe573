package tr.edu.boun.bingedtv.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tr.edu.boun.bingedtv.services.restservices.RestConstants;

import static android.content.Context.MODE_PRIVATE;

public class TraktApiClient {

    public static Map<String, String> getHeaders(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("trakt-api-version", "2");
        params.put("trakt-api-key", RestConstants.clientID);

        SharedPreferences sp = context.getSharedPreferences("credentials", MODE_PRIVATE);
        String accessToken = sp.getString("access_token", "");
        if (!accessToken.isEmpty()) {
            params.put("Authorization", "Bearer " + accessToken);
        }

        return params;
    }

    public static TraktJsonObjectRequest getRequest(Context context, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        return new TraktJsonObjectRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    public static TraktJsonObjectRequest postRequest(Context context, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        return new TraktJsonObjectRequest(context, Request.Method.POST, url, jsonRequest, listener, errorListener);
    }

    public static TraktJsonArrayRequest getArrayRequest(Context context, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        return new TraktJsonArrayRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    public static class TraktJsonArrayRequest extends JsonArrayRequest {
        private final Context context;

        public TraktJsonArrayRequest(Context context, int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.context = context;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return TraktApiClient.getHeaders(context);
        }
    }

    public static class TraktJsonObjectRequest extends JsonObjectRequest {
        private final Context context;

        public TraktJsonObjectRequest(Context context, int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.context = context;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return TraktApiClient.getHeaders(context);
        }
    }
}
