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


        public static final String URL = "http://132.148.25.44:8090/";
//    public static final String URL = "http://169.45.133.92:89/";

    public static final String Status  = "Status";
    public static final String Message = "Message";
    public static final String success = "success";


    public static final String BROADCAST_GETUNREAD   = "ggn.post.get_unread";
    public static final String BROADCAST_UPDATE_HOME = "ggn.post.update_home";


    public static       int INTRENSIC_COUNT       = 0;
    public static final int INTRENSIC_TOTAL_COUNT = 5;


    public static boolean SHOW_Ad()
    {
        INTRENSIC_COUNT++;

        if (INTRENSIC_COUNT > INTRENSIC_TOTAL_COUNT)
        {
            INTRENSIC_COUNT = 0;
            return true;
        }

        return false;
    }


}
