package com.xspaks.filmscan.api;

public interface GoogleVisionResult {
    void onResult(String result);
    void onError(Throwable t);
}
