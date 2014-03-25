package com.android.smsDatabase.model;

import java.util.Date;

public class SmsModel {

	String message, contactName;
	int contactNumber;
	Long sendTime, settingTime;

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

	public int getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public Long getSettingTime() {
		return settingTime;
	}

	public void setSettingTime(Long settingTime) {
		this.settingTime = settingTime;
	}

}
