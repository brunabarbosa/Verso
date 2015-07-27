package com.projetoles.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Model implements Parcelable {

	protected String mId;

	public Model(Parcel in) {
		setId(in.readString()); 
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getId());
	}
	
	public Model(String id) {
		setId(id);
	}
	
	public void setId(String id) {
		this.mId = id;
	}
	
	public String getId() {
		return this.mId;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
