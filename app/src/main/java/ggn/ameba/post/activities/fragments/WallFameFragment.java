package ggn.ameba.post.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ggn.ameba.post.R;
import ggn.ameba.post.adapter.WallFameAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WallFameFragment extends Fragment
{


    public WallFameFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wall_fame, container, false);


        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);


        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerview.setAdapter(new WallFameAdapter(getActivity(), new ArrayList<String>()));


        return view;
    }

}
