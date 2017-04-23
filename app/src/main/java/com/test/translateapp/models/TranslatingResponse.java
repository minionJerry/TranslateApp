package com.test.translateapp.models;

/**
 * Created by Каныкей on 02.04.2017.
 */
public class TranslatingResponse {
    private String[] text;

    private int code;

    private String lang;

    public String getText ()
    {
        String newText = "";
        for (String word : text)
           newText += word;
        return newText;
    }

    public void setText (String[] text)
    {
        this.text = text;
    }

    public int getCode ()
    {
        return code;
    }

    public void setCode (int code)
    {
        this.code = code;
    }

    public String getLang ()
    {
        return lang;
    }

    public void setLang (String lang)
    {
        this.lang = lang;
    }

}
