package com.domnich.vlad.translator;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private EditText edTextEng;
    private TextView textRus;
    private Button buttonTranslate;

    private static final String KEY = "trnsl.1.1.20170210T124934Z.d35ea31d54902cf9.1111b58b85d4b141359d3f5d458c065e2c398fcf";
    private static final String URL = "https://translate.yandex.net";

    //создаем объект Gson
    private Gson gson = new GsonBuilder().create();

    //создаем объект класса Retrofit
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    //создаем объект интерфейса
    private Link link = retrofit.create(Link.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        edTextEng = (EditText) findViewById(R.id.edTextEng);
        textRus = (TextView) findViewById(R.id.textRus);
        buttonTranslate = (Button) findViewById(R.id.buttonTranslate);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> mapJson = new HashMap<>();
                mapJson.put("key", KEY);
                mapJson.put("text", edTextEng.getText().toString());
                mapJson.put("lang", "en-ru");

                //создаем объект Call
                Call<Object> call = link.translate(mapJson);

                try {
                    Response<Object> response = call.execute();

                    Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);

                    for(Map.Entry e : map.entrySet()){

                        if(e.getKey().equals("text")){
                            textRus.setText(e.getValue().toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
