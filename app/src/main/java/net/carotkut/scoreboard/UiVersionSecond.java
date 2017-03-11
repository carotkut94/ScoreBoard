package net.carotkut.scoreboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class UiVersionSecond extends AppCompatActivity {

    ImageView profileImage;
    TextView player_name, textCountry, text_runs, text_matches, text_desc, text_isFavourite;

    FloatingTextButton ftb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_version_second);
        profileImage = (ImageView) findViewById(R.id.player_image);
        textCountry = (TextView) findViewById(R.id.textCountry);
        text_runs = (TextView) findViewById(R.id.text_runs);
        text_matches = (TextView) findViewById(R.id.text_matches);
        text_desc = (TextView) findViewById(R.id.desc_details);
        player_name = (TextView)findViewById(R.id.player_name_details);
        text_isFavourite = (TextView) findViewById(R.id.isfav);
        ftb  = (FloatingTextButton) findViewById(R.id.share_button);
        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("image")).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade(300).into(profileImage);

        setTitle(intent.getStringExtra("name"));
        boolean isFavorite = intent.getBooleanExtra("isFavourite", false);
        textCountry.setText(intent.getStringExtra("country"));
        text_runs.setText(Integer.toString(intent.getIntExtra("runs",0)));
        text_matches.setText(Integer.toString(intent.getIntExtra("match", 1)));
        text_desc.setText(intent.getStringExtra("desc"));
        player_name.setText(intent.getStringExtra("name"));

        if(isFavorite)
        {
            text_isFavourite.setText("Favourite");
        }
        else
        {
            text_isFavourite.setText("Not Favourite");
        }


        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, player_name.getText().toString()+"\n"+text_runs.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }
}
