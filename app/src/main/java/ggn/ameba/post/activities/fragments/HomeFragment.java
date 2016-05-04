package ggn.ameba.post.activities.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.SharedPrefHelper;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.activities.HomeTabActivity;
import ggn.ameba.post.activities.RecentChatListActivity;
import ggn.ameba.post.activities.SettingsActivity;
import ggn.ameba.post.activities.ThemeInfoActivtiy;
import ggn.ameba.post.adapter.HomeAdapter;
import ggn.ameba.post.adapter.HomeModel;
import ggn.ameba.post.adapter.RecentChatAdapter;
import ggn.ameba.post.widget.EndlessRecyclerOnScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragmentG implements View.OnClickListener, SearchView.OnQueryTextListener
{


    public HomeFragment()
    {
        // Required empty public constructor
    }


    private ImageView imgSettings, imgMsg;
    private SearchView searchView;
    private TextView   tvPOST;
    RecyclerView recyclerview;

    private ProgressBar loading;


    List<HomeModel> listHome;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        listHome = new ArrayList<>();

        tvPOST = (TextView) view.findViewById(R.id.tvPOST);
        tvPOST.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gnawhard.otf"));

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        imgSettings = (ImageView) view.findViewById(R.id.imgSettings);
        imgMsg = (ImageView) view.findViewById(R.id.imgMsg);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        imgMsg.setOnClickListener(this);
        imgSettings.setOnClickListener(this);


        mLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerview.setLayoutManager(mLayoutManager);


        homeAdapter = new HomeAdapter(getActivity(), listHome);
        recyclerview.setAdapter(homeAdapter);

//        showData(0);
        showData(0);
        recyclerview.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                // do something...

                isLoading(true);

                homeAdapter.count = 10 * current_page;
                homeAdapter.notifyDataSetChanged();
                System.out.println(current_page + "GGGGGGGGGGGG");

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        isLoading(false);
                    }
                }, 2000);


            }
        });


        return view;
    }


    private void isLoading(boolean isloading)
    {
        loading.setVisibility(isloading ? View.VISIBLE : View.GONE);
    }


    HomeAdapter                homeAdapter;
    StaggeredGridLayoutManager mLayoutManager;


    private void showData(int pageNUmber)
    {
//        URL : http://112.196.34.42:8089/theme/GetThemePosts?ThemeID=38&PageNumber=0
//        Method : GET
//
//                Response
//        [{"ThemePostId":11, "ThemeID" : 38, "CustomerId" : 123, "ImagePath" : "path","HashTag" : "Pizza,Beer", "TagLocation" : "Chandigarh", "Latitude" : 32.00,
//
//            "Longitude" : 70.00, "CreatedDate": "2016-04-30T00:00:00" },{"ThemePostId":12, "ThemeID" : 38, "CustomerId" : 143, "ImagePath" : "path","HashTag" :
//
//        "Pizza,Beer", "TagLocation" : "Chandigarh", "Latitude" : 32.00, "Longitude" : 70.00, "CreatedDate": "2016-04-30T00:00:00" }]

        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetThemePosts?ThemeID=" + getLocaldata().getThemeID() + "&PageNumber=" + pageNUmber, new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONArray jsonArray = new JSONArray(jboj.getString(GlobalConstantsG.Message));


                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            HomeModel homemodel = new HomeModel();

                            JSONObject jInner = jsonArray.getJSONObject(i);
                            homemodel.setImagePath(jInner.getString("ImagePath"));
                            homemodel.setCreatedDate(jInner.getString("CreatedDate"));
                            homemodel.setCustomerId(jInner.getString("CustomerId"));
                            homemodel.setThemePostId(jInner.getString("ThemePostId"));

                            listHome.add(homemodel);
                        }

                        homeAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), getActivity(), true);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();
    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.imgMsg:

                startActivity(new Intent(getActivity(), RecentChatListActivity.class));
                break;
            case R.id.imgSettings:


                startActivity(new Intent(getActivity(), SettingsActivity.class));

                break;


        }


    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return false;
    }
}
