package com.simamat.lagnu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.simamat.lagnu.database.DatabaseAccess;
import static android.widget.Toast.LENGTH_LONG;

public class LirikActivity extends AppCompatActivity {

    private TextView tvPencipta, tvLirik;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lirik);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        Intent getIntent = getIntent();
        final String judul = getIntent.getStringExtra("judul");

        final String judulLagu = databaseAccess.getJudul(judul);
        String penciptaLagu = databaseAccess.getPencipta(judul);
        String lirikLagu = databaseAccess.getLirik(judul);
        final String urlLagu = databaseAccess.getUrlVideo(judul);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        tvPencipta = (TextView) findViewById(R.id.tv_pencipta_lagu);
        tvLirik = (TextView) findViewById(R.id.tv_lirik_lagu);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        toolbar.setTitle(judulLagu);
        tvPencipta.setText(penciptaLagu);
        tvLirik.setText(lirikLagu);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(judulLagu);
        collapsingToolbarLayout.setExpandedTitleColor(
                ContextCompat.getColor(this, R.color.colorPrimary));

        if (adaInternet()) {
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(urlLagu, 0);
                }
            });
        } else {
            Toast.makeText(LirikActivity.this, "Tidak Ada Koneksi Internet!", LENGTH_LONG).show();
        }

        if(tvPencipta.length() ==  0 || tvPencipta.equals("")) {
            tvPencipta.setVisibility(View.VISIBLE);
        } else {
            tvPencipta.setVisibility(View.GONE);
        }

        databaseAccess.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            youTubePlayerView.release();
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        youTubePlayerView.release();
        finish();
    }

    private boolean adaInternet(){
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
}
