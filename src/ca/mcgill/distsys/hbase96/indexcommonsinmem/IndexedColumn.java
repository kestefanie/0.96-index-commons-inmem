package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import java.io.Serializable;

public class IndexedColumn implements Serializable {
	private static final long serialVersionUID = 110709434738361949L;

	private byte[] columnFamily;
	private byte[] qualifier;

	private String indexType;
	private Object[] arguments;

	public IndexedColumn(byte[] columnFamily, byte[] qualifier) {
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		// set default index type and arguments
		indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
		arguments = new Object[] {columnFamily, qualifier};
	}

	public byte[] getColumnFamily() {
		return columnFamily;
	}

	public byte[] getQualifier() {
		return qualifier;
	}
	
	public String getIndexType() {
		return indexType;
	}
	
	public Object[] getArguments() {
		return arguments;
	}
	
	public void setIndexType(String type){
		indexType = type;
	}
	
	public void setArguments(Object[] argu) {
		arguments = argu;
	}
}
