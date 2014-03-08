package ca.mcgill.distsys.hbase96.indexcommonsinmem.proto;


public class Column {
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
}
