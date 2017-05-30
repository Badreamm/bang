package cn.xcom.helper.wheel;

public class DistrictModel {
	private String name;
	private String id;

	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String id) {
		super();
		this.name = name;
		this.id = id;
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

	@Override
	public String toString() {
		return getName().trim();
	}
}
