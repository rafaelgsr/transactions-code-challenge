package com.rafaelramos.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafaelramos.model.Transaction;
import com.rafaelramos.repository.TransactionRepository;
import com.rafaelramos.service.TransactionService;
import com.rafaelramos.service.exception.EntityNotFoundException;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository repository;

	@Override
	public Transaction save(long id, BigDecimal amount, String type, Long parentId) {

		Validate.notNull(amount);
		Validate.notBlank(type);

		// TODO validate if there is already a transaction with the specified
		// id?

		Transaction parent = null;
		if (parentId != null) {
			parent = this.repository.findOne(parentId);
			Validate.notNull(parent);
		}

		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setAmount(amount);
		transaction.setType(type);
		transaction.setParent(parent);
		this.repository.save(transaction);

		if (parent != null) {
			parent.getChildren().add(transaction);
		}

		return transaction;

	}

	@Override
	public Transaction findOne(long id) {
		Transaction transaction = this.repository.findOne(id);
		if (transaction == null) {
			throw new EntityNotFoundException("not found: " + id);
		}
		return transaction;
	}

	@Override
	public List<Long> listByType(String type) {
		return this.repository.listIdsByType(type);
	}

	@Override
	public BigDecimal sumByParentId(long parentId) {
		Transaction transaction = this.repository.findOne(parentId);
		if (transaction == null) {
			throw new EntityNotFoundException("not found: " + parentId);
		}

		return transaction.getChildren().stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
