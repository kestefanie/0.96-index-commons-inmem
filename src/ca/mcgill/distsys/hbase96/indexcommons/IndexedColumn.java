package ca.mcgill.distsys.hbase96.indexcommons;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

import ca.mcgill.distsys.hbase96.indexcommons.proto.Column;

public class IndexedColumn implements Serializable {

	private static final long serialVersionUID = 110709434738361949L;

	private String indexType;
	private Object[] arguments;

	private List<Column> colList;

	public IndexedColumn(List<Column> colList) {
		this.colList = colList;
		// set default index type and arguments
		//indexType = "ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.pluggableIndex.hybridBased.HybridIndex";
		//arguments = new Object[] {columnFamily, qualifier};
	}

	public List<Column> getColumnList() {
		return this.colList;
	}

	public String getIndexType() {
		return indexType;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setIndexType(String type) {
		indexType = type;
	}

	public void setArguments(Object[] argu) {
		arguments = argu;
	}

	@Override
	public String toString() {
		return Util.concatColumnsToString(colList);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if (obj instanceof IndexedColumn) {
			if(toString().equals(((IndexedColumn) obj).toString())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(Bytes.toBytes(toString()));
	}

}
