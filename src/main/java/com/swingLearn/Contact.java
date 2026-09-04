package com.swingLearn;

public class Contact {
	private int id;
	private String fname;
	private String lname;
	private String email;
	private String phone;
	private int departmentId;
	private String departmentName;

	public Contact(String fname, String lname, String email, String phone, int departmentId) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phone = phone;
		this.departmentId = departmentId;
	}

	public Contact(int id, String fname, String lname, String email, String phone, int departmentId) {
		this(id, fname, lname, email, phone, departmentId, null);
	}

	public Contact(int id, String fname, String lname, String email, String phone, int departmentId,
			String departmentName) {
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phone = phone;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", phone=" + phone
				+ ", departmentId=" + departmentId + "]";
	}

	public int getId() {
		return id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getName() {
		return getFname() + " " + getLname();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}
}
