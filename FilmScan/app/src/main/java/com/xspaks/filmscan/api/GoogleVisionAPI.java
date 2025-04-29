package com.xspaks.filmscan.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GoogleVisionAPI {
    @POST("v1/images:annotate?key=AIzaSyCGj5C2xpdkWY-APejR3XjbACSxxaNJO5k")
    Call<ResponseBody> annotateImage(@Body RequestBody body);
}

