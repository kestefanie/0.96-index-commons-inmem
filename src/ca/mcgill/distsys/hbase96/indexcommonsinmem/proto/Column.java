package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.hadoop.hbase.util.Bytes;

import ca.mcgill.distsys.hbase96.indexcommonsinmem.Util;

public class Column implements Comparable<Column>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -83906511023166410L;
	/**
	 *
	 */

	private byte[] family;
	private byte[] qualifier;

	public Column(byte[] family) {
		this.family = family;
		this.qualifier = null;
	}

	public Column(String family) {
		this.family = Bytes.toBytes(family);
		this.qualifier = null;
	}

	public Column(byte[] family, byte[] qualifier) {
		this.family = family;
		this.qualifier = qualifier;
	}

	public Column(String family, String qualifier) {
		this.family = Bytes.toBytes(family);
		this.qualifier = Bytes.toBytes(qualifier);
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
		if (qualifier == null) {
			return Util.concatByteArray(this.family, Bytes.toBytes(":"));

		} else {
			return Util.concatByteArray(
					Util.concatByteArray(this.family, Bytes.toBytes(":")),
					this.qualifier);
		}
	}

	@Override
	public int compareTo(Column o) {
		return Bytes.compareTo(this.getConcatByteArray(), o.getConcatByteArray());
	}

	@Override
	public String toString() {
		String string = Bytes.toString(family) + ":";
		if (qualifier != null) {
			string = string + Bytes.toString(qualifier);
		}
		return string;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if (obj instanceof Column) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(Bytes.toBytes(toString()));
	}
}
