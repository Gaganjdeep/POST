package ggn.ameba.post.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.DateUtilsG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.adapter.WallFameAdapter;
import ggn.ameba.post.adapter.WallOfFameModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WallFameFragment extends BaseFragmentG
{


    public WallFameFragment()
    {
        // Required empty public constructor
    }


    private List<WallOfFameModel> listData;
    private WallFameAdapter       wallFameAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wall_fame, container, false);

        listData = new ArrayList<>();

        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        wallFameAdapter = new WallFameAdapter(getActivity(), listData);


        recyclerview.setAdapter(wallFameAdapter);


        showData();


        return view;
    }

    SimpleDateFormat sdf = new SimpleDateFormat(DateUtilsG.G_FORMAT_SHORT, Locale.US);

    private void showData()
    {

//        showProgress();

        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetWallFame", new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
//                    cancelProgress();


                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
                        JSONArray jsonArray = new JSONArray(jboj.getString(GlobalConstantsG.Message));

                        listData.clear();

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            WallOfFameModel homemodel = new WallOfFameModel();

                            JSONObject jInner = jsonArray.getJSONObject(i);

                            homemodel.setWallFamePhoto(jInner.optString("WallFamePhoto"));


                            String date = jInner.getString("ThemeEndDate");
                            if (date.contains("."))
                            {
                                date = date.substring(0, date.indexOf("."));
                            }

                            String created_at = sdf.format(DateUtilsG.dateServer(date));


                            homemodel.setThemeEndDate(created_at);


                            homemodel.setThemeStartDate(jInner.getString("ThemeStartDate"));


                            homemodel.setThemeID(jInner.getString("ThemeID"));
                            homemodel.setThemeName(jInner.getString("ThemeName"));


//                            "ThemeID": ​38,
//                                "ThemeName": "Food",
//                                "ThemeStartDate": "2016-04-27T00:00:00",
//                                "ThemeEndDate": "2016-05-16T00:00:00",
//                                "CreatedDate": "2016-04-27T00:00:00",
//                                "WallFamePhoto": "http://112.196.34.42:8089/ThemePhoto/a644a878-b9b7-4aa8-9fa2-99330dfe9fc7.png",
//                                "ViewCount": ​11,
//                                "LikeCount": ​0,
//                                "CommentCount": ​0


                            listData.add(homemodel);
                        }

                        wallFameAdapter.notifyDataSetChanged();

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


}
