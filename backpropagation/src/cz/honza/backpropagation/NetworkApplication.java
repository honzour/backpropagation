package cz.honza.backpropagation;

import cz.honza.backpropagation.learning.LearningThread;
import cz.honza.backpropagation.network.Network;
import android.app.Application;

public class NetworkApplication extends Application {

	public static LearningThread sThread;
	public volatile static Network sNetwork = null;
	public static NetworkApplication sInstance = null;
	
	public static final String PREFS = "PREFS";
	
	public static final String PREFS_DEFAULT_EXPORT_XML_FILE = "PREFS_DEFAULT_EXPORT_XML_FILE";
	public static final String PREFS_DEFAULT_IMPORT_XML_URL = "PREFS_DEFAULT_IMPORT_XML_URL";
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		sInstance = this;
	}
	
}
