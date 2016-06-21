package com.ariseden.post.activities.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariseden.post.activities.CommentsListActivity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;

import com.ariseden.post.R;
import com.ariseden.post.UtillsG.CallBackG;
import com.ariseden.post.UtillsG.GlobalConstantsG;
import com.ariseden.post.UtillsG.ShowIntrensicAd;
import com.ariseden.post.UtillsG.UtillG;
import com.ariseden.post.WebService.SuperAsyncG;
import com.ariseden.post.activities.BlockListActivity;
import com.ariseden.post.activities.ChatActivity;
import com.ariseden.post.activities.OpenImageActivity;
import com.ariseden.post.activities.ViewPostActivity;
import com.ariseden.post.adapter.HomeModel;
import com.ariseden.post.adapter.IdCardModel;
import com.ariseden.post.adapter.RecentChatModel;
import com.ariseden.post.widget.RoundedCornersGaganImg;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDetailsFragment extends BaseFragmentG
{


    public ImageDetailsFragment()
    {
        // Required empty public constructor
    }


    private ImageView imgShare, imgLike;
    private RoundedCornersGaganImg imgPost;
    private TextView               tvLocation, tvViewCount, tvLikeCount, tvCommentCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_details, container, false);

        findViewByID(v);
        showDataInt();


        return v;
    }


    @Override
    public void onResume()
    {
        fetchData();
        super.onResume();
    }

    String like_notlike = "yes";
    private final String LIKE      = "yes";
    private final String NOT_LIKED = "no";


    private void findViewByID(View view)
    {
        imgShare = (ImageView) view.findViewById(R.id.imgShare);
        imgLike = (ImageView) view.findViewById(R.id.imgLike);
        imgPost = (RoundedCornersGaganImg) view.findViewById(R.id.imgPost);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvViewCount = (TextView) view.findViewById(R.id.tvViewCount);
        tvLikeCount = (TextView) view.findViewById(R.id.tvLikeCount);
        tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
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
        tvViewCount.setText(imageInfo.getViewCount());

        tvLikeCount.setText(imageInfo.getLikeCount());
        tvCommentCount.setText(imageInfo.getCommentCount());

        if (!imageInfo.getStatusLine().equals("null") && !imageInfo.getStatusLine().isEmpty())
        {
            tvStatusLine.setText(imageInfo.getStatusLine());
        }

        imgProfile.setImageUrlRound(getActivity(), imageInfo.getPhotoPath());
        tvName.setText(imageInfo.getCustomerName());


        imgedit.setVisibility(getLocaldata().getUserid().equals(imageInfo.getHomeModel().getCustomerId()) ? View.GONE : View.VISIBLE);

        imgedit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (GlobalConstantsG.SHOW_Ad_MSG())
                {
                    ShowIntrensicAd.loadInterstitialAd(getActivity());
                }
                else
                {
                    if (BlockedByMe)
                    {
                        UtillG.show_dialog_msg(getActivity(), "You have blocked this user. Go to settings to unblock " + imageInfo.getCustomerName() + " ?", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                UtillG.global_dialog.dismiss();
                                startActivity(new Intent(getActivity(), BlockListActivity.class));
                            }
                        });
                    }
                    else
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
            }
        });


        tvCommentCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), CommentsListActivity.class);
                intent.putExtra("ThemePostId", homeModel.getThemePostId());
                intent.putExtra("ThemeId", homeModel.getThemeId());
                startActivity(intent);

            }
        });


        idCardModel = new IdCardModel(imageInfo.getPhotoPath(), imageInfo.getCustomerName(), imageInfo.getEmailId(), imageInfo.getStatusLine(), imageInfo.getHomeModel().getCustomerId());


//        like feature


        imgLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showProgress();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("CustomerId", getLocaldata().getUserid());
                hashMap.put("ThemeID", homeModel.getThemeId());
                hashMap.put("ThemePostId", homeModel.getThemePostId());
                hashMap.put("IsLike", like_notlike);
