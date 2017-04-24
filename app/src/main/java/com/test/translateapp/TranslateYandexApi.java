package com.test.translateapp;

import com.test.translateapp.models.LangList;
import com.test.translateapp.models.TranslatingResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateYandexApi {
    @POST("api/v1.5/tr.json/getLangs")
    Call<LangList> getLangList(@Query("ui") String lang, @Query("key") String apiKey);
    @POST("api/v1.5/tr.json/translate")
    Call<TranslatingResponse> doTranslate (@Query("key") String apiLKey,@Query("text") String text,@Query("lang") String lang,@Query("options") int option);
}
