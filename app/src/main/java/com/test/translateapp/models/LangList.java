package com.test.translateapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Каныкей on 16.03.2017.
 */
public class LangList {
    @SerializedName("langs")
    @Expose
    private Lang langs;
    @SerializedName("dirs")
    @Expose
    private String[] dirs;

    public List<String> getLangs ()
    {
        return langs.returnLangs();
    }

    public void setLangs (Lang langs)
    {
        this.langs = langs;
    }

    public String[] getDirs ()
    {
        return dirs;
    }

    public void setDirs (String[] dirs)
    {
        this.dirs = dirs;
    }



//    @Override
//    public String toString()
//    {
//        return langs.toString();
//    }

}
