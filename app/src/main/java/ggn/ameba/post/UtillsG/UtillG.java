package ggn.ameba.post.UtillsG;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import ggn.ameba.post.R;

/**
 * Created by gagandeep on 19 Apr 2016.
 */
public class UtillG
{

    public static Toast toast;


    public static void showToast(String msg, Context context, boolean center)
    {
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (center)
        {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }

        toast.show();

    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }


    public static void showSnacky(View view, String string)
    {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
    }


    //dialog onw button
    public static Dialog global_dialog;

    public static void show_dialog_msg(final Context con, String text, View.OnClickListener onClickListener)
    {
        global_dialog = new Dialog(con, R.style.Theme_Dialog);
        global_dialog.setContentView(R.layout.dialog_global);
        global_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tex    = (TextView) global_dialog.findViewById(R.id.text);
        Button   ok     = (Button) global_dialog.findViewById(R.id.ok);
        Button   cancel = (Button) global_dialog.findViewById(R.id.cancel);


        tex.setText(text);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(global_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        global_dialog.show();
        global_dialog.getWindow().setAttributes(lp);


        if (onClickListener != null)
        {
            cancel.setVisibility(View.VISIBLE);
            // ok.setOnClickListener(oc);
            cancel.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });


            ok.setOnClickListener(onClickListener);

        }
        else
        {
            ok.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    global_dialog.dismiss();

                }
            });
        }


    }


    public static void ShowToastImage(Context context, int imagetoShow)
    {
        View layout = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_toast, null);

        ImageView view = (ImageView) layout.findViewById(R.id.imgToast);

//        view.setImageResource(imagetoShow);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

//        TranslateAnimation animation = new TranslateAnimation(0, 0, fromY, toY);
        ObjectAnimator y = ObjectAnimator.ofFloat(view,
                "translationY", layout.getBottom() + 200, layout.getBottom() + 100, layout.getBottom() + 50, layout.getBottom() + 20, layout.getBottom() + 10, 0, 0, 0, 0, 0, 0, 0);

//        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0, 0, 0, 0, 0, 6, -8, 8, -8, 8, -8, 0);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.4f, 0.6f, 0.8f, 0.9f, 1, 1, 1, 1, 1, 1, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.4f, 0.6f, 0.8f, 0.9f, 1, 1, 1, 1, 1, 1, 1);

        final AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(y, scaleX, scaleY);
        mAnimatorSet.setDuration(1700);
        mAnimatorSet.start();
    }


    public static void transitionToActivity(Activity activity, Intent intent, View logo, String transitionName)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent i = intent;
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    android.util.Pair.create(logo, transitionName)
            );
            activity.startActivity(i, options.toBundle());

        }
        else
        {
            activity.startActivity(intent);
        }
    }


    public static String locationName(Context context, LatLng latLng)
    {
        try
        {

            Geocoder      geo       = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.isEmpty())
            {

                return "Unknown Location";
            }
            else
            {
                if (addresses.size() > 0)
                {
                    return addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "Unknown Location";
    }


}
