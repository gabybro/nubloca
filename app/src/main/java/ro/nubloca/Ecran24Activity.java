package ro.nubloca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import ro.nubloca.Networking.CustomAdapter;
import ro.nubloca.Networking.RequestTara;
import ro.nubloca.Networking.StandElem;
import ro.nubloca.extras.CustomFontTitilliumRegular;
import ro.nubloca.extras.FontTitilliumBold;
import ro.nubloca.extras.Global;

public class Ecran24Activity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    String[] listNumere;
    int[] click;
    ListAdapter customAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ecran24);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#fcd116"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);



        int size_LSNumere = sharedpreferences.getInt("SizeLSNumere", 0);
        if (size_LSNumere > 0) {
            listNumere = new String[size_LSNumere];
            for (int i = 0; i < size_LSNumere; i++) {
                listNumere[i] = sharedpreferences.getString("LSNumere" + i, "");
            }
            /*CustomFontTitilliumRegular text = (CustomFontTitilliumRegular) findViewById(R.id.text1);
            String textShared = listNumere[0];
            text.setText(textShared);*/

            customAdapter = new CustomAdapterNumere(this, listNumere);
            ListView lv = (ListView) findViewById(R.id.list);
            lv.setAdapter(customAdapter);

        }



    }

    public class CustomAdapterNumere extends ArrayAdapter<String> {



        public CustomAdapterNumere(Context context, String[] elemente) {
            super(context, R.layout.raw_list1, elemente);
            click = new int[elemente.length];
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {


            LayoutInflater dapter = LayoutInflater.from(getContext());
            final View customView = dapter.inflate(R.layout.raw_list24, parent, false);

            String singleElem = getItem(position);
            CustomFontTitilliumRegular textul = (CustomFontTitilliumRegular) customView.findViewById(R.id.text);
            final ImageView imaginea = (ImageView) customView.findViewById(R.id.radioButton);

            textul.setText(singleElem);

            LinearLayout layout = (LinearLayout) customView.findViewById(R.id.linear);

            imaginea.setImageResource(R.drawable.radio);

            if (layout != null) {
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (click[position]==0) {
                            imaginea.setImageResource(R.drawable.radio_press);
                            click[position] = 1;

                        }else {
                            imaginea.setImageResource(R.drawable.radio);
                            click[position] = 0;
                        }
                    }
                });
            }


            return customView;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        View button = (View) this.findViewById(R.id.sterge);
        int size_LSNumere = sharedpreferences.getInt("SizeLSNumere", 0);
        if (size_LSNumere > 0) {
            if (button != null)
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int sum = 0;

                        for (int i = 0; i < click.length; i++) {
                            sum += click[i];
                        }

                        String[] trans = new String[listNumere.length - sum];
                        int j = 0;
                        for (int i = 0; i < listNumere.length; i++) {
                            if (click[i] == 0) {
                                trans[j] = listNumere[i];
                                j++;
                            }
                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        for (int i = 0; i < listNumere.length; i++) {
                            editor.remove("LSNumere" + i);
                        }
                        editor.putInt("SizeLSNumere", trans.length);
                        editor.commit();

                        listNumere = new String[trans.length];
                        listNumere = trans;

                        for (int i = 0; i < listNumere.length; i++) {
                            editor.putString("LSNumere" + i, listNumere[i]);
                        }
                        editor.commit();

                        customAdapter = new CustomAdapterNumere(getApplicationContext(), listNumere);
                        ListView lv = (ListView) findViewById(R.id.list);
                        lv.setAdapter(customAdapter);

                    /*Toast toast = Toast.makeText(Ecran24Activity.this, "", Toast.LENGTH_LONG);
                    toast.show();*/

                    }
                });
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Ecran24Activity.this, Ecran20Activity.class));
        finish();
        super.onBackPressed();
    }

}