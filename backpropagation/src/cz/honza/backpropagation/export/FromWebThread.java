package cz.honza.backpropagation.export;

import java.io.InputStream;
import java.net.URL;

import android.os.Handler;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.Parser;
import cz.honza.backpropagation.network.ParserResultHandler;

public class FromWebThread extends Thread {
	
	public ImportDataActivity mContext;
	public String mUrl;
	public Handler mHandler;
	public int mFormat;
	
	public FromWebThread(ImportDataActivity context, String url, int format)
	{
		mContext = context;
		mUrl = url;
		mHandler = new Handler();
		mFormat = format;
	}
	
	public void setContext(ImportDataActivity context)
	{
		mContext = context;
	}
	
	@Override
	public void run() {
		
		ParserResultHandler callBack = new ParserResultHandler() {
			
			protected void enableButton()
			{
				mContext.mWebButton.setEnabled(true);
			}
			
			@Override
			public void onFinished(final Network network) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						NetworkApplication.sNetwork = network;
						if (mContext != null)
							mContext.finish();
					}
				});
			}
			
			@Override
			public void onError(final String error) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(NetworkApplication.sInstance, error, Toast.LENGTH_LONG).show();
						enableButton();
					}
				});
			}
		};
		
		try
		{
			URL u = new URL(mUrl);
			final InputStream inputStream = u.openStream();
			
			
			if (mFormat == ExportActivity.EXTRA_FORMAT_CSV)
			{
				Parser.parseCsv(inputStream, callBack);
			}
			else
				Parser.parseXml(inputStream, callBack);
			inputStream.close();
		}
		catch (final Throwable e)
		{
			callBack.onError(e.toString());
		}
	}
}
