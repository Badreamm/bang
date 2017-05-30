package cn.xcom.helper.wheel;

import java.util.ArrayList;


public class ProvinceModel {
	private String name;
	private String id;
	private ArrayList<CityModel> cityList;

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, String id, ArrayList<CityModel> cityList) {
		super();
		this.name = name;
		this.id = id;
		this.cityList = cityList;
	}

	public ProvinceModel(String name, ArrayList<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		if (name==null||name.equals("null")) {
			name="";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return getName().trim();
	}
}
