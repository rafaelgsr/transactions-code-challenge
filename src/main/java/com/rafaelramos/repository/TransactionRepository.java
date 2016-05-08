package com.rafaelramos.repository;

import java.util.List;

import com.rafaelramos.model.Transaction;

/**
 * The best approach would be using a JpaRepository and an in-memory database
 * (such as HSQL). Since the test specification said not to use SQL, I
 * implemented a simple map-based structure to store the values.
 * 
 * @author rafaelramos
 *
 */
public interface TransactionRepository {

	public Transaction save(Transaction transaction);
	
	public Transaction findOne(long transactionId);
	
	public List<Long> listIdsByType(String type);
	
}
