package com.projetoles.model;

import java.util.Calendar;

import com.projetoles.controller.Controller;
import com.projetoles.controller.Dependencies;
import com.projetoles.dao.OnRequestListener;

import android.os.Parcel;
import android.os.Parcelable;

public class PreloadedObject<T extends TemporalModel> implements Comparable<PreloadedObject>, Parcelable {

	private Calendar date;
	private String id;
	private T loadedObj;

	public static final Parcelable.Creator<PreloadedObject> CREATOR = 
			new Parcelable.Creator<PreloadedObject>() {
        public PreloadedObject createFromParcel(Parcel in) {
            return new PreloadedObject(in); 
        }

        public PreloadedObject[] newArray(int size) {
            return new PreloadedObject[size];
        }
    };

	public PreloadedObject(Parcel in) {
		String id = in.readString();
		long time = in.readLong();
		setId(id);
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(time);
		setDate(date);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getId());
		dest.writeLong(this.getDate().getTimeInMillis());
	}
	
	public PreloadedObject(Calendar date, String id) {
		setDate(date);
		setId(id);
	}
	
	public PreloadedObject(long date, String id) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		setDate(c);
		setId(id);
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void load(Controller<T> controller, final OnRequestListener<T> callback) {
		controller.get(id, new OnRequestListener<T>(controller.getContext()) {
 
			@Override
			public void onSuccess(T result) {
				loadedObj = result;
				callback.onSuccess(result);
			}

			@Override
			public void onError(String errorMessage) {
				callback.onError(errorMessage);
			} 
			
			@Override
			public void onTimeout() {
				callback.onTimeout();
			}
		});
	}
	
	public T getLoadedObj() {
		return loadedObj;
	}
	
	public void setLoadedObj(T loadedObj) {
		this.loadedObj = loadedObj;
	}
	
	public boolean isLoaded() {
		return loadedObj != null;
	}
	
	@Override
	public int compareTo(PreloadedObject arg0) {
		return arg0.date.compareTo(date);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		PreloadedObject other = (PreloadedObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
