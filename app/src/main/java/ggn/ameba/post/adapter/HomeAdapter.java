package ggn.ameba.post.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.util.AdInternalSettings;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.r;

import java.util.List;
import java.util.UUID;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.GlobalConstantsG;
import ggn.ameba.post.UtillsG.ShowIntrensicAd;
import ggn.ameba.post.activities.ViewImageActivity;
import ggn.ameba.post.widget.RoundedCornersGaganImg;

/**
 * Created by gagandeep on 14 Apr 2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolders>
{
    private List<HomeModel> itemList;
    private Context         context;
    SharedPreferences var1;

    public HomeAdapter(Context context, List<HomeModel> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        var1 = context.getSharedPreferences("FBAdPrefs", 0);
    }

    @Override
    public HomeViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_adapterlayout, null);
        HomeViewHolders rcv = new HomeViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final HomeViewHolders holder, int position)
    {
        HomeModel homeModel = itemList.get(position);

        int                       height = 304;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
        holder.view.setTag(position);


        if (homeModel != null)
        {
            if (position == 0/*|| (Integer) holder.view.getTag() % 7 == 0*/)
            {
                holder.adViewContainer.setVisibility(View.GONE);

                holder.imgVpost.setVisibility(View.VISIBLE);
                holder.imgVpost.setImageResource(R.drawable.default_grey);
                holder.imgVpost.setLayoutParams(params);

            }
            else
            {
                holder.adViewContainer.setVisibility(View.GONE);

                holder.imgVpost.setVisibility(View.VISIBLE);

                holder.imgVpost.setImageResource(R.drawable.default_grey);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                holder.imgVpost.setLayoutParams(params2);
            }

            holder.imgVpost.setImageUrlSmall(context, homeModel.getImagePath());
            holder.imgVpost.setTag(homeModel);
            holder.imgVpost.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (GlobalConstantsG.SHOW_Ad())
                    {

                        if (!var1.getString("deviceIdHash", "").equals(""))
                        {
                            String i = r.b(UUID.randomUUID().toString());
                            var1.edit().putString("deviceIdHash", i).apply();
                            AdSettings.addTestDevice(i);
                        }

                        ShowIntrensicAd.loadInterstitialAd(context);
                    }
                    else
                    {
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        intent.putExtra("data", (HomeModel) view.getTag());
                        context.startActivity(intent);
                    }
                }
            });
        }
        else
        {
            holder.imgVpost.setVisibility(View.GONE);
            holder.adViewContainer.setLayoutParams(params);
            holder.adViewContainer.setVisibility(View.VISIBLE);

            showNativeAd(holder.adViewContainer);
        }

    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }


    public class HomeViewHolders extends RecyclerView.ViewHolder
    {
        public RoundedCornersGaganImg imgVpost;

        View view;


        LinearLayout adViewContainer;

        public HomeViewHolders(View itemView)
        {
            super(itemView);
            view = itemView;
            imgVpost = (RoundedCornersGaganImg) itemView.findViewById(R.id.imgVpost);
            adViewContainer = (LinearLayout) itemView.findViewById(R.id.adViewContainer);
        }


    }


    NativeAd nativeAd;

    private void showNativeAd(final LinearLayout layout)
    {

        LayoutInflater     inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout adView   = (LinearLayout) inflater.inflate(R.layout.ad_unit, layout);

//        AdSettings.addTestDevice("7ffbe159364e446d0a207af123739cba");
        String i = r.b(UUID.randomUUID().toString());
        if (!var1.getString("deviceIdHash", "").equals(i))
        {
            var1.edit().putString("deviceIdHash", i).apply();
        }

        AdSettings.addTestDevice(i);

        nativeAd = new NativeAd(context, "1726985874239530_1726987440906040");
//        nativeAd = new NativeAd(context, "753831574743569_875301179263274");

        // Unregister last ad
//        nativeAd.unregisterView();


        nativeAd.setAdListener(new AdListener()
        {
            @Override
            public void onError(Ad ad, AdError error)
            {
                System.out.println("Gagan");
            }

            @Override
            public void onAdLoaded(Ad ad)
            {
                if (ad != nativeAd)
                {
                    return;
                }

                inflateAd(nativeAd, adView, context);
            }

            @Override
            public void onAdClicked(Ad ad)
            {
            }
        });

        nativeAd.loadAd();
    }


    public static void inflateAd(NativeAd nativeAd, View adView, Context context)
    {
        // Create native UI using the ad metadata.

        ImageView nativeAdIcon          = (ImageView) adView.findViewById(R.id.nativeAdIcon);
        TextView  nativeAdTitle         = (TextView) adView.findViewById(R.id.nativeAdTitle);
        TextView  nativeAdBody          = (TextView) adView.findViewById(R.id.nativeAdBody);
        MediaView nativeAdMedia         = (MediaView) adView.findViewById(R.id.nativeAdMedia);
        TextView  nativeAdSocialContext = (TextView) adView.findViewById(R.id.nativeAdSocialContext);
        Button    nativeAdCallToAction  = (Button) adView.findViewById(R.id.nativeAdCallToAction);
        RatingBar nativeAdStarRating    = (RatingBar) adView.findViewById(R.id.nativeAdStarRating);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(View.VISIBLE);
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();

        nativeAdMedia.setNativeAd(nativeAd);

        NativeAd.Rating rating = nativeAd.getAdStarRating();
        if (rating != null)
        {
            nativeAdStarRating.setVisibility(View.VISIBLE);
            nativeAdStarRating.setNumStars((int) rating.getScale());
            nativeAdStarRating.setRating((float) rating.getValue());
        }
        else
        {
            nativeAdStarRating.setVisibility(View.GONE);
        }

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable
        nativeAd.registerViewForInteraction(adView);

        // Or you can replace the above call with the following function to specify the clickable areas.
        // nativeAd.registerViewForInteraction(adView,
        //     Arrays.asList(nativeAdCallToAction, nativeAdMedia));
    }


//    int            bannerWidth  = adCoverImage.getWidth();
//    int            bannerHeight = adCoverImage.getHeight();
//        WindowManager  wm           = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display        display      = wm.getDefaultDisplay();
//        DisplayMetrics metrics      = new DisplayMetrics();
//        display.getMetrics(metrics);
//        int screenWidth  = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;
//        nativeAdMedia.setLayoutParams(new FrameLayout.LayoutParams(
//                screenWidth,
//                Math.min((int) (((double) screenWidth / (double) bannerWidth) * bannerHeight), screenHeight / 3)
//        ));


}