package com.rafaelramos;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.rafaelramos.controller.resource.TransactionResource;
import com.rafaelramos.model.Transaction;
import com.rafaelramos.repository.TransactionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TransactionsApplication.class)
@WebIntegrationTest(randomPort = true)
public class TransactionsTests {

	@Autowired
	private TransactionRepository repository;

	@Value("${local.server.port}")
	private int port;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void testInsertTransaction() {

		TransactionResource request = new TransactionResource();
		request.setAmount(BigDecimal.valueOf(5000));
		request.setType("cars");

		long id = new Random().nextInt(Integer.MAX_VALUE);

		RestTemplate template = new TestRestTemplate();
		ResponseEntity<Void> response = template.exchange(this.getBaseUrl() + "/transactionservice/transaction/" + id,
				HttpMethod.PUT, new HttpEntity<>(request), Void.class);
		assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));

		Transaction transaction = this.repository.findOne(id);
		assertNotNull(transaction);
		assertThat(transaction.getAmount(), is(BigDecimal.valueOf(5000)));
		assertThat(transaction.getType(), is("cars"));
		assertNull(transaction.getParent());

	}

	@Test
	public void testTransactionNotFound() {
		when().get(this.getBaseUrl() + "/transactionservice/transaction/{id}", new Random().nextInt(Integer.MAX_VALUE))
				.then().statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void testFindTransaction() {

		int parentId = new Random().nextInt(Integer.MAX_VALUE);
		Transaction parent = new Transaction();
		parent.setId(parentId);
		parent.setAmount(BigDecimal.valueOf(7000));
		parent.setType("cars");
		this.repository.save(parent);

		int id = new Random().nextInt(Integer.MAX_VALUE);
		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setAmount(BigDecimal.valueOf(5000));
		transaction.setType("cars");
		transaction.setParent(parent);
		this.repository.save(transaction);

		parent.getChildren().add(transaction);

		when().get(this.getBaseUrl() + "/transactionservice/transaction/{id}", id).then()
				.statusCode(HttpStatus.OK.value()).body("amount", is(5000)).body("parent_id", is(parentId))
				.body("type", is("cars"));

	}

	@Test
	public void testListByType() {

		List<Integer> ids = new Random().ints(5, 0, Integer.MAX_VALUE).boxed().collect(Collectors.toList());
		ids.forEach(id -> {
			Transaction transaction = new Transaction();
			transaction.setId(id);
			transaction.setAmount(BigDecimal.valueOf(5000));
			transaction.setType("trucks");
			this.repository.save(transaction);
		});

		when().get(this.getBaseUrl() + "/transactionservice/transaction/types/trucks").then()
				.statusCode(HttpStatus.OK.value()).body("$", containsInAnyOrder(ids.toArray(new Integer[ids.size()])));

		when().get(this.getBaseUrl() + "/transactionservice/transaction/types/planes").then()
				.statusCode(HttpStatus.OK.value()).body("$", IsEmptyCollection.empty());

	}

	@Test
	public void testSumByParentId() {

		int parentId = new Random().nextInt(Integer.MAX_VALUE);
		Transaction parent = new Transaction();
		parent.setId(parentId);
		parent.setAmount(BigDecimal.valueOf(7000));
		parent.setType("cars");
		this.repository.save(parent);

		Transaction transaction1 = new Transaction();
		transaction1.setId(new Random().nextInt(Integer.MAX_VALUE));
		transaction1.setAmount(BigDecimal.valueOf(5000));
		transaction1.setType("cars");
		transaction1.setParent(parent);
		this.repository.save(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setId(new Random().nextInt(Integer.MAX_VALUE));
		transaction2.setAmount(BigDecimal.valueOf(10000));
		transaction2.setType("bikes");
		transaction2.setParent(parent);
		this.repository.save(transaction2);

		parent.getChildren().add(transaction1);
		parent.getChildren().add(transaction2);

		when().get(this.getBaseUrl() + "/transactionservice/sum/{id}", parentId).then()
				.statusCode(HttpStatus.OK.value()).body("sum", is(22000));

		when().get(this.getBaseUrl() + "/transactionservice/sum/{id}", transaction1.getId()).then()
				.statusCode(HttpStatus.OK.value()).body("sum", is(5000));

	}

}
