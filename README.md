food2go
=======

**Q&A**

<pre>

Q: What does @Getter/@Setter/@ToString/@EqualsAndHashCode do? Why I have compile error on all setter and getter methods?
A: I use lombok to generate code. You need to install lombok plugin to your ide, refer to [this](http://projectlombok.org/features/index.html) for more detail.

Q: How can I setup a hsqldb instance?
A: Scenario:acceptance test/user acceptance tests
   1.execute "java -cp hsqldb-2.3.0.jar org.hsqldb.server.Server --database.0 ./db/f2g --dbname.0 f2g" to setup a hsqldb instance in server mode.
   2.execute "java -cp hsqldb-2.3.0.jar org.hsqldb.util.DatabaseManagerSwing" to setup a gui tool
   3.login
   4.execute "SET DATABASE SQL SYNTAX ORA true" to allow ORACLE compatibility
   
   Scenario:persistence tests
   1.connect to embedded instance using "jdbc:hsqldb:mem:f2g;sql.syntax_ora=true"

</pre>

