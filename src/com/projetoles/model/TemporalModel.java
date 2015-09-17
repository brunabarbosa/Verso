package com.projetoles.model;

import java.util.Calendar;

import android.os.Parcel;

public abstract class TemporalModel extends Model implements Comparable<TemporalModel> {

	protected Calendar mDataCriacao;

	public TemporalModel(Parcel in) {
		super(in);
		setDataCriacao(CalendarUtils.longToCalendar(in.readLong()));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(this.getDataCriacao().getTimeInMillis());
	}
	
	public TemporalModel(String id, Calendar dataCriacao) {
		super(id);
		setDataCriacao(dataCriacao);
	}
	
	public TemporalModel() {
		super("");
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.mDataCriacao = dataCriacao;
	}
	
	public Calendar getDataCriacao() {
		return this.mDataCriacao;
	}
	
	public String getStringDataCriacao() {
		return CalendarUtils.getTimeInMilli(this.getDataCriacao());
	}
  
	@Override
	public int compareTo(TemporalModel other) {
		return other.getDataCriacao().compareTo(this.getDataCriacao());
	}
	
}
