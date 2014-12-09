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
	
	public ImportActivity mContext;
	public String mUrl;
	public Handler mHandler;
	public int mFormat;
	
	public FromWebThread(ImportActivity context, String url, int format)
	{
		mContext = context;
		mUrl = url;
		mHandler = new Handler();
		mFormat = format;
	}
	
	public void setContext(ImportActivity context)
	{
		mContext = context;
	}
	
	@Override
	public void run() {
		try
		{
			URL u = new URL(mUrl);
			final InputStream inputStream = u.openStream();
			ParserResultHandler callBack = new ParserResultHandler() {
				
				protected void enableButton()
				{
					if (mFormat == ExportActivity.EXTRA_FORMAT_CSV)
						mContext.mWebCsvButton.setEnabled(true);
					if (mFormat == ExportActivity.EXTRA_FORMAT_XML)
						mContext.mWebXmlButton.setEnabled(true);
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
			mHandler.post(new Runnable() {
				public void run() {
					Toast.makeText(NetworkApplication.sInstance, e.toString(), Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
