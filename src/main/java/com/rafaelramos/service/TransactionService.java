package com.rafaelramos.service;

import java.math.BigDecimal;
import java.util.List;

import com.rafaelramos.model.Transaction;

public interface TransactionService {

	public Transaction save(long id, BigDecimal amount, String type, Long parentId);
	
	public Transaction findOne(long id);
	
	public List<Long> listByType(String type);
	
	public BigDecimal sumByParentId(long parentId);
	
}
