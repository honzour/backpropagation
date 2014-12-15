package cz.honza.backpropagation;

import java.io.ByteArrayInputStream;

import cz.honza.backpropagation.learning.LearningThread;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.Parser;
import cz.honza.backpropagation.network.ParserResultHandler;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class NetworkApplication extends Application {

	public static LearningThread sThread;
	public volatile static Network sNetwork = null;
	public static NetworkApplication sInstance = null;
	
	public static final String PREFS = "PREFS";
	
	public static final String PREFS_DEFAULT_EXPORT_XML_FILE = "PREFS_DEFAULT_EXPORT_XML_FILE";
	public static final String PREFS_DEFAULT_EXPORT_CSV_FILE = "PREFS_DEFAULT_EXPORT_CSV_FILE";
	
	public static final String PREFS_DEFAULT_IMPORT_CSV_URL = "PREFS_DEFAULT_IMPORT_CSV_URL";
	public static final String PREFS_DEFAULT_IMPORT_XML_URL = "PREFS_DEFAULT_IMPORT_XML_URL";
	public static final String PREFS_STORED_NET = "PREFS_STORED_NET";
	
	public static final String HELP_URL_ROOT = "http://backpropagation.moxo.cz/2.0/";
	
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
		final String net = loadPref(PREFS_STORED_NET, null);
		if (net != null)
		{
			try
			{
				Parser.parseXml(new ByteArrayInputStream(net.getBytes()), new ParserResultHandler() {
					@Override
					public void onFinished(Network network) {
						sNetwork = network;
					}
				
					@Override
					public void onError(String error) {
						// do nothing
					}
				});
			}
			catch (Exception e)
			{
				// do nothing
			}
		}
		
	}
}
