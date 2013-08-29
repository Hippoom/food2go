package com.github.hippoom.food2go.infrastructure.persistence;

import static com.github.hippoom.test.dbunit.DatabaseOperationBuilder.flatXml;
import static org.dbunit.Assertion.assertEquals;
import static org.dbunit.operation.DatabaseOperation.DELETE;
import static org.dbunit.operation.DatabaseOperation.REFRESH;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.hippoom.food2go.domain.model.order.OrderLine;
import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantIdentity;
import com.github.hippoom.food2go.domain.model.restaurant.RestaurantRepository;
import com.github.hippoom.food2go.test.PersistenceTests;
import com.github.hippoom.test.dbunit.DatabaseOperationBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ApplicationContextLoaderForPersistenceTests.class)
public class PendingOrderRepositoryPersistenceTests implements
		ApplicationContextAware, PersistenceTests {

	private static final TrackingId TRACKING_ID_FOR_SAVE = new TrackingId(2L);
	private static final TrackingId TRACKING_ID_FOR_UPDATE = new TrackingId(3L);
	private static final TrackingId PROTOTYPE_FOR_UPDATE = new TrackingId(4L);
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private PendingOrderRepository repository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private DataSource dataSource;

	private ApplicationContext applicationContext;
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Test
	public void getsNextTrackingIdFromSequence() {
		TrackingId trackingId1 = repository.nextTrackingId();
		TrackingId trackingId2 = repository.nextTrackingId();

		assertThat(trackingId2.getValue(), equalTo(trackingId1.getValue() + 1));
	}

	@Test
	public void saves() throws Exception {
		refresh(testFixtureForSave());

		final PendingOrder toBeSaved = copyFrom(protoypeForSave());

		delete(testFixtureForSave());

		repository.store(toBeSaved);

		assertEquals(expectedSaved(), actualSaved());
	}

	@Test
	public void updates() throws Exception {
		refresh(testFixtureForUpdate());
		//use transaction to enable lazy loading
		new TransactionTemplate(transactionManager)
				.execute(new TransactionCallback<PendingOrder>() {

					public PendingOrder doInTransaction(TransactionStatus status) {
						final PendingOrder prototype = repository
								.findOne(PROTOTYPE_FOR_UPDATE);
						final PendingOrder toBeUpdated = repository
								.findOne(TRACKING_ID_FOR_UPDATE);
						final RestaurantIdentity restaurantId = prototype
								.getRestaurantIdentity();

						toBeUpdated.update(
								restaurantRepository.findOne(restaurantId),
								copy(prototype.getOrderLines()));
						repository.store(toBeUpdated);
						return toBeUpdated;
					}
				});

		assertEquals(expectedUpdated(), actualUpdated());
	}

	private List<OrderLine> copy(List<OrderLine> orderLines) {
		List<OrderLine> copy = new ArrayList<OrderLine>();
		for (OrderLine ol : orderLines) {
			copy.add(ol.copy());
		}
		return copy;
	}

	private String testFixtureForSave() {
		return "classpath:t_f2g_pending_order_save.xml";
	}

	private String testFixtureForUpdate() {
		return "classpath:t_f2g_pending_order_update.xml";
	}

	private String testExpectedForUpdate() {
		return "classpath:t_f2g_pending_order_update_after.xml";
	}

	private IDataSet actualSaved() throws DatabaseUnitException, SQLException {
		return actual(TRACKING_ID_FOR_SAVE);
	}

	private IDataSet actualUpdated() throws DatabaseUnitException, SQLException {
		QueryDataSet actual = actual(TRACKING_ID_FOR_UPDATE);
		actual.addTable("t_f2g_order_line",
				"select * from t_f2g_order_line where tracking_id="
						+ TRACKING_ID_FOR_UPDATE.getValue() + " order by name");

		return actual;
	}

	private QueryDataSet actual(TrackingId trackingId)
			throws DatabaseUnitException, SQLException {
		QueryDataSet queryDataSet = new QueryDataSet(getConnection());
		queryDataSet.addTable("t_f2g_pending_order",
				"select * from t_f2g_pending_order where tracking_id="
						+ trackingId.getValue());
		return queryDataSet;
	}

	private IDataSet expectedSaved() throws Exception {
		return flatXml(file(testFixtureForSave()));
	}

	private IDataSet expectedUpdated() throws Exception {
		return flatXml(file(testExpectedForUpdate()));
	}

	private PendingOrder copyFrom(PendingOrder protoype) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(protoype, PendingOrder.class);
	}

	private PendingOrder protoypeForSave() {
		return repository.findOne(TRACKING_ID_FOR_SAVE);
	}

	private void refresh(String file) throws Exception {
		new DatabaseOperationBuilder(dataSource).to(REFRESH,
				flatXml(file(file))).execute();
	}

	private File file(String file) throws IOException {
		return applicationContext.getResource(file).getFile();
	}

	private void delete(String file) throws Exception {
		new DatabaseOperationBuilder(dataSource)
				.to(DELETE, flatXml(file(file))).execute();
	}

	private IDatabaseConnection getConnection() throws DatabaseUnitException,
			SQLException {
		return new DatabaseOperationBuilder(dataSource).getConnection();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
