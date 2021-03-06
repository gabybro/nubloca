package ro.nubloca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ro.nubloca.extras.ObservableScrollView;
import ro.nubloca.extras.ScrollViewListener;

public class Ecran3Activity extends AppCompatActivity implements ScrollViewListener {

    private ObservableScrollView scrollView = null;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public boolean tos_check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_ecran3);

        scrollView = (ObservableScrollView) findViewById(R.id.scrollView1);
        scrollView.setScrollViewListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        View b1 = (View) findViewById(R.id.acord);
        if (b1 != null) {
            b1.setOnClickListener(new View.OnClickListener() {
                //ecran 4, buton "De acord"
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    //verifica daca TOS-ul a fost citit
                    if (tos_check) {
                        Toast.makeText(Ecran3Activity.this, "Thanks", Toast.LENGTH_LONG).show();
                        //TOS-ul a fost acceptat
                        editor.putBoolean("TOS", true);
                        editor.apply();
                        finish();
                        //merge la ecranul 7
                        startActivity(new Intent(Ecran3Activity.this, Ecran7Activity.class));
                    } else {
                        //TOS-ul nu a fost citit
                        final RelativeLayout back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
                        //background-ul negru
                        //******back_dim_layout.setVisibility(View.VISIBLE);
                        //******LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        //popup-ul de interogare ecran 4.2
                        //******View popupView = layoutInflater.inflate(R.layout.popup, null);
                        //******final PopupWindow popupWindow = new PopupWindow(
                        //******      popupView,
                        //******    Toolbar.LayoutParams.WRAP_CONTENT,
                        //******  Toolbar.LayoutParams.WRAP_CONTENT, true);
                        openAlert(v);

                    }

                }
            });
        }

        View b2 = (View) findViewById(R.id.refuz);
        if (b2 != null) {
            b2.setOnClickListener(new View.OnClickListener() {
                //a fost apasat butonul "Refuz" din ecranul 4.1 si se trece la ecranul 5
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("TOS", false);
                    editor.apply();
                    finish();
                    startActivity(new Intent(Ecran3Activity.this, Ecran4Activity.class));
                }
            });
        }
    }

    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //verifica citirea TOS-ului
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        if (diff == 0) {
            tos_check = true;
            //Toast.makeText(Ecran3Activity.this, "Jos", Toast.LENGTH_LONG).show();

        }


    }

    private void openAlert(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Ecran3Activity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup, null);
        View btnDa = (View) dialogView.findViewById(R.id.button2);
        View btnDismiss = (View) dialogView.findViewById(R.id.button1);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // set positive button: Yes message
        btnDa.setOnClickListener(new Button.OnClickListener() {
            //a fost apasat butonul "Da"
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("TOS", true);
                editor.apply();
                //se trece la ecranul 7
                finish();
                startActivity(new Intent(Ecran3Activity.this, Ecran7Activity.class));

            }
        });
        // set negative button: No message

        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            //a fost apasat butonul "Da"
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // show alert
        alertDialog.show();
    }
}