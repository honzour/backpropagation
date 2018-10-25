package cz.honza.backpropagation.network.parser;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;

public abstract class ParserResultHandler {
	public abstract void onFinished(Network network);
	public abstract void onError(String error);
	
	public void onError(int resource)
	{
		onError(NetworkApplication.sInstance.getResources().getString(resource));
	}
}
