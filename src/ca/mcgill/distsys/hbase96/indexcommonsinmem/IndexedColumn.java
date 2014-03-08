package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import java.io.Serializable;

public class IndexedColumn implements Serializable {
    private static final long serialVersionUID = 110709434738361949L;

    private byte[] columnFamily;
    private byte[] qualifier;
    
    public IndexedColumn(byte[] columnFamily, byte[] qualifier) {
        this.columnFamily = columnFamily;
        this.qualifier = qualifier;
    }

    public byte[] getColumnFamily() {
        return columnFamily;
    }

    public byte[] getQualifier() {
        return qualifier;
    }
}
