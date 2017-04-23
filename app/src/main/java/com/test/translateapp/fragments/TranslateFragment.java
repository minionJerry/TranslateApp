package com.test.translateapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateFragment extends Fragment {

    private static final java.lang.String DIALOG_REPEAT_SETTING ="some tag" ;
    TextView finalLangBtn;
    EditText inputEditTxt;
    TextView outputTxtView;
    static final String KEYAPI = "trnsl.1.1.20170316T165017Z.c83d6fdb6fc20c53.22b2b05fe8551fc2c266acaddcecfe116f37d818";
    ArrayList<String> langList;
    String finalLang;
    String sourceText;
    String translatedText;
    HashMap<String,String> listOfLangCodes;
    ImageView pic;
    ImageView addToFavBtn;
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        finalLangBtn = (Button)view.findViewById(R.id.translateToBtn);
        inputEditTxt = (EditText)view.findViewById(R.id.editTextToType);
        outputTxtView = (TextView)view.findViewById(R.id.resultTextView);
        pic = (ImageView)view.findViewById(R.id.arrowPic);
        finalLang = null;
        langList = new ArrayList<>();
        listOfLangCodes = createMap();
        addToFavBtn = (ImageView) view.findViewById(R.id.addToFavBtn);
        db = new DatabaseHelper(getContext());

        // get list of languages and their codes
        getListOfLanguages();
        // fill array adapter with langList
        final ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,langList);

        finalLangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose final language")
                        .setSingleChoiceItems(langAdapter, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                //onclick
                                finalLang = langAdapter.getItem(item);
                                dialog.dismiss();
                                Toast.makeText(getActivity(),finalLang,Toast.LENGTH_SHORT).show();
                                finalLangBtn.setText(finalLang);

                            }
                        })
                        .show();

            }
        });

        // translate text to chosen language on click button
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
                // Inserting favorite item
                Log.d("Insert: ", "Inserting ..");
                TextModel textModel = db.getText(sourceText,translatedText,finalLang);
                if (textModel!= null && textModel.isFavorite()!=1) {
                    db.changeIsFavoriteStatus(textModel);
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
                langList.addAll(response.body().getLangs());
            }

            @Override
            public void onFailure(Call<LangList> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void translateText(){
        String lang =  getLangCode();
      //  String translatedText = null;
        if (lang.isEmpty()){
            Toast.makeText(getContext(),"Choose language.",Toast.LENGTH_LONG).show();
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
                else Toast.makeText(getContext(),"Cannot translate",Toast.LENGTH_LONG).show();
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
