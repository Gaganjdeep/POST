package ggn.ameba.post.UtillsG;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
