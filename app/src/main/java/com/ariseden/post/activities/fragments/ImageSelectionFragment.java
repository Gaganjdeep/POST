package com.ariseden.post.activities.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ariseden.post.UtillsG.BitmapDecoderG;
import com.ariseden.post.activities.ImagePostActivity;
import com.ariseden.post.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSelectionFragment extends Fragment implements View.OnClickListener
{


    public ImageSelectionFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);

        Button btnGallery = (Button) view.findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);

        Button btnCamera = (Button) view.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.btnGallery:

                BitmapDecoderG.openGallery(getActivity(), this);
                break;

            case R.id.btnCamera:
                BitmapDecoderG.openCamera(getActivity(), this);

                break;
        }

//        BitmapDecoderG.selectImage(getActivity(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {

            if (resultCode == Activity.RESULT_OK)
            {
                Uri uri = BitmapDecoderG.getFromData(requestCode, resultCode, data, getActivity().getContentResolver());

                Intent intnt = new Intent(getActivity(), ImagePostActivity.class);
                intnt.putExtra("image", uri);
                startActivity(intnt);
            }

        }
        catch (Exception E)
        {
            E.printStackTrace();
        }


    }







    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }




}