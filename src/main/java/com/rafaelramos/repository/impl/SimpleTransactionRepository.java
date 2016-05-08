package com.rafaelramos.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.rafaelramos.model.Transaction;
import com.rafaelramos.repository.TransactionRepository;

@Repository
public class SimpleTransactionRepository implements TransactionRepository {

	public static ConcurrentMap<Long, Transaction> TRANSACTIONS = new ConcurrentHashMap<>();

	@Override
	public Transaction save(Transaction transaction) {
		return TRANSACTIONS.put(transaction.getId(), transaction);
	}

	@Override
	public Transaction findOne(long transactionId) {
		return TRANSACTIONS.get(transactionId);
	}

	@Override
	public List<Long> listIdsByType(String type) {
		return TRANSACTIONS.values().stream().filter(t -> t.getType().equals(type)).map(t -> t.getId())
				.collect(Collectors.toList());
	}

	@Override
	public BigDecimal sumByParentId(long parentId) {
		return TRANSACTIONS.values().stream().filter(t -> t.getParent() != null && t.getParent().getId() == parentId)
				.map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
