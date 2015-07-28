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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mId == null) ? 0 : mId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (mId == null) {
			if (other.mId != null)
				return false;
		} else if (!mId.equals(other.mId))
			return false;
		return true;
	}

}
