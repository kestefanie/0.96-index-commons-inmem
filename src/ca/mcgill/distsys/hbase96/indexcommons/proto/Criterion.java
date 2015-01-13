package ca.mcgill.distsys.hbase96.indexcommons.proto;

import java.util.Set;

import org.apache.hadoop.hbase.filter.Filter;

public abstract class Criterion<T> {
    // Add Range(lower bound, higher bound) by Cong
	public enum CompareType {
      LESS,
      LESS_OR_EQUAL,
      EQUAL,
      NOT_EQUAL,
      GREATER_OR_EQUAL,
      GREATER,
      NO_OP,
      RANGE;
    }
    
    private CompareType compareType = CompareType.EQUAL;
    private T comparisonValue;
    private Column compareColumn;
    // added by Cong
    private Range range;

    public CompareType getComparisonType() {
        return compareType;
    }
    
    public void setComparisonType(CompareType comparisonType) {
        this.compareType = comparisonType;
    }

    public T getComparisonValue() {
        return comparisonValue;
    }
    
    // added by Cong
    public void setRange(byte [] lower, byte [] upper){
    	range = new Range(lower, upper);
    }
    
    // added by Cong
    public Range getRange() {
    	return range;
    }

    public void setComparisonValue(T comparisonValue) {
        this.comparisonValue = comparisonValue;
    }

    /**
     * Compares the value to the criterion's comparison value using the specified comparison type; <br>
     * Used for comparing column values that are not indexed once a subset has been created by using the index.
     * @param value
     * @return true if the value compares successfully to the criterion's comparison value, false otherwise.
     */
    public abstract boolean compare(byte[] value);
    
    /**
     * Returns a subset of values from a indexed column's keyset that match the criterion. <br>
     * This is used to retrieve only the rows from the region that will match this criterion.
     * @param value
     * @return true if the value compares successfully to the criterion's comparison value, false otherwise.
     */
    public abstract Set<String> getMatchingValueSetFromIndex(Set<String> idxValueSet);

    /**
     * Converts this criterion to an HBase filter
     * @return
     */
    public abstract Filter toFilter();
    
    public Column getCompareColumn() {
        return compareColumn;
    }   

    public void setCompareColumn(Column compareColumn) {
        this.compareColumn = compareColumn;
    }

    

}
