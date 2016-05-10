package ggn.ameba.post.UtillsG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by gagandeep on 22 Dec 2015.
 */
public class GlobalConstantsG
{
    public static final String SENDER_ID = "994056203006";
    public static       String NAME      = "name", NOTES = "notes", DEADLINE = "deadline", ISCOMPLETED = "isCompleted", PHONE = "phone";

    public static DateFormat dateformat = new SimpleDateFormat("MMM d, hh:mm a", Locale.ENGLISH);

    //                                                    2016-04-30T00:00:00


    final public static int REQUESTCODE_CAMERA  = 11;
    final public static int REQUESTCODE_GALLERY = 22;


    public static final String URL = "http://112.196.34.42:8089/";

    public static final String Status  = "Status";
    public static final String Message = "Message";
    public static final String success = "success";


    public static final String BROADCAST_GETUNREAD = "ggn.post.get_unread";
    public static final String BROADCAST_UPDATE_HOME = "ggn.post.update_home";

}
