package com.test.translateapp.models;

/**
 * Created by Каныкей on 17.04.2017.
 */
public class TextModel {
    private int id;
    private String text;
    private String translateText;
    private String lang;
    private int isFavorite;

    public int isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(int favorite) {
        this.isFavorite = favorite;
    }

    public TextModel(){}

    public TextModel(int id, String text, String translateText, String lang, int isFav) {
        this.id = id;
        this.text = text;
        this.translateText = translateText;
        this.lang = lang;
        isFavorite = isFav;
    }

    public TextModel(String text, String translateText, String lang, int isFav) {
        this.text = text;
        this.translateText = translateText;
        this.lang = lang;
        isFavorite = isFav;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslateText() {
        return this.translateText;
    }

    public void setTranslateText(String translateText) {
        this.translateText = translateText;
    }
}
