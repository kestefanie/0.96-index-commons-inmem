package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

import java.util.ArrayList;
import java.util.List;

public class IndexedColumnQuery {
    private List<Criterion<?>> criteriaList;
    private boolean mustPassAllCriteria;
    private List<Column> columnList;
    
    public IndexedColumnQuery() {
        criteriaList = new ArrayList<Criterion<?>>();
        columnList = new ArrayList<Column>();
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
}
