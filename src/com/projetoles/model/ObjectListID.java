package com.projetoles.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectListID implements Parcelable {

	private List<String> mList = new ArrayList<String>();

	public static final Parcelable.Creator<ObjectListID> CREATOR = 
			new Parcelable.Creator<ObjectListID>() {
        public ObjectListID createFromParcel(Parcel in) {
            return new ObjectListID(in); 
        }

        public ObjectListID[] newArray(int size) {
            return new ObjectListID[size];
        }
    };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringList(mList);
	}
	
	public ObjectListID() {
		
	}
	
	public ObjectListID(Parcel in) {
		in.readStringList(mList);
	}
	
	public ObjectListID(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			this.mList.add(array.get(i).toString());
		}
	}
	
	public void add(String id) {
		if (!this.mList.contains(id)) {
			this.mList.add(id);
		}
	}

	public boolean remove(String id) {
		return this.mList.remove(id);
	}

	public boolean isEmpty() {
		return this.mList.isEmpty();
	}
	
	public int size() {
		return this.mList.size();
	}

	public boolean contains(String id) {
		return this.mList.contains(id);
	}
		
	public String getIntersecction(ObjectListID b) {
		for (String i : this.mList) {
			for (String j : b.mList) {
				if (i.equals(j))
					return i;
			}
		}
		return null;
	}
	
	public List<String> getList() {
		return Collections.unmodifiableList(this.mList);
	}

}
