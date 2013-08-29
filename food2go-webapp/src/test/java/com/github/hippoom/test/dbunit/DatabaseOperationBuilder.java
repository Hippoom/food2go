package com.github.hippoom.test.dbunit;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.Getter;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.CloseConnectionOperation;
import org.dbunit.operation.DatabaseOperation;

public class DatabaseOperationBuilder {
	private DataSource dataSource;

	private List<DatabaseOperationAction> operations = new ArrayList<DatabaseOperationAction>();

	public DatabaseOperationBuilder(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public IDatabaseConnection getConnection() throws DatabaseUnitException,
			SQLException {
		return new DatabaseConnection(dataSource.getConnection());
	}

	public DatabaseOperationBuilder to(final DatabaseOperation operation,
			final IDataSet dataSet) throws Exception {
		this.operations.add(new DatabaseOperationAction(operation, dataSet));
		return this;
	}

	public void execute() throws DatabaseUnitException, SQLException {
		for (DatabaseOperationAction action : operations) {
			new CloseConnectionOperation(action.getOperation()).execute(
					getConnection(), action.getDataSet());
		}
	}

	public static IDataSet flatXml(File file)
			throws MalformedURLException, DataSetException {
		ReplacementDataSet dataSet = new ReplacementDataSet(
				new FlatXmlDataSetBuilder().build(file));
		dataSet.addReplacementObject("[NULL]", null);
		return dataSet;
	}

	private class DatabaseOperationAction {
		@Getter
		private DatabaseOperation operation;
		@Getter
		private IDataSet dataSet;

		public DatabaseOperationAction(DatabaseOperation operation,
				IDataSet dataSet) {
			super();
			this.operation = operation;
			this.dataSet = dataSet;
		}
	}
}
