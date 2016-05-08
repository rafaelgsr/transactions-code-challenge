package com.rafaelramos.controller.resource;

import java.math.BigDecimal;

public class TransactionSumResponse {
	
	private BigDecimal sum;
	
	public TransactionSumResponse() {
		super();
	}

	public TransactionSumResponse(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

}
