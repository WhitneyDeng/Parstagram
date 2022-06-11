package com.example.parstagram;

import android.app.Application;
import android.util.Log;

import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application
{
    public static final String TAG = "ParseApplication";

    @Override
    public void onCreate()
    {
        super.onCreate();

//        // Use for troubleshooting -- remove this line for production
//        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
//
//        // Use for monitoring Parse OkHttp traffic
//        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
//        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.networkInterceptors().add(httpLoggingInterceptor);

        // register parse model to be able to query & set data in this model
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id)) // should correspond to Application Id env variable
                .clientKey(getString(R.string.back4app_client_key))  // should correspond to Client key env variable
                .server(getString(R.string.back4app_server_url))
                .build());

//        // New test creation of object below
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }
}