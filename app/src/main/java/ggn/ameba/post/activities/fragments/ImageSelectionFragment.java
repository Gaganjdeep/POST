package ggn.ameba.post.activities.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ggn.ameba.post.activities.ImagePostActivity;
import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.BitmapDecoderG;

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
}