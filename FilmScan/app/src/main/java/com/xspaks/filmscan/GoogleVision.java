package com.xspaks.filmscan;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.xspaks.filmscan.api.GoogleVisionAPI;
import com.xspaks.filmscan.api.RetrofitClient;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoogleVision {
    public static Void request(Bitmap imageBitmap) {

        Retrofit retrofit = new RetrofitClient("https://vision.googleapis.com/").getRetrofitInstance();
        GoogleVisionAPI visionAPI = retrofit.create(GoogleVisionAPI.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP);

        String requestBody = "{\n" +
                "  \"requests\": [\n" +
                "    {\n" +
                "      \"image\": {\n" +
                "        \"content\": \"" + base64Image + "\"\n" +
                "      },\n" +
                "      \"features\": [\n" +
                "        {\n" +
                "          \"type\": \"LABEL_DETECTION\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Convert the String to RequestBody
        RequestBody requestBodyObject = RequestBody.create(MediaType.parse("application/json"), requestBody);

        // Send the request to the API
        Call<ResponseBody> call = visionAPI.annotateImage(requestBodyObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GoogleVision", "Response: " + responseBody);
                    } catch (IOException e) {
                        Log.e("GoogleVision", "Error reading response body", e);
                    }
                } else {
                    Log.e("GoogleVision", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GoogleVision", "Request failed: " + t.getMessage());
            }
        });
        return null;
    }
}

