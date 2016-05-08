package com.rafaelramos.model;

import java.math.BigDecimal;

public class Transaction {

	private long id;
	private BigDecimal amount;
	private String type;

	private Transaction parent;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Transaction getParent() {
		return parent;
	}

	public void setParent(Transaction parent) {
		this.parent = parent;
	}

}
