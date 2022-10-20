package com.jwtathentication.bean;

public class Survey {
	private String key;
	private String farmName;
	private String cordinates;
	private String state;
	private String district;
	private String taluk;
	private String crop;
	private int surveyId;
	private long sowingdate;
	private int area;
	private String unit;
	private int subSurveyId;
	private String village;
	private String irrigationType;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFarmName() {
		return farmName;
	}
	public void setFarmName(String farmName) {
		this.farmName = farmName;
	}

	public String getCordinates() {
		return cordinates;
	}
	public void setCordinates(String cordinates) {
		this.cordinates = cordinates;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTaluk() {
		return taluk;
	}
	public void setTaluk(String taluk) {
		this.taluk = taluk;
	}
	public String getCrop() {
		return crop;
	}
	public void setCrop(String crop) {
		this.crop = crop;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getIrrigationType() {
		return irrigationType;
	}
	public void setIrrigationType(String irrigationType) {
		this.irrigationType = irrigationType;
	}
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	public long getSowingdate() {
		return sowingdate;
	}
	public void setSowingdate(long sowingdate) {
		this.sowingdate = sowingdate;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getSubSurveyId() {
		return subSurveyId;
	}
	public void setSubSurveyId(int subSurveyId) {
		this.subSurveyId = subSurveyId;
	}
	
}
