How to write persistence tests
=======

Q: What exactly do you test?  
A: Most projects use orm frameworks for persisting. The frameworks' quality are well proved, so we don't need to test them. But we need to test if we config/use them correctly. And I strongly recommend develop your persistent components by TDD since debugging the or mapping will kill your time relentlessly.

Q: What is the prerequisite?  
A: Not really. A handy test framework is needed of course(I use junit). An embedded-supported database is prefered can helps keep your test suite fast and save your budget(My organization uses oracle in uat, production environment, but we can't afford per-oracle-instance-per-developer in development environment. so we use hsqldb instead which support most oracle syntax). And it will make this job easier if you don't mix business log in data access operations.

Q: Any Hello world?  
A: Well...

<pre>
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context-infrastructure-persistence.xml" }) (1)
public class OrderRepositoryPersistenceTests {
    @Autowired
    private OrderRepository repository; (2)

    @Test
    public void saves() throws Throwable {

        final TrackingId trackingId = TrackingId.valueOf("1");
        final Order expect = new Order(trackingId);

        repository.store(expect); (3)

        final Order actual = repository.findBy(trackingId); (4)
        assertThat(persisted.trackingId(), equalTo(trackingId); (5)
    }
}
</pre>

This exmaple is pretty straightforward. I use the same configuration in production code at step (1) to bootstrap an ioc container, this step check if everything is configured(datasource, transaction manager etc). Retrieve the repository (infrastructure agnositic) from the container at step (2). Save an order at step (3) and check if the orm does the right thing for me at step (4) and (5).

