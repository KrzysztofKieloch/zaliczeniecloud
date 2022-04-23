package com.example.zaliczenie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    TextView thetext;
    EditText model,marka,purl;
    Button btnAdd,btnBack,btnB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_add);

        model = (EditText)findViewById(R.id.txtModel);
        marka = (EditText)findViewById(R.id.txtMarka);
        purl = (EditText)findViewById(R.id.txtUrl);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnBack = (Button)findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(view -> {
            insertData();
            clearAll();
        });

        btnBack.setOnClickListener(view -> finish());


        thetext = findViewById(R.id.t1);
        btnB = findViewById(R.id.btnB);

        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new doit().execute();

            }
        });
    }

    private void insertData(){
        Map<String,Object> map = new HashMap<>();
        map.put("model",model.getText().toString());
        map.put("marka",marka.getText().toString());
        map.put("purl",purl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Urządzenia").push()
                .setValue(map)
                .addOnSuccessListener(unused -> Toast.makeText(AddActivity.this, "Zauktaulizowano pomyślnie.",Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddActivity.this, "Odrzucono.",Toast.LENGTH_SHORT).show());
    }
    private void clearAll(){
        model.setText("");
        marka.setText("");
        purl.setText("");
    }

    public class doit extends AsyncTask<Void,Void,Void>{
String nazwa;

        @Override
        protected Void doInBackground(Void... voids) {
org.jsoup.nodes.Document doc = null;
            try {
                doc= Jsoup.connect("https://www.mgsm.pl/pl/katalog/samsung/galaxym535g/").get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            org.jsoup.select.Elements elements = doc.getElementsByClass("breadcrumbs__last");
            nazwa = elements.text();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            thetext.setText(nazwa);
        }
    }
}
