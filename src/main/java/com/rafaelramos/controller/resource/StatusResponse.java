package com.rafaelramos.controller.resource;

public class StatusResponse {

	private String status;

	public StatusResponse() {
		super();
	}

	public StatusResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
