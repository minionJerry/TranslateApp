package com.test.translateapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.translateapp.DatabaseHelper;
import com.test.translateapp.Network;
import com.test.translateapp.R;
import com.test.translateapp.models.LangList;
import com.test.translateapp.models.TextModel;
import com.test.translateapp.models.TranslatingResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateFragment extends Fragment {

    static final String KEYAPI = "trnsl.1.1.20170316T165017Z.c83d6fdb6fc20c53.22b2b05fe8551fc2c266acaddcecfe116f37d818";
    TextView finalLangBtn;
    TextView outputTxtView;
    EditText inputEditTxt;
    ImageView pic;
    ImageView addToFavBtn;
    HashMap<String,String> listOfLangCodes;
    String finalLang;
    String sourceText;
    String translatedText;
    DatabaseHelper db;
    SharedPreferences prefs;
    final String sharedPrefKey ="chosenLang";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorTranslateStatus));
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarTranslate);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTranslate));

        prefs = getActivity().getSharedPreferences(
                "chosenLanguage", Context.MODE_PRIVATE);

        finalLangBtn = (Button)view.findViewById(R.id.translateToBtn);
        inputEditTxt = (EditText)view.findViewById(R.id.editTextToType);
        outputTxtView = (TextView)view.findViewById(R.id.resultTextView);
        pic = (ImageView)view.findViewById(R.id.arrowPic);
        finalLang = null;
        //langList = new ArrayList<>();
        listOfLangCodes = createMap();
        addToFavBtn = (ImageView) view.findViewById(R.id.addToFavBtn);
        db = new DatabaseHelper(getContext());

        getListOfLanguages();

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceText = inputEditTxt.getText().toString();
                if (sourceText.isEmpty() || finalLang==null)  {
                    Toast.makeText(getActivity(),"Input text or choose language before translating.",Toast.LENGTH_SHORT).show();
                } else {
                    translateText();
                }
            }
        });

        addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Insert: ", "Inserting ..");
                TextModel textModel = db.getText(sourceText,translatedText,finalLang);
                if (textModel!= null && textModel.isFavorite()!=1) {
                    db.changeToPositiveStatus(textModel);
                    Toast.makeText(getContext(),"Item is added to favorites successfully",Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(),"You have to translate text first",Toast.LENGTH_SHORT).show();
            }
        });

        db.close();
        return view;
    }

    private void getListOfLanguages(){
        Network.getInterface().getLangList("en",KEYAPI).enqueue(new Callback<LangList>()  {
            @Override
            public void onResponse(Call<LangList> call, Response<LangList> response) {
                List<String> list = new ArrayList<>(response.body().getLangs());

                final String[] langArr = new String[3];
                for (int i=0;i< langArr.length;i++){
                    langArr[i] = list.get(i).toString();
                }
                final int langPosition = getLangPosition(langArr);

                finalLangBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Select destination language")
                                .setSingleChoiceItems(langArr, langPosition, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        finalLang = langArr[item].toString();
                                        // save chosen language position in sharedPref
                                        prefs.edit().putString(sharedPrefKey,finalLang).apply();
                                        dialog.dismiss();
                                        finalLangBtn.setText(finalLang);
                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(Call<LangList> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private int getLangPosition(String[] langlist){
        final String chosenLang = prefs.getString(sharedPrefKey,null);
        if (chosenLang!=null){
            for (int i=0;i<langlist.length;i++){
                if (langlist[i].equals(chosenLang)){
                    finalLangBtn.setText(chosenLang);
                    return i;
                }
            }
        }
        return -1;
    }

    private void translateText(){
        String lang =  getLangCode();
        if (lang.isEmpty()){
            Toast.makeText(getContext(),"Choose language",Toast.LENGTH_LONG).show();
            return;
        }

        Network.getInterface().doTranslate(KEYAPI,sourceText,lang,1).enqueue(new Callback<TranslatingResponse>() {
            @Override
            public void onResponse(Call<TranslatingResponse> call, Response<TranslatingResponse> response) {
                if (response.body().getCode()==200){
                    translatedText = response.body().getText();
                    outputTxtView.setText(translatedText);
                    //add record to table
                    db.addText(new TextModel(sourceText,translatedText,finalLang,0));
                }
                else Toast.makeText(getContext(),"Cannot translate. Something went wrong.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<TranslatingResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private static HashMap<String,String> createMap(){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("English","en");
        map.put("Kyrgyz","ky ");
        map.put("Russian","ru");
        return map;
    }

    private String getLangCode(){
       try {
           for (String key : listOfLangCodes.keySet()) {
               if (key.equals(finalLang))
                   return listOfLangCodes.get(key);
           }
       }catch (Exception e) {
         Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
       }
        return  null;
    }
}
