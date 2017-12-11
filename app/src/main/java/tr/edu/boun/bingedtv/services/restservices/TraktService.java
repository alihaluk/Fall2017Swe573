package tr.edu.boun.bingedtv.services.restservices;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by haluks on 26.11.2017.
 */

public class TraktService
{
    private static TraktService mInstance;
    private static Context mContext = null;

    private RequestQueue mRequestQueue;

    public TraktService(Context ctx)
    {
        mContext = ctx;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized TraktService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TraktService(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void Authorize()
    {
        String url = "https://trakt.tv/"
                + "oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + RestConstants.clientID
                + "&redirect_uri=" + RestConstants.redirectUri;

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        mContext.startActivity(i);
    }
}
