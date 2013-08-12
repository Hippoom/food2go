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

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
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

import com.github.hippoom.food2go.domain.model.order.PendingOrder;
import com.github.hippoom.food2go.domain.model.order.PendingOrderRepository;
import com.github.hippoom.food2go.domain.model.order.TrackingId;
import com.github.hippoom.food2go.test.PersistenceTests;
import com.github.hippoom.test.dbunit.DatabaseOperationBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ApplicationContextLoaderForPersistenceTests.class)
public class PendingOrderRepositoryPersistenceTests implements
		ApplicationContextAware, PersistenceTests {

	private static final TrackingId TRACKING_ID_FOR_SAVE = new TrackingId(2L);

	@Autowired
	private PendingOrderRepository repository;
	@Autowired
	private DataSource dataSource;

	private ApplicationContext applicationContext;

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

	private String testFixtureForSave() {
		return "classpath:t_f2g_pending_order_save.xml";
	}

	private IDataSet actualSaved() throws DatabaseUnitException, SQLException {
		QueryDataSet queryDataSet = new QueryDataSet(getConnection());
		queryDataSet.addTable("t_f2g_pending_order",
				"select * from t_f2g_pending_order where tracking_id="
						+ TRACKING_ID_FOR_SAVE.getValue());
		return queryDataSet;
	}

	private IDataSet expectedSaved() throws Exception {
		return flatXml(file(testFixtureForSave()));
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
