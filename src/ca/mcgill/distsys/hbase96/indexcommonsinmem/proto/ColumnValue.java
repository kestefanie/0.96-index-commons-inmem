package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;

public class ColumnValue extends Column{

	private static final long serialVersionUID = -8348637705219065047L;

	private byte[] value;
	
	public ColumnValue(byte [] family) {
		super(family);
	}
	
	public void setColumnValue(byte [] value) {
		this.value = value;
	}
	
	public byte[] getColumnValue() {
		return value;
	}

}
