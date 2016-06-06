package ro.nubloca;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import ro.nubloca.Networking.AllElem;
import ro.nubloca.Networking.GetRequest;
import ro.nubloca.Networking.Response;
import ro.nubloca.extras.CustomFontTitilliumBold;
import ro.nubloca.extras.CustomFontTitilliumRegular;
import ro.nubloca.extras.GlobalVar;

public class Ecran26Activity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    String url = "http://api.nubloca.ro/tipuri_inmatriculare_tipuri_elemente/";
    String url2 = "http://api.nubloca.ro/tipuri_elemente/";

    String result_string, nume_tip_inmatriculare;
    int campuri = 3;
    private ProgressDialog pd;
    int[] ret, id_tip_element;
    AllElem[] allelem;
    List<Response> response2;
    int [] Ids_tipuri_inmatriculare_tipuri_elemente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ecran26);

        nume_tip_inmatriculare = ((GlobalVar) this.getApplication()).getName_tip_inmatriculare();
        Ids_tipuri_inmatriculare_tipuri_elemente = ((GlobalVar) this.getApplication()).getIds_tipuri_inmatriculare_tipuri_elemente();
        ret = Ids_tipuri_inmatriculare_tipuri_elemente;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#fcd116"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        makePostRequestOnNewThread();
    }

    private void makePostRequestOnNewThread() {

        //pd = ProgressDialog.show(this, "", "", true,  false);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    makePostRequest();
                    makePostRequest2();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);

            }
        });
        t.start();
    }

    private void makePostRequest() throws JSONException {
        /*{
            "identificare": {
                "user": {"app_code": "abcdefghijkl123456"},
                "resursa": {"id": [1,2,3]}
        },
            "cerute": ["id","id_tip_element","ordinea","valoare_demo_imagine"]            ]
        }*/

        GetRequest elemm = new GetRequest();
        JSONObject resursa = new JSONObject();
        JSONArray cerute = new JSONArray().put("id").put("id_tip_element").put("valoare_demo_imagine").put("ordinea");
        JSONArray arr = new JSONArray();
        for (int ff = 0; ff < ret.length; ff++) {
            arr.put(Ids_tipuri_inmatriculare_tipuri_elemente[ff]);
        }
        resursa.put("id", arr);

        result_string = elemm.getRaspuns(Ecran26Activity.this, url, resursa, cerute);
        Gson gson = new Gson();
        Type listeType = new TypeToken<List<Response>>() {
        }.getType();
        List<Response> response = (List<Response>) gson.fromJson(result_string, listeType);
        /*[{
                "id": 1,
                "id_tip_element": 1,
                "ordinea": 1,
                "valoare_demo_imagine": "B"
        }]*/
        allelem = new AllElem[response.size()];
        for (int i = 0; i < response.size(); i++) {
            allelem[i] = new AllElem();
            allelem[i].setId(response.get(i).getId());
            allelem[i].setId_tip_element(response.get(i).getId_tip_element());
            allelem[i].setOrdinea(response.get(i).getOrdinea());
            allelem[i].setValoare_demo_imagine(response.get(i).getValoare_demo_imagine());
        }
        id_tip_element = new int[response.size()];
        for (int i = 0; i < response.size(); i++) {
            int j = response.get(i).getId_tip_element();
            id_tip_element[i] = j;
        }
    }

    private void makePostRequest2() throws JSONException {
       /* {
            "identificare": {
                "user": {"app_code": "abcdefghijkl123456"},
                "resursa":{"id":[5,6,6]}},
            "cerute":[
                "id",
                "tip",
                "editabil_user",
                "maxlength",
                "valori"]
        }*/

        GetRequest elemm = new GetRequest();

        JSONArray cerute = new JSONArray().put("id").put("tip").put("editabil_user").put("maxlength").put("valori");
        JSONObject resursa = new JSONObject();
        String result_string = null;
        Gson gson = new Gson();
        Type listeType = new TypeToken<List<Response>>() {
        }.getType();
        JSONArray id = new JSONArray();
        for (int i = 0; i < id_tip_element.length; i++) {
            id.put(id_tip_element[i]);
        }
        resursa.put("id", id);
        result_string = elemm.getRaspuns(Ecran26Activity.this, url2, resursa, cerute);
        response2 = (List<Response>) gson.fromJson(result_string, listeType);

        for (int i = 0; i < allelem.length; i++) {
            for (int j = 0; j < response2.size(); j++) {
                if (allelem[i].getId_tip_element() == response2.get(j).getId()) {
                    allelem[i].setEditabil_user(response2.get(j).getEditabil_user());
                    allelem[i].setMaxlength(response2.get(j).getMaxlength());
                    allelem[i].setTip(response2.get(j).getTip());
                }
            }
        }

        Arrays.sort(allelem);

        /*[{
            "id": 5,
            "tip": "LISTA", // "tip": "CIFRE", // "tip": "LITERE"
            "editabil_user": 1,
            "maxlength": 2,
            "valori":[{"id": 1,"cod": "CD"},{"id": 2,"cod": "CO"},{"id": 3,"cod": "TC"}]}
           }]*/

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //pd.dismiss();
            Gson gson = new Gson();
            Type listeType = new TypeToken<List<Response>>() {
            }.getType();
            List<Response> response = (List<Response>) gson.fromJson(result_string, listeType);
            campuri = response.size();
            ///////////////
            showElements();
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Ecran26Activity.this, Ecran23Activity.class));
        finish();
        super.onBackPressed();
    }

    private void showElements() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int divLength = size.x;
        int height = size.y;

        int nrUML = 0;
        int nrSPI = campuri - 1;
        int nrSPL = 2;

        int valUML = 0;
        int valSPL = 0;
        int valSPI = 0;

        int valMaxUML = divLength / 10;

        for (int i = 0; i < campuri; i++) {
            // calculam lunngimea inputului
            if (allelem[i].getMaxlength() < 3) {
                int rr = allelem.length;
                int oo = allelem[i].getMaxlength();
                nrUML += 3;
            } else {
                int xx = allelem[i].getMaxlength();
                nrUML += allelem[i].getMaxlength();
            }

        }
        valSPI = divLength / (nrUML * 6 + 3 * nrSPL + nrSPI);
        valSPL = 3 * valSPI;
        valUML = 6 * valSPI;
        int valRealUml = Math.min(valMaxUML, valUML);
        int minTrei = 0;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearPlate1);
        linearLayout.setPadding(valSPL, 0, valSPL, 0);

        for (int i = 0; i < campuri; i++) {
            if (allelem[i].getMaxlength() < 3) {
                minTrei = 3;
            } else {
                minTrei = allelem[i].getMaxlength();
            }


            CustomFontTitilliumBold field = new CustomFontTitilliumBold(this);
            field.setId(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);

            if (i != 0) {
                params.setMargins(valSPI, 0, 0, 0);
            }

            field.setLayoutParams(params);
            field.setWidth(valRealUml * minTrei);
            field.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
            field.setTextSize(30);
            field.setBackgroundResource(R.drawable.plate_border);
            field.setText(allelem[i].getValoare_demo_imagine());
            if (allelem[i].getEditabil_user() == 0) {
                field.setBackgroundResource(R.drawable.plate_border_white);
            }
            linearLayout.addView(field);

            CustomFontTitilliumBold tip_inmatriculare_nume = (CustomFontTitilliumBold) findViewById(R.id.nume_tip_inmatriculare);
            tip_inmatriculare_nume.setVisibility(View.VISIBLE);
            tip_inmatriculare_nume.setText(nume_tip_inmatriculare);
            CustomFontTitilliumRegular text1 = (CustomFontTitilliumRegular) findViewById(R.id.textView20);
            text1.setVisibility(View.VISIBLE);
            ImageView image1 = (ImageView) findViewById(R.id.imageView9);
            image1.setVisibility(View.VISIBLE);
        }

    }

}
