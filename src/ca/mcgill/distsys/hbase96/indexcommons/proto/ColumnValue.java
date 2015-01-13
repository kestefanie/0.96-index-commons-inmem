package ca.mcgill.distsys.hbase96.indexcommons.proto;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

public class ColumnValue extends Column {

	private static final long serialVersionUID = -8348637705219065047L;

	private byte[] value;
	
	public ColumnValue(byte [] family, byte[] qualifier, byte[] value) {
		super(family, qualifier);
    this.value = value;
	}

  public ColumnValue(Column column) {
    this(column.getFamily(), column.getQualifier(), null);
  }

  public ColumnValue(KeyValue kv) {
    this(kv.getFamily(), kv.getQualifier(), kv.getValue());
  }

	public void setValue(byte [] value) {
		this.value = value;
	}
	
	public byte[] getValue() {
		return value;
	}

  public String toStringFull() {
    return this.toString() + "=" + Bytes.toString(this.value);
  }
}
