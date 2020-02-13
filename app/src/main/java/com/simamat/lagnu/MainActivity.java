package com.simamat.lagnu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.simamat.lagnu.database.DatabaseAccess;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvCatalog;
    private CardView cvMain;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("LagNU");
        collapsingToolbarLayout.setExpandedTitleColor(
                ContextCompat.getColor(this, R.color.colorTansparant));

        lvCatalog = (ListView) findViewById(R.id.rv_judul_catalog);
        cvMain = (CardView) findViewById(R.id.card_view_main);


        lvCatalog.setDivider(null);
        lvCatalog.setNestedScrollingEnabled(true);
        cvMain.setBackgroundResource(R.drawable.bg_list);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> allJudul = databaseAccess.getAllJudul();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_judul_lagu,
                R.id.tv_list_judul, allJudul);
        lvCatalog.setAdapter(adapter);

        lvCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent moveLirik = new Intent(MainActivity.this, LirikActivity.class);
                String pos = (lvCatalog.getItemAtPosition(i).toString());
                moveLirik.putExtra("judul", pos);
                startActivity(moveLirik);
            }
        });

    }
}