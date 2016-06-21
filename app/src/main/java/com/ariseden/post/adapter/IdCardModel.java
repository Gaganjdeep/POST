package com.ariseden.post.adapter;

import java.io.Serializable;

/**
 * Created by gagandeep on 11 May 2016.
 */
public class IdCardModel implements Serializable
{
    private String ProfilePic, Name, Email,TagLine, UserId;


    public IdCardModel(String profilePic, String name, String email, String tagLine, String userId)
    {
        ProfilePic = profilePic;
        Name = name;
        Email = email;
        TagLine = tagLine;
        UserId = userId;
    }

    public String getProfilePic()
    {
        return ProfilePic;
    }

    public String getName()
    {
        return Name;
    }

    public String getEmail()
    {
        return Email;
    }

    public String getUserId()
    {
        return UserId;
    }

    public String getTagLine()
    {
        return TagLine;
    }
}
