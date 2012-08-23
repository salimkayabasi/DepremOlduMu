package com.minikod.deprem.model;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
@PersistenceCapable
public class Deprem implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Date time;

	@Persistent
	private Double lng;

	@Persistent
	private Double lat;

	@Persistent
	private Double depth;

	@Persistent
	private Double magnitudeMD;

	@Persistent
	private Double magnitudeML;

	@Persistent
	private Double magnitudeMW;

	@Persistent
	private String region;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getDepth() {
		return depth;
	}

	public void setDepth(Double depth) {
		this.depth = depth;
	}

	public Double getMagnitudeMD() {
		return magnitudeMD;
	}

	public void setMagnitudeMD(Double magnitudeMD) {
		this.magnitudeMD = magnitudeMD;
	}

	public Double getMagnitudeML() {
		return magnitudeML;
	}

	public void setMagnitudeML(Double magnitudeML) {
		this.magnitudeML = magnitudeML;
	}

	public Double getMagnitudeMS() {
		return magnitudeMW;
	}

	public void setMagnitudeMW(Double magnitudeMW) {
		this.magnitudeMW = magnitudeMW;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

}
