package com.android.smsDatabase.model;

public class SmsModel {

	int id;

	String message, contactName, contactNumber;;

	long sendTime, initialTime,contact_id, photo_id;
	


	public long getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(long photo_id) {
		this.photo_id = photo_id;
	}

	public long getContact_id() {
		return contact_id;
	}

	public void setContact_id(long contact_id) {
		this.contact_id = contact_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public Long getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(long settingTime) {
		this.initialTime = settingTime;
	}

}
