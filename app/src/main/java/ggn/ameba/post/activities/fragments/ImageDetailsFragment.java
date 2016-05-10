package ggn.ameba.post.activities.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.CallBackG;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.UtillG;
import ggn.ameba.post.WebService.SuperAsyncG;
import ggn.ameba.post.activities.ChatActivity;
import ggn.ameba.post.activities.EditProfileActivity;
import ggn.ameba.post.activities.OpenImageActivity;
import ggn.ameba.post.adapter.HomeModel;
import ggn.ameba.post.adapter.RecentChatModel;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends BaseFragmentG
{


    public ImageDetailsFragment()
    {
        // Required empty public constructor
    }


    private ImageView              imgShare;
    private RoundedCornersGaganImg imgPost;
    private TextView               tvLocation, tvViewCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_details, container, false);

        findViewByID(v);
        showDataInt();

        fetchData();

        return v;
    }


    private void findViewByID(View view)
    {
        imgShare = (ImageView) view.findViewById(R.id.imgShare);
        imgPost = (RoundedCornersGaganImg) view.findViewById(R.id.imgPost);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvViewCount = (TextView) view.findViewById(R.id.tvViewCount);
        viewIdCard = view.findViewById(R.id.viewIdCard);

        showIDcard();
    }


    private void showDataInt()
    {
        if (homeModel != null)
        {
            imgPost.setImageUrl(getActivity(), homeModel.getImagePath());
            imgPost.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
//                    Intent intent = new Intent(getActivity(), OpenImageActivity.class);
//                    intent.putExtra("image", homeModel.getImagePath());
//                    startActivity(intent);

                    Intent intent = new Intent(getActivity(), OpenImageActivity.class);
                    intent.putExtra("image", homeModel.getImagePath());
                    UtillG.transitionToActivity(getActivity(), intent, imgProfile, "image");

                }
            });
        }
    }


    private void showCompleteData(final ImageInfo imageInfo)
    {
        tvLocation.setText(imageInfo.getTagLocation());

        tvViewCount.setText(imageInfo.getViewCount());

        if (!imageInfo.getStatusLine().equals("null") && !imageInfo.getStatusLine().isEmpty())
        {
            tvStatusLine.setText(imageInfo.getStatusLine());
        }

        imgProfile.setImageUrlRound(getActivity(), imageInfo.getPhotoPath());
        tvName.setText(imageInfo.getCustomerName());


        imgedit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!getLocaldata().getUserid().equals(imageInfo.getHomeModel().getCustomerId()))
                {
                    RecentChatModel recentChatModel = new RecentChatModel();
                    recentChatModel.setCustomerIdTo(imageInfo.getHomeModel().getCustomerId());
                    recentChatModel.setCustomerName(imageInfo.getCustomerName());
                    recentChatModel.setChatContent("");
                    recentChatModel.setDateTimeCreated("");
                    recentChatModel.setPhotoPath(imageInfo.getPhotoPath());
                    recentChatModel.setIsRead("");


                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("data", recentChatModel);
                    getActivity().startActivity(intent);
                }
            }
        });


    }

    ImageView imgedit, img_eye;

    RoundedCornersGaganImg imgProfile;
    TextView               tvName, tvEmail, tvStatusLine;

    View viewIdCard;


    private void showIDcard()
    {
        imgProfile = (RoundedCornersGaganImg) viewIdCard.findViewById(R.id.imgProfile);


        tvName = (TextView) viewIdCard.findViewById(R.id.tvName);


        tvEmail = (TextView) viewIdCard.findViewById(R.id.tvEmail);
        tvStatusLine = (TextView) viewIdCard.findViewById(R.id.tvStatusLine);
        tvStatusLine.setVisibility(View.VISIBLE);

        img_eye = (ImageView) viewIdCard.findViewById(R.id.img_eye);
        imgedit = (ImageView) viewIdCard.findViewById(R.id.imgedit);


        TextView tv = (TextView) viewIdCard.findViewById(R.id.tvPostId);
        tv.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Gnawhard.otf"));
    }


    HomeModel homeModel;

    public void setData(HomeModel homeModel)
    {
        this.homeModel = homeModel;
    }


    private void fetchData()
    {
//        ThemePostId=11&CustomerId=123

        showProgress();

        new SuperAsyncG(GlobalConstantsG.URL + "theme/GetThemeCustomerPost?ThemePostId=" + homeModel.getThemePostId() + "&CustomerId=" + getLocaldata().getUserid(), new HashMap<String, String>(), new CallBackG<String>()
        {
            @Override
            public void callBack(String response)
            {
                try
                {
                    cancelProgress();

                    JSONObject jboj = new JSONObject(response);

                    if (jboj.getString(GlobalConstantsG.Status).equals(GlobalConstantsG.success))
                    {
//                        "ThemePostId": ​9,
//                            "ThemeID": ​38,
//                            "CustomerId": ​584,
//                            "ImagePath": "http://112.196.34.42:8089/ThemePhoto/75f49665-c275-4969-aa42-473eedae81b0.png",
//                            "HashTag": null,
//                            "TagLocation": null,
//                            "Latitude": null,
//                            "Longitude": null,
//                            "CreatedDate": "2016-04-28T15:43:36.717",
//                            "ViewCount": ​0,
//                            "CustomerName": "yo",
//                            "PhotoPath": "http://112.196.34.42:8089/CustomerPhoto/70fec7eb-9e0d-460f-9261-d28a847cac79.png",
//                            "EmailId": "yo@yo.com"

                        ImageInfo imageInfo = new ImageInfo();

                        JSONObject jInner = new JSONObject(jboj.getString(GlobalConstantsG.Message));
                        imageInfo.setPhotoPath(jInner.getString("PhotoPath"));
                        imageInfo.setCreatedDate(jInner.getString("CreatedDate"));
                        imageInfo.setCustomerName(jInner.getString("CustomerName"));
                        imageInfo.setEmailId(jInner.getString("EmailId"));

                        imageInfo.setStatusLine(jInner.optString("StatusLine"));

                        imageInfo.setViewCount(jInner.getString("ViewCount"));

                        imageInfo.setHomeModel(homeModel);


                        try
                        {
                            if (!jInner.optString("Latitude").equals("null"))
                            {
                                imageInfo.setLatLng(jInner.getString("Latitude"), jInner.getString("Longitude"));
                                imageInfo.setTagLocation(jInner.optString("TagLocation"));
                            }
                            else
                            {
                                imageInfo.setTagLocation("location");
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        imgPost.setImageUrl(getActivity(), jInner.getString("ImagePath"));
                        showCompleteData(imageInfo);


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


    public class ImageInfo
    {
        private String StatusLine, TagLocation, CreatedDate, ViewCount, CustomerName, PhotoPath, EmailId;

        private HomeModel homeModel;

        public String getStatusLine()
        {
            return StatusLine;
        }

        public void setStatusLine(String statusLine)
        {
            StatusLine = statusLine;
        }

        private double lat, lng;


        public LatLng getLat()
        {
            return new LatLng(lat, lng);
        }

        public void setLatLng(String lat, String lng)
        {
            this.lat = Double.parseDouble(lat);
            this.lng = Double.parseDouble(lng);
        }


        public HomeModel getHomeModel()
        {
            return homeModel;
        }

        public void setHomeModel(HomeModel homeModel)
        {
            this.homeModel = homeModel;
        }


        public String getTagLocation()
        {
            return TagLocation;
        }

        public void setTagLocation(String tagLocation)
        {
            TagLocation = tagLocation;
        }

        public String getCreatedDate()
        {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate)
        {
            CreatedDate = createdDate;
        }

        public String getViewCount()
        {
            return ViewCount;
        }

        public void setViewCount(String viewCount)
        {
            ViewCount = viewCount;
        }

        public String getCustomerName()
        {
            return CustomerName;
        }

        public void setCustomerName(String customerName)
        {
            CustomerName = customerName;
        }

        public String getPhotoPath()
        {
            return PhotoPath;
        }

        public void setPhotoPath(String photoPath)
        {
            PhotoPath = photoPath;
        }

        public String getEmailId()
        {
            return EmailId;
        }

        public void setEmailId(String emailId)
        {
            EmailId = emailId;
        }
    }


}
