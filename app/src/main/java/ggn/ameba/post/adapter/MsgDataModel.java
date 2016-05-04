package ggn.ameba.post.adapter;

/**
 * Created by gagandeep on 20 Apr 2016.
 */
public class MsgDataModel
{


    private boolean lastMsg;

    private String id, sender_userid, recipient_userid, message, created_at, username, profile_pic;


    public String getCreated_at()
    {
        return created_at;
    }

    public boolean isLastMsg()
    {
        return lastMsg;
    }

    public void setLastMsg(boolean lastMsg)
    {
        this.lastMsg = lastMsg;
    }

    public MsgDataModel()
    {
    }


    public void setId(String id)
    {
        this.id = id;
    }

    public void setSender_userid(String sender_userid)
    {
        this.sender_userid = sender_userid;
    }

    public void setRecipient_userid(String recipient_userid)
    {
        this.recipient_userid = recipient_userid;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setProfile_pic(String profile_pic)
    {
        this.profile_pic = profile_pic;
    }


    public String getId()
    {
        return id;
    }

    public String getSender_userid()
    {
        return sender_userid;
    }

    public String getRecipient_userid()
    {
        return recipient_userid;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUsername()
    {
        return username;
    }

    public String getProfile_pic()
    {
        return profile_pic;
    }
}
