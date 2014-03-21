package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ByteArrayCriterion extends Criterion<byte[]> {

    public ByteArrayCriterion(byte[] criterionValue) {
        setComparisonValue(criterionValue);
    }

    @Override
    public boolean compare(byte[] value) {
        switch (getComparisonType()) {
        case EQUAL:
            if (Arrays.equals(value, getComparisonValue())) {
            	System.out.println("Just For Test:   " + Bytes.toString(value) +
                  "  " + Bytes.toString(getComparisonValue()));
                return true;
            }
            return false;
        default:
            return false;
        }
    }

    @Override
    public Set<String> getMatchingValueSetFromIndex(Set<String> idxValueSet) {
        Set<String> result = new HashSet<String>();
        switch (getComparisonType()) {
        case EQUAL:
            if (idxValueSet.contains(Bytes.toString(getComparisonValue()))) {
                result.add(Bytes.toString(getComparisonValue()));
            }
        default:
            return result;
        }
    }

    @Override
    public Filter toFilter() {
        Filter filter;

        switch (getComparisonType()) {
        case EQUAL:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.EQUAL,
                    getComparisonValue());
            break;
        case GREATER:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.GREATER,
                    getComparisonValue());
            break;
        case GREATER_OR_EQUAL:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.GREATER_OR_EQUAL,
                    getComparisonValue());
            break;
        case LESS:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.LESS, getComparisonValue());
            break;
        case LESS_OR_EQUAL:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.LESS_OR_EQUAL,
                    getComparisonValue());
            break;
        case NOT_EQUAL:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.NOT_EQUAL,
                    getComparisonValue());
            break;
        default:
            filter = new SingleColumnValueFilter(getCompareColumn().getFamily(), getCompareColumn().getFamily(), CompareOp.NO_OP,
                    getComparisonValue());
            break;
        }
        return filter;
    }

}
