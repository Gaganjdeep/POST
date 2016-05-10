package ggn.ameba.post.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ggn.ameba.post.R;
import ggn.ameba.post.adapter.HomeAdapter;
import ggn.ameba.post.adapter.HomeModel;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

public class ViewPostActivity extends BaseActivityG
{
    private RecyclerView recyclerview;
    private ImageView    imgedit, img_eye;
    private
    RoundedCornersGaganImg imgProfile;
    private TextView tvName, tvEmail, tvStatusLine;

    View viewIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, 1));

        recyclerview.setAdapter(new HomeAdapter(ViewPostActivity.this, new ArrayList<HomeModel>()));

        showIDcard();
    }


    private void showIDcard()
    {

        viewIdCard = findViewById(R.id.viewIdCard);

        imgProfile = (RoundedCornersGaganImg) viewIdCard.findViewById(R.id.imgProfile);
        imgProfile.setImageUrlRound(ViewPostActivity.this, getLocaldata().getPhotoUrl());

        tvName = (TextView) viewIdCard.findViewById(R.id.tvName);
        tvName.setText(getLocaldata().getName());


        tvStatusLine = (TextView) viewIdCard.findViewById(R.id.tvStatusLine);
        tvStatusLine.setVisibility(View.VISIBLE);
        tvStatusLine.setText(getLocaldata().getTagLine());
        TextView tv = (TextView) viewIdCard.findViewById(R.id.tvPostId);
        tv.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Gnawhard.otf"));

        tvEmail = (TextView) viewIdCard.findViewById(R.id.tvEmail);
        tvEmail.setText(getLocaldata().getEmail());


    }

    public void baCk(View view)
    {
        finish();
    }
}
