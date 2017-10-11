package com.swatcat.your_name.services.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.hawk.Hawk;
import com.swatcat.your_name.base.App;
import com.swatcat.your_name.services.Constants;
import com.swatcat.your_name.services.utils.Tools;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by max_ermakov on 9/20/17.
 */

public class RestService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static RestApi createRestService(){
        Gson gson = new GsonBuilder() //do we really need such customization?
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(App.getInstance().getContext().getString(R.string.base_url)); //TODO There will be our base endpoint

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder newBuilder = request.newBuilder();

                        String token = Hawk.get(Constants.TOKEN, "");
                        if (!Tools.isNullOrEmpty(token)) {
                            newBuilder.addHeader("Authorization", "Token " + token);
                            Timber.d("Authorization: Token " + token);
                        }
                        Request newRequest = newBuilder
                                .build();
                        Response response = null;
                        String newStringBody = "";
                        try {
                            response = chain.proceed(newRequest);
                            switch (response.code()) {
                                case HttpURLConnection.HTTP_FORBIDDEN:
                                    final String url = response.request().url().toString();
                                    if (url != null) {
                                        App.getInstance().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                    } else {
                                        Timber.e("Response without request URL! " + response);
                                    }
                                    break;
                            }
                            ResponseBody body = response.body();
                            Timber.d("HTTP " + response.code() + " URL=" + response.request().url().toString());
                            String bodyString = body.string();
                            Timber.d(bodyString);
                            if (bodyString.startsWith("[")) {
                                newStringBody = "{\"data\":" + bodyString + "}";
                            } else {
                                newStringBody = bodyString;
                            }
                            Timber.d(newStringBody);
                            final Response.Builder newResponse = response.newBuilder()
                                    .body(ResponseBody.create(JSON, newStringBody));
                            response = newResponse.build();

                        } catch (IOException e) {
                            e.getLocalizedMessage();
                   /* RdxtApplication.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         //   RdxtApplication.getInstance().getConnectivityNotifier().notifyListeners(new ConnectivityStateChangedEvent());
                        }
                    });*/
                        }
                        return response;
                    }
                }).build();
        builder.client(client);

        return builder.build().create(RestApi.class);
    }
}
