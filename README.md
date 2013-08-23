food2go
=======

# Branches

## deploy-skeleton-mvn


**Summary**: The first step is to develop a very small but deployable application. Some infrastructure could be set up in this step such as scm repository.  
**Stories**: <a href="https://github.com/Hippoom/food2go/blob/deploy-skeleton-mvn/food2go-webapp/src/test/stories/deploy.feature">Server startup</a>.  
**Description**: 
    This process should be quick and straightforward. Access the welcome page(the only page needed so far) to check whether the application is deployed, <a href="https://github.com/Hippoom/food2go/blob/deploy-skeleton-mvn/food2go-webapp/src/test/java/com/github/hippoom/food2go/at/DeploySteps.java">see this.</a>.  


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



# Resources
Jpa criteria query:http://www.ibm.com/developerworks/java/library/j-typesafejpa/
