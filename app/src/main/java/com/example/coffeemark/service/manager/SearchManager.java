package com.example.coffeemark.service.manager;

import android.util.Log;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.service.search.SearchResponse;

public class SearchManager {
    public static void search(Search search, SearchRequest request) {
        // Викликаємо метод з ApiHelper
        ApiHelper.search(request, new ApiHelper.ApiCallback<SearchResponse>() {
            @Override
            public void onSuccess(SearchResponse response) {
                Object message = response.getMessage();
                Log.e("RegisterActivity", "Message: " + message);

                if (response.isSuccess()) {
                    search.onSuccess(message);

                } else {
                    search.onError(message);
                }
            }

            @Override
            public void onError(String errorMessage, int code) {
                Log.e("RegisterActivity", "HTTP-код помилки: " + code);
                Log.e("RegisterActivity", "Повідомлення: " + errorMessage);

                search.onError(errorMessage);
            }
        });
    }

    public interface Search {
        void onSuccess(Object message);

        void onError(Object message);
    }
}
