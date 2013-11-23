package com.example.contactform;

import java.util.Comparator;

public class PhoneModel implements Comparator<PhoneModel>{

	private String  name;
	private String  phone;
	private boolean isSection;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isSection() {
		return isSection;
	}
	public void setSection(boolean isSection) {
		this.isSection = isSection;
	}
	@Override
	public int compare(PhoneModel lhs, PhoneModel rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
