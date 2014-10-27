package cz.honza.backpropagation;

import cz.honza.backpropagation.learning.LearningThread;
import cz.honza.backpropagation.network.Network;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class NetworkApplication extends Application {

	public static LearningThread sThread;
	public volatile static Network sNetwork = null;
	public static NetworkApplication sInstance = null;
	
	public static final String PREFS = "PREFS";
	
	public static final String PREFS_DEFAULT_EXPORT_XML_FILE = "PREFS_DEFAULT_EXPORT_XML_FILE";
	public static final String PREFS_DEFAULT_IMPORT_XML_URL = "PREFS_DEFAULT_IMPORT_XML_URL";
	
	
	public void savePref(String key, String value)
	{
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = prefs.edit();
		e.putString(key, value);
		e.commit();
	}
	
	
	public String loadPref(String key, String defaultVaule)
	{
		final SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		return prefs.getString(key, defaultVaule);
	}
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		sInstance = this;
		
	}
}
