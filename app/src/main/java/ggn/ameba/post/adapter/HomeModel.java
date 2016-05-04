package ggn.ameba.post.adapter;

import java.io.Serializable;

/**
 * Created by gagandeep on 28 Apr 2016.
 */
public class HomeModel implements Serializable
{

    private String ThemePostId,CustomerId,ImagePath,CreatedDate;

    public String getThemePostId()
    {
        return ThemePostId;
    }

    public void setThemePostId(String themePostId)
    {
        ThemePostId = themePostId;
    }

    public String getCustomerId()
    {
        return CustomerId;
    }

    public void setCustomerId(String customerId)
    {
        CustomerId = customerId;
    }

    public String getImagePath()
    {
        return ImagePath;
    }

    public void setImagePath(String imagePath)
    {
        ImagePath = imagePath;
    }

    public String getCreatedDate()
    {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate)
    {
        CreatedDate = createdDate;
    }
}
