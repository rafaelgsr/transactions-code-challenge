package com.rafaelramos.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.rafaelramos.controller.resource.TransactionResource;
import com.rafaelramos.model.Transaction;
import com.rafaelramos.service.TransactionService;

/**
 * Given the simple entities this Controller handles, I think the best approach
 * would be to use a RepositoryRestResource.
 * 
 * @author rafaelramos
 *
 */
@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	/**
	 * When possible, I prefer to specify my DTO's for request and response. It
	 * gives me a clear separation between the Business and Message layers.
	 * 
	 * Normally PUT methods are idempotent and invoking this method with the
	 * same parameters should return the same result. But since the
	 * specification said "transaction_id is a long specifying a new
	 * transaction", I assumed that this method is intended for insertion only,
	 * instead of "upsert".
	 * 
	 * @param request
	 * @param id
	 */
	@RequestMapping(value = "/transaction/{id:[0-9]+}", method = RequestMethod.PUT)
	public @ResponseBody Map<String, String> insert(@RequestBody TransactionResource request, @PathVariable long id) {
		this.transactionService.save(id, request.getAmount(), request.getType(), request.getParentId());
		return ImmutableMap.of("status", "ok");
	}

	@RequestMapping(value = "/transaction/{id:[0-9]+}", method = RequestMethod.GET)
	public @ResponseBody TransactionResource get(@PathVariable long id) {

		Transaction transaction = this.transactionService.findOne(id);

		TransactionResource resource = new TransactionResource();
		resource.setAmount(transaction.getAmount());
		resource.setType(transaction.getType());
		if (transaction.getParent() != null) {
			resource.setParentId(transaction.getParent().getId());
		}

		return resource;

	}

	@RequestMapping(value = "/transaction/types/{type}", method = RequestMethod.GET)
	public @ResponseBody List<Long> listByTypes(@PathVariable String type) {
		return this.transactionService.listByType(type);
	}

	@RequestMapping(value = "/sum/{id:[0-9]+}", method = RequestMethod.GET)
	public @ResponseBody Map<String, BigDecimal> sum(@PathVariable long id) {
		return ImmutableMap.of("sum", this.transactionService.sumByParentId(id));
	}

}
