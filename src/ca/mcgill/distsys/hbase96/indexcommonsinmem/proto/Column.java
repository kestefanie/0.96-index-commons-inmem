package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

import org.apache.hadoop.hbase.util.Bytes;

import ca.mcgill.distsys.hbase96.indexcommonsinmem.Util;

public class Column implements Comparable<Column> {
	private byte[] family;
	private byte[] qualifier;

	public Column(byte[] family) {
		this.family = family;
		this.qualifier = null;
	}

	public byte[] getFamily() {
		return family;
	}

	public byte[] getQualifier() {
		return qualifier;
	}

	public Column setQualifier(byte[] qualifier) {
		this.qualifier = qualifier;
		return this;
	}

	public byte[] getConcatByteArray() {
		if(qualifier == null){
			return Util.concatByteArray(this.family, Bytes.toBytes(":"));
					
		} else {
			return Util.concatByteArray(
					Util.concatByteArray(this.family, Bytes.toBytes(":")),
					this.qualifier);
		}
		
	}

	@Override
	public int compareTo(Column o) {
		return Bytes.compareTo(this.getConcatByteArray(),
				o.getConcatByteArray());
	}

}
