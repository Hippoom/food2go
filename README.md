food2go
=======

# Branches

## deploy-skeleton-mvn

**Summary**: The first step is to develop a very small but deployable application.  
**Stories**: <a href="https://github.com/Hippoom/food2go/blob/deploy-skeleton-mvn/food2go-webapp/src/test/stories/deploy.feature">Server startup</a>.  
**Description**: This process should be quick and straightforward. Access the welcome page(the only page needed so far) to check whether the application is deployed, <a href="https://github.com/Hippoom/food2go/blob/deploy-skeleton-mvn/food2go-webapp/src/test/java/com/github/hippoom/food2go/at/DeploySteps.java">see this.</a>.Some infrastructure could be set up in this step such as scm repository.  

## place-order

**Summary**: The first scenario in the first feature.  
**Stories**: <a href="https://github.com/Hippoom/food2go/blob/place-order/food2go-webapp/src/test/stories/features/place_order.feature">Place order</a>.  
**Description**: The logic is pretty simple(if there is any), just create an order and store, 
[no validations for business rule at all](https://github.com/Hippoom/food2go/blob/place-order/food2go-webapp/src/main/java/com/github/hippoom/food2go/application/impl/TransactionalPlaceOrderServiceImpl.java). 
The most important progress is a simple domain model and database being set up. 
I used to write acceptance tests via Selenium Web Driver, it can cover view logic but it is also time consuming. 
So this time I would like to try a new [approach](https://github.com/Hippoom/food2go/blob/place-order/food2go-webapp/src/test/java/com/github/hippoom/food2go/features/PlaceOrderSteps.java).

## place-order-alternative-path

**Summary**: Alternative path of place order. Available restaurants checking is introduced in this branch.  
**Stories**: <a href="https://github.com/Hippoom/food2go/blob/place-order-alternative-path/food2go-webapp/src/test/stories/features/place_order.feature">Place order</a>.  
**Description**: [An aggregate factory](https://github.com/Hippoom/food2go/blob/place-order-alternative-path/food2go-webapp/src/main/java/com/github/hippoom/food2go/domain/model/order/PendingOrderFactory.java) 
is introduced to prevent domain logic from leaking into [application layer](https://github.com/Hippoom/food2go/blob/place-order-alternative-path/food2go-webapp/src/main/java/com/github/hippoom/food2go/application/impl/TransactionalPlaceOrderServiceImpl.java).
There is an error this branch, I misunderstood how to [match](https://github.com/Hippoom/food2go/blob/place-order-alternative-path/food2go-webapp/src/main/java/com/github/hippoom/food2go/infrastructure/persistence/jpa/RestaurantRepositoryImpl.java) delivery address with service areas of restaurant,
Apparently, it's not feasible to use street for matching available restuarants in real business. 
This kind of problems often occour in read projects, so it happens to be a good example, I'll fix it in another branch later.

## a-le-carte

**Summary**: Following step of place order. Restaurant and food selections are introduced in this branch.  
**Stories**: <a href="https://github.com/Hippoom/food2go/blob/a-le-carte/food2go-webapp/src/test/stories/features/place_order.feature">Place order</a>.  

# Q&A


**Q**: What does @Getter/@Setter/@ToString/@EqualsAndHashCode do? Why I have compile error on all setter and getter methods?  
**A**: I use lombok to generate code. You need to install lombok plugin on your ide, refer to <a href="http://projectlombok.org/features/index.html">this</a> for more detail.

**Q**: How can I setup a hsqldb instance?  
**A**: There are two scenarios you need to the database.

**Scenario:acceptance test/user acceptance tests**  
   1. execute "java -cp hsqldb-2.3.0.jar org.hsqldb.server.Server --database.0 ./db/f2g --dbname.0 f2g" to setup a hsqldb instance in server mode.
   2. execute "java -cp hsqldb-2.3.0.jar org.hsqldb.util.DatabaseManagerSwing" to setup a gui tool
   3. login
   4. execute "SET DATABASE SQL SYNTAX ORA true" to allow ORACLE compatibility
   
**Scenario:persistence tests**   
   1. connect to embedded instance using "jdbc:hsqldb:mem:f2g;sql.syntax_ora=true"

**Q**: How do you write tests for persistence components?  
**A**: This is not easy to answer, refer to <a href="how-to-write-persistence-tests.md">this</a> for more detail.



# Resources
Jpa criteria query:http://www.ibm.com/developerworks/java/library/j-typesafejpa/
