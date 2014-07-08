package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import java.io.Serializable;
import java.util.List;

import ca.mcgill.distsys.hbase96.indexcommonsinmem.proto.Column;

public class IndexedColumn implements Serializable {

	private static final long serialVersionUID = 110709434738361949L;

	private String indexType;
	private Object[] arguments;

	private List<Column> colList;

	public IndexedColumn(List<Column> colList) {
		this.colList = colList;
		// set default index type and arguments
		//indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
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
}
