package edu.mta.chatty.domain;

public class SyncTimeStampSpan {
	private long beginTS;
	private long endTS;

	public long getBeginTS() {
		return beginTS;
	}

	public long getEndTS() {
		return endTS;
	}
	
	public void update(){
		synchronized (this) {
			beginTS=endTS;
			endTS=System.currentTimeMillis();
		}
	}
	
}