//                {"ThemePostId":11, "ThemeID" : 38, "CustomerId" : 123 , "IsLike" : "yes" or "no"}

                new SuperAsyncG(GlobalConstantsG.URL + "theme/SavePostLike", hashMap, new CallBackG<String>()
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
                                if (like_notlike.equals(LIKE))
                                {
//                                    UtillG.ShowToastImage(getActivity(), R.drawable.vector_heart);
                                    imgLike.setImageResource(R.mipmap.ic_like);

                                    like_notlike = NOT_LIKED;

                                    imageInfo.setLikeCount(String.format("%d", Integer.parseInt(imageInfo.getLikeCount()) + 1));
                                    tvLikeCount.setText(imageInfo.getLikeCount());
                                }
                                else
                                {
                                    imgLike.setImageResource(R.mipmap.ic_notlike);
                                    like_notlike = LIKE;

                                    imageInfo.setLikeCount(String.format("%d", Integer.parseInt(imageInfo.getLikeCount()) - 1));

                                    tvLikeCount.setText(imageInfo.getLikeCount());
                                }
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
        });


        tvLocation.setText(imageInfo.getTagLocation());
        tvLocation.setSelected(true);

//        end like feature


    }

    private ImageView imgedit, img_eye;

    private RoundedCornersGaganImg imgProfile;
    private TextView               tvName, tvEmail, tvStatusLine;

    private View viewIdCard;

    private IdCardModel idCardModel;

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


        img_eye.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (GlobalConstantsG.SHOW_Ad_EYE())
                {
                    ShowIntrensicAd.loadInterstitialAd(getActivity());
                }
                else
                {
                    if (idCardModel != null)
                    {
                        Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                        intent.putExtra("data", idCardModel);
                        startActivity(intent);

                    }
                }
            }
        });


        imgShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, homeModel.getImagePath());
                startActivity(Intent.createChooser(shareIntent, "Share.."));
            }
        });

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
                        ImageInfo imageInfo = new ImageInfo();

                        JSONObject jInner = new JSONObject(jboj.getString(GlobalConstantsG.Message));
                        imageInfo.setPhotoPath(jInner.getString("PhotoPath"));
                        imageInfo.setCreatedDate(jInner.getString("CreatedDate"));
                        imageInfo.setCustomerName(jInner.getString("CustomerName"));
                        imageInfo.setEmailId(jInner.getString("EmailId"));

                        imageInfo.setStatusLine(jInner.optString("StatusLine"));

                        imageInfo.setViewCount(jInner.getString("ViewCount"));

                        imageInfo.setLikeCount(jInner.getString("LikeCount"));

                        imageInfo.setCommentCount(jInner.optString("CommentCount"));


                        if (jInner.getString("LikedByMe").equals(LIKE))
                        {
                            like_notlike = NOT_LIKED;
                            imgLike.setImageResource(R.mipmap.ic_like);
                        }
                        else
                        {
                            like_notlike = LIKE;
                            imgLike.setImageResource(R.mipmap.ic_notlike);
                        }


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

                        BlockedByMe = jInner.optString("BlockedByMe").equals(LIKE);

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


    boolean BlockedByMe;
//    BlockedByMe
//            BlockedByThem

    public class ImageInfo
    {
        private String StatusLine;
        private String TagLocation;
        private String CreatedDate;
        private String ViewCount;
        private String CustomerName;
        private String PhotoPath;
        private String EmailId;
        private String LikeCount;

        public String getCommentCount()
        {
            return CommentCount;
        }

        public void setCommentCount(String commentCount)
        {
            CommentCount = commentCount;
        }

        private String CommentCount;

        public String getLikeCount()
        {
            return LikeCount;
        }

        public void setLikeCount(String likeCount)
        {
            LikeCount = likeCount;
        }

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
