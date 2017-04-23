package com.test.translateapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Каныкей on 17.03.2017.
 */
public class Lang {

    private String ky;

    private String en;

    private String ru;

    public String getKy ()
    {
        return ky;
    }

    public void setKy (String pl)
    {
        this.ky = pl;
    }

    public String getEn ()
    {
        return en;
    }

    public void setEn (String en)
    {
        this.en = en;
    }

    public String getRu ()
    {
        return ru;
    }

    public void setRu (String ru)
    {
        this.ru = ru;
    }

    public List<String> returnLangs()
    {
        return new ArrayList<String>(){{add(ky);add(en);add(ru);}};
    }
}
