package cn.xcom.helper.wheel;

import java.util.ArrayList;
import java.util.List;


public class CityModel {
	private String name;
	private String id;
	private ArrayList<DistrictModel> districtList;

	public CityModel() {
		super();
	}

	public CityModel(String name, String id, ArrayList<DistrictModel> districtList) {
		super();
		this.name = name;
		this.id = id;
		this.districtList = districtList;
	}

	public CityModel(String name, ArrayList<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
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

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(ArrayList<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return getName().trim();
	}
}
