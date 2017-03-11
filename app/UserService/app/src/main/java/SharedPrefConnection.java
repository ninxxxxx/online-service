import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arnon on 6/7/2559.
 */
public class SharedPrefConnection {

    public static SharedPreferences sharedPreferences;


    public static SharedPreferences getSharedPreference(Context context){

        return context.getSharedPreferences("connection", context.MODE_PRIVATE);
    }
//    public static setValue


}
