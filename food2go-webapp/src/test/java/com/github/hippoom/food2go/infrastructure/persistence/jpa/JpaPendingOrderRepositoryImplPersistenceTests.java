package com.github.hippoom.food2go.infrastructure.persistence.jpa;

import static org.dbunit.Assertion.assertEquals;
import static org.dbunit.operation.DatabaseOperation.DELETE;
import static org.dbunit.operation.DatabaseOperation.REFRESH;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.CloseConnectionOperation;
import org.dbunit.operation.DatabaseOperation;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context-infrastructure-persistence.xml" })
public class JpaPendingOrderRepositoryImplPersistenceTests implements
		ApplicationContextAware, PersistenceTests {

	private static final TrackingId TRACKING_ID_FOR_SAVE = new TrackingId(2L);

	@Autowired
	private PendingOrderRepository repository;
	@Autowired
	private DataSource dataSource;

	private ApplicationContext applicationContext;

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
		return flatXmlDataSet(file(testFixtureForSave()));
	}

	private PendingOrder copyFrom(PendingOrder protoype) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(protoype, PendingOrder.class);
	}

	private PendingOrder protoypeForSave() {
		return repository.findBy(TRACKING_ID_FOR_SAVE);
	}

	private void refresh(String file) throws Exception {
		execute(REFRESH, file);
	}

	private void execute(DatabaseOperation refresh, String file)
			throws DatabaseUnitException, SQLException, MalformedURLException,
			DataSetException, IOException {
		new CloseConnectionOperation(refresh).execute(getConnection(),
				flatXmlDataSet(file(file)));
	}

	private File file(String file) throws IOException {
		return applicationContext.getResource(file).getFile();
	}

	private FlatXmlDataSet flatXmlDataSet(File file)
			throws MalformedURLException, DataSetException {
		return new FlatXmlDataSetBuilder().build(file);
	}

	private void delete(String file) throws Exception {
		execute(DELETE, file);
	}

	private IDatabaseConnection getConnection() throws DatabaseUnitException,
			SQLException {
		return new DatabaseConnection(dataSource.getConnection());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
