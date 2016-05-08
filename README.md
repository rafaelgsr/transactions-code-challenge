Transactions REST API
======================================

### About

This is a Spring Boot based project that provides simple Transaction operations, such as insert, filter and reduce.

It can be executed using **TransactionsApplication**.

### Tests

The operations can be tested with **TransactionsTest**.

### Notes

* Given the simple entities this Controller handles, I think the best approach would be to use a RepositoryRestResource.
* When possible, I prefer to specify my DTO's for request and response. It gives me a clear separation between the Business and Message layers.
* Normally PUT methods are idempotent and invoking this method with the same parameters should return the same result. But since the specification said "transaction_id is a long specifying a new transaction", I assumed that this method is intended for insertion only, instead of "upsert".
* The best approach would be using a JpaRepository and an in-memory database (such as HSQL). Since the test specification said not to use SQL, I implemented a simple map-based structure to store the values.
* The sum operation could also be done directly in the repository (even better if we were using a database, since it would avoid loading the whole entity to perform some calculation that could be done by a simple *sum()*).