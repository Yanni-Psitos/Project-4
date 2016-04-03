package ypsitos.selfly.remote;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ypsitos.selfly.data.Datum;
import ypsitos.selfly.data.Images;
import ypsitos.selfly.instagram.InstagramAPIResults;

/**
 * Created by YPsitos on 3/31/16.
 */
public interface InstagramAPI {

    String BASE_URL = "https://api.instagram.com";

    @GET("/v1/users/self/media/recent/?access_token=9009673.1c62b9b.362ee1627c43443798b9e461f84f3d27")
    Call<InstagramAPIResults> getInstagram();

    class Factory {

        private static InstagramAPI service;

        public static InstagramAPI getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(InstagramAPI.class);
                return service;
            } else {
                return service;
            }
        }


    }
}
