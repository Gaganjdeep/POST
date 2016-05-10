package ggn.ameba.post.activities.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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


    private SwipeRefreshLayout refreshLayout;

    private ImageView imgSettings, imgMsg;
    private SearchView searchView;
    private TextView   tvPOST;
    RecyclerView recyclerview;

    private ProgressBar loading;


    List<HomeModel> listHome;


    int PAGE_NUMBER = 0;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        listHome = new ArrayList<>();
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
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


        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                if (listHome.size() > 14)
                {
                    showData(PAGE_NUMBER);
                }

            }
        };
//        showData(0);
        showData(0);
        recyclerview.setOnScrollListener(endlessRecyclerOnScrollListener);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                endlessRecyclerOnScrollListener.startOverStaggered();
                PAGE_NUMBER = 0;
                showData(0);
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


    private void showData(final int pageNUmber)
    {
        if (pageNUmber == 0)
        {
            refreshLayout.setRefreshing(true);
        }
        else
        {
            isLoading(true);
        }

        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetThemePosts?ThemeID=" + getLocaldata().getThemeID() + "&PageNumber=" + pageNUmber, new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    if (pageNUmber == 0)
                    {
                        if (refreshLayout.isRefreshing())
                        {
                            refreshLayout.setRefreshing(false);
                        }
                    }
                    else
                    {
                        isLoading(false);
                    }


                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONArray jsonArray = new JSONArray(jboj.getString(GlobalConstantsG.Message));

                        if (pageNUmber == 0)
                        {
                            listHome.clear();
                        }

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
                        PAGE_NUMBER++;
                        homeAdapter.notifyDataSetChanged();

                    }
                    else
                    {
//                        UtillG.showToast(jboj.getString(GlobalConstantsG.Message), getActivity(), true);
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


    boolean broadcastisnull = false;

    @Override
    public void onResume()
    {
        super.onResume();

        if (!broadcastisnull)
        {
            if (receiver == null)
            {
                receiver = new Chat_BroadcastReceiver();
            }

            getActivity().registerReceiver(receiver, new IntentFilter(GlobalConstantsG.BROADCAST_UPDATE_HOME));
            broadcastisnull = true;
        }

    }


    @Override
    public void onDestroy()
    {
        try
        {
            if (broadcastisnull)
            {
                getActivity().unregisterReceiver(receiver);
                broadcastisnull = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    Chat_BroadcastReceiver receiver;

    private class Chat_BroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            endlessRecyclerOnScrollListener.startOverStaggered();
            PAGE_NUMBER = 0;
            showData(0);
        }
    }


}
