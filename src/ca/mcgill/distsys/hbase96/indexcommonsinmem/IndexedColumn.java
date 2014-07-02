package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import java.io.Serializable;
import java.util.List;

import ca.mcgill.distsys.hbase96.indexcommonsinmem.proto.Column;

public class IndexedColumn implements Serializable {
	private static final long serialVersionUID = 110709434738361949L;

	private byte[] columnFamily;
	private byte[] qualifier;

	private String indexType;
	private Object[] arguments;
	private Class<?> [] argumentsClasses;
	
	private boolean multiCols;
	private List<Column> colList;

	public IndexedColumn(byte[] columnFamily, byte[] qualifier) {
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		// set default index type and arguments
		indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
		arguments = new Object[] {columnFamily, qualifier};
	}
	
	public IndexedColumn(List<Column> colList) {
		this.colList = colList;
		// set multiCols to true
		this.multiCols = true;
		// set default index type and arguments
		indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
		arguments = new Object[] {columnFamily, qualifier};
	}
	
	public List<Column> getColumnList() {
		
		return this.colList;
				
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
	
	public boolean getMultiColumn() {
		return this.multiCols;
	}
	
	public Class<?>[] getArgumentsClasses() {
		return this.argumentsClasses;
	}
	
	public void setIndexType(String type){
		indexType = type;
	}
	
	public void setArguments(Object[] argu) {
		arguments = argu;
	}
	
	public void setMultiCol(boolean isMultiCol){
		this.multiCols = isMultiCol;
	}
	
	public void setArgumentsClasses(Class<?>[] argumentsClasses) {
		this.argumentsClasses = argumentsClasses;
	}
}
