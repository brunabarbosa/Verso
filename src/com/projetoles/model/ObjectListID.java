package com.projetoles.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectListID<T extends TemporalModel> implements Parcelable {

	private List<PreloadedObject<T> > mList = new ArrayList<PreloadedObject<T> >();

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
		dest.writeList(mList);
	}
	
	public ObjectListID() {
		
	}
	
	public ObjectListID(Parcel in) {
		mList = in.readArrayList(PreloadedObject.class.getClassLoader());
	}
	
	public ObjectListID(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			String id = array.getJSONObject(i).getString("id");
			long date = Long.valueOf(array.getJSONObject(i).getString("date"));
			this.mList.add(new PreloadedObject<T>(date, id));
		}
	}
	
	public void add(String id, long date) {
		PreloadedObject<T> obj = new PreloadedObject<T>(date, id);
		if (!this.mList.contains(obj)) {
			this.mList.add(obj);
		}
	}
	
	public void add(PreloadedObject<T> obj) {
		if (!this.mList.contains(obj)) {
			this.mList.add(obj);
		}
	}
	
	public PreloadedObject<T> get(int index) {
		return mList.get(index);
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
		
	public PreloadedObject<T> getIntersecction(ObjectListID<T> b) {
		for (PreloadedObject<T> i : this.mList) {
			for (PreloadedObject<T> j : b.mList) {
				if (i.equals(j))
					return i;
			}
		}
		return null;
	}
	
	public List<PreloadedObject<T> > getList() {
		return Collections.unmodifiableList(this.mList);
	}

	public void setList(List<PreloadedObject<T> > list) {
		this.mList = list;
	}
	
	public void sort() {
		Collections.sort(this.mList);
	}
	
}
