package ggn.ameba.post.activities.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ggn.ameba.post.R;
import ggn.ameba.post.UtillsG.SharedPrefHelper;

/**
 * Created by gagandeep on 28 Apr 2016.
 */
public class BaseFragmentG extends Fragment
{


    public Dialog           dialog;
    public SharedPrefHelper sharedPrefHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        sharedPrefHelper = new SharedPrefHelper(getActivity());
        super.onCreate(savedInstanceState);
    }

    public SharedPrefHelper getLocaldata()
    {
        return sharedPrefHelper;
    }


    public void showProgress()
    {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.show();

    }


    public void cancelProgress()
    {
        if (dialog != null)
        {
            dialog.cancel();
        }
    }

}