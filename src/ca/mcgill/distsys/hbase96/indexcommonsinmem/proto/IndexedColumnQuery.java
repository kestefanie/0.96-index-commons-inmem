package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexedColumnQuery {

	private List<Criterion<?>> criteriaList;
	private List<Column> columnList;
	private boolean mustPassAllCriteria = true;
	//private boolean isMultiColumn = false;

	public IndexedColumnQuery() {
		criteriaList = new ArrayList<Criterion<?>>();
		columnList = new ArrayList<Column>();
	}

	public IndexedColumnQuery(List<Criterion<?>> selectCriteria) {
		criteriaList = selectCriteria;
		columnList = new ArrayList<Column>();
	}

	public IndexedColumnQuery(List<Criterion<?>> selectCriteria,
							  List<Column> projectColumns) {
		criteriaList = selectCriteria;
		columnList = projectColumns;
	}

	public IndexedColumnQuery(Criterion<?> selectCriterion) {
		this();
		criteriaList.add(selectCriterion);
	}

	public List<Criterion<?>> getCriteria() {
		return criteriaList;
	}

	public void addCriterion(Criterion<?> criterion) {
		criteriaList.add(criterion);
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void addColumn(Column column) {
		columnList.add(column);
	}

	public void setMustPassAllCriteria(boolean value) {
		mustPassAllCriteria = value;
	}

	public boolean isMustPassAllCriteria() {
		return mustPassAllCriteria;
	}

	/*
	public void setMultiColumn(boolean isMultiCol) {
		isMultiColumn = isMultiCol;
	}

	public boolean isMultiColumn() {
		return isMultiColumn;
	}
	*/
}
