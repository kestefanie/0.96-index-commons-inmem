package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

import ca.mcgill.distsys.hbase96.indexcommonsinmem.proto.Column;

public class IndexedColumn implements Serializable {
	private static final long serialVersionUID = 110709434738361949L;

	private byte[] columnFamily;
	private byte[] qualifier;

	private String indexType;
	private Object[] arguments;
	private Class<?>[] argumentsClasses;

	private boolean multiCols = false;
	private List<Column> colList;

	public IndexedColumn(byte[] columnFamily, byte[] qualifier) {
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		// set default index type and arguments
		indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
		arguments = new Object[] { columnFamily, qualifier };
	}

	public IndexedColumn(List<Column> colList) {
		this.colList = colList;
		// set multiCols to true
		this.multiCols = true;
		// set default index type and arguments
		indexType = "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex.hybridBased.HybridIndex";
		arguments = new Object[] { columnFamily, qualifier };
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

	public void setIndexType(String type) {
		indexType = type;
	}

	public void setArguments(Object[] argu) {
		arguments = argu;
	}

	public void setMultiCol(boolean isMultiCol) {
		this.multiCols = isMultiCol;
	}

	public void setArgumentsClasses(Class<?>[] argumentsClasses) {
		this.argumentsClasses = argumentsClasses;
	}

	
//	public int compareTo (IndexedColumn col) {
//		
//		String key = "";
//		String anotherKey = "";
//		if(multiCols == true) {
//			key = Bytes.toString(Util.concatColumns(this.getColumnList()));
//		} else {
//			key = Bytes.toString(Util.concatByteArray(this.getColumnFamily(), this.getQualifier()));
//		}
//		
//		if(col.getMultiColumn() == true) {
//			anotherKey = Bytes.toString(Util.concatColumns(col.getColumnList()));
//		} else {
//			anotherKey = Bytes.toString(Util.concatByteArray(col.getColumnFamily(), col.getQualifier()));
//		}
//		
//		System.out.println("Mine: " + key + "   the other: " + anotherKey);
//		
//		if(this.multiCols != col.getMultiColumn()) {
//			System.out.println("Hello I am here1");
//			return 1;
//		}
//		if(multiCols == true) {
//			System.out.println("Hello I am here2");
//			return Bytes.compareTo(Util.concatColumns(colList),Util.concatColumns(col.getColumnList()));
//		} else {
//			System.out.println("Hello I am here3");
//			return Bytes.compareTo(Util.concatByteArray(columnFamily, qualifier), Util.concatByteArray(col.getColumnFamily(), col.getQualifier()));
//		}
//	}
	
	@Override
	public boolean equals(Object obj) {
		//System.out.println("Hello world");
		if(obj == null) {
			return false;
		}
		if (obj instanceof IndexedColumn) {
			//System.out.println("Hello I am here1");
			IndexedColumn col = (IndexedColumn) obj;
			if(this.multiCols != col.getMultiColumn()) {
				return false;
			}
			if(multiCols == true) {
				//System.out.println("Hello I am here2");
				if(Bytes.compareTo(Util.concatColumns(colList),Util.concatColumns(col.getColumnList())) == 0) {
					return true;
				} else {
					return false;
				}
			} else {
				//System.out.println("Hello I am here3");
				if(Bytes.compareTo(Util.concatByteArray(columnFamily, qualifier), Util.concatByteArray(col.getColumnFamily(), col.getQualifier())) == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if(multiCols == true) {
			return Arrays.hashCode(Util.concatColumns(colList));
		} else {
			return Arrays.hashCode(Util.concatByteArray(columnFamily, qualifier));
		} 
	}
	
}
