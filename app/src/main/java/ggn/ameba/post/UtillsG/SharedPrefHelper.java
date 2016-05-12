package ggn.ameba.post.UtillsG;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gagandeep on 25 Apr 2016.
 */
public class SharedPrefHelper
{
    static final public String PREF_NAME = "postAppG";

    SharedPreferences sharedPreferences;
    Context           context;


    public final String USERID    = "UserId";
    public final String NAME      = "Name";
    public final String PHOTO_PIC = "photourl";
    public final String EMAIL     = "email";
    public final String TAGLINE   = "tagLine";

    SharedPreferences.Editor edit;

    public SharedPrefHelper(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
    }


    public void logOut()
    {
        edit.clear();
        edit.apply();
    }


    public void setUserid(String userid)
    {
        edit.putString(USERID, userid);
        edit.apply();
    }

    public void setName(String name)
    {
        edit.putString(NAME, name);
        edit.apply();
    }


    public void setPhotoUrl(String photoUrl)
    {
        edit.putString(PHOTO_PIC, photoUrl);
        edit.apply();
    }

    public void setEmail(String email)
    {
        edit.putString(EMAIL, email);
        edit.apply();
    }


    public void setTagLine(String tag)
    {
        edit.putString(TAGLINE, tag);
        edit.apply();
    }

    public String getTagLine()
    {
        return sharedPreferences.getString(TAGLINE, "");
    }


    public String getEmail()
    {
        return sharedPreferences.getString(EMAIL, "");
    }


    public String getUserid()
    {
        return sharedPreferences.getString(USERID, "");
    }

    public String getName()
    {
        return sharedPreferences.getString(NAME, "");
    }


    public String getPhotoUrl()
    {
        return sharedPreferences.getString(PHOTO_PIC, "");
    }


//    THEME INFO
//{"ThemeID":38,"ThemeName":"Food","CustomerId":527,"ThemeStatusId":1,"Overview":"Food
//
//    Contest","ThemeStartDate":"2016-04-27T00:00:00","ThemeEndDate":"2016-04-30T00:00:00","CreatedDate":"2016-04-
//
//        27T17:54:03.283","IsActive":true}}


    public void setThemeID(String ThemeID)
    {
        edit.putString("ThemeID", ThemeID);
        edit.apply();
    }

    public String getThemeID()
    {
        return sharedPreferences.getString("ThemeID", "38");
    }

    public void setThemeName(String ThemeName)
    {
        edit.putString("ThemeName", ThemeName);
        edit.apply();
    }

    public void setThemeOverview(String Overview)
    {
        edit.putString("Overview", Overview);
        edit.apply();
    }


    public void setThemeEndDate(String ThemeEndDate)
    {
        edit.putString("ThemeEndDate", ThemeEndDate);
        edit.apply();
    }

    public void setThemeStartDate(String ThemeStartDate)
    {
        edit.putString("ThemeStartDate", ThemeStartDate);
        edit.apply();
    }

    public String getThemeName()
    {
        return sharedPreferences.getString("ThemeName", "Food");
    }


    public String getThemeOverview()
    {
        return sharedPreferences.getString("Overview", "");
    }

    public String getThemeEndDate()
    {
        return sharedPreferences.getString("ThemeEndDate", "2016-05-20T16:47:00");
    }


    public String getThemeStartDate()
    {
        return sharedPreferences.getString("ThemeStartDate", "");
    }


//    END OF THEME INFO


}
