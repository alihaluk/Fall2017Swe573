package tr.edu.boun.bingedtv.services.restservices;

/**
 * Created by haluks on 27.11.2017.
 */

public class RestConstants
{
    public static String baseServiceAddress = "https://api.trakt.tv/";
    public static String clientID = "YOUR_CLIENT_ID";
    public static String clientSecret = "YOUR_CLIENT_SECRET";
    public static String redirectUri = "urn:ietf:wg:oauth:2.0:oob";

    /**
     * Status Codes
     * Code	Description
     200	Success
     201	Success - new resource created (POST)
     204	Success - no content to return (DELETE)
     400	Bad Request - request couldn't be parsed
     401	Unauthorized - OAuth must be provided
     403	Forbidden - invalid API key or unapproved app
     404	Not Found - method exists, but no record found
     405	Method Not Found - method doesn't exist
     409	Conflict - resource already created
     412	Precondition Failed - use application/json content type
     422	Unprocessible Entity - validation errors
     429	Rate Limit Exceeded
     500	Server Error
     503	Service Unavailable - server overloaded (try again in 30s)
     504	Service Unavailable - server overloaded (try again in 30s)
     520	Service Unavailable - Cloudflare error
     521	Service Unavailable - Cloudflare error
     522	Service Unavailable - Cloudflare error
     */
}
