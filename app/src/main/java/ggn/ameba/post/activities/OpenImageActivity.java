package ggn.ameba.post.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ggn.ameba.post.R;

public class OpenImageActivity extends Activity
{


    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);


        String url = getIntent().getStringExtra("image");


        imgView = (ImageView) findViewById(R.id.imgView);

        Picasso.with(OpenImageActivity.this).load(url).priority(Picasso.Priority.HIGH).centerInside().resize(640, 400).into(imgView);


    }
}
