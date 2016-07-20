package com.atm.chatonline.bbs.commom;
/**
 *这个类是用来判断当前是否有连接网络，如果有则返回true，没有则返回false
 *2015-7-28-郑 
 */
 
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IsNetworkAvailable extends Activity {
	 
		 public boolean isNetworkAvailable(Activity activity)
		    {
		        Context context = activity.getApplicationContext();
		        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        
		        if (connectivityManager == null)
		        {
		            return false;
		        }
		        else
		        {
		            // 获取NetworkInfo对象
		            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
		            
		            if (networkInfo != null && networkInfo.length > 0)
		            {
		                for (int i = 0; i < networkInfo.length; i++)
		                {
		                    
		                    // 判断当前网络状态是否为连接状态
		                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
		                    {
		                        return true;
		                    }
		                }
		            }
		        }
		        return false;
		    }

	
}
