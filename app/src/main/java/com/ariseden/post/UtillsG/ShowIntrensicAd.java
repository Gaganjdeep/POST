package com.ariseden.post.UtillsG;

import android.content.Context;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

/**
 * Created by gagandeep on 01 Jun 2016.
 */
public class ShowIntrensicAd
{
    // Declare the InterstitialActivity in AndroidManifest.xml:
//  <activity android:name="com.facebook.ads.InterstitialAdActivity"         android:configChanges="keyboardHidden|orientation" />
// In the Activity that will launch the interstitial,
// implement the AdListener interface and add the following:

    public static void loadInterstitialAd(Context context)
    {
        final InterstitialAd interstitialAd;

        interstitialAd = new InterstitialAd(context, "1726985874239530_1729230294015088");
        interstitialAd.setAdListener(new InterstitialAdListener()
        {
            @Override
            public void onInterstitialDisplayed(Ad ad)
            {

            }

            @Override
            public void onInterstitialDismissed(Ad ad)
            {

            }

            @Override
            public void onError(Ad ad, AdError adError)
            {

            }

            @Override
            public void onAdLoaded(Ad ad)
            {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad)
            {

            }
        });
        interstitialAd.loadAd();
    }

}
