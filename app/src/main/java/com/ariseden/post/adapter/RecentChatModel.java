package com.ariseden.post.adapter;

import java.io.Serializable;

/**
 * Created by gagandeep on 03 May 2016.
 */
public class RecentChatModel implements Serializable
{
    private String CustomerIdTo,CustomerName,ChatContent,DateTimeCreated,PhotoPath,IsRead;

    public String getCustomerIdTo()
    {
        return CustomerIdTo;
    }

    public void setCustomerIdTo(String customerIdTo)
    {
        CustomerIdTo = customerIdTo;
    }

    public String getCustomerName()
    {
        return CustomerName;
    }

    public void setCustomerName(String customerName)
    {
        CustomerName = customerName;
    }

    public String getChatContent()
    {
        return ChatContent;
    }

    public void setChatContent(String chatContent)
    {
        ChatContent = chatContent;
    }

    public String getDateTimeCreated()
    {
        return DateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated)
    {
        DateTimeCreated = dateTimeCreated;
    }

    public String getPhotoPath()
    {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath)
    {
        PhotoPath = photoPath;
    }

    public String getIsRead()
    {
        return IsRead;
    }

    public void setIsRead(String isRead)
    {
        IsRead = isRead;
    }
}
