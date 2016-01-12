package ca.mcgill.distsys.hbase96.indexcommons;

import java.util.Comparator;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes.ByteArrayComparator;

public class ResultComparator implements Comparator<Result> {

    public int compare(Result leftResult, Result rightResult) {       
        ByteArrayComparator byteArrayComparator = new ByteArrayComparator();
        return byteArrayComparator.compare(leftResult.getRow(), rightResult.getRow());
    }

}
