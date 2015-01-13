package ca.mcgill.distsys.hbase96.indexcommons;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;

public class SecondaryIndexConstants {
    /**
     * Name of the master index table that contains which columns are indexed for which tables.
     */
    public static final String MASTER_INDEX_TABLE_NAME = "__sys__indextable";
    
    /**
     * Within the master index table:
     * Name of the column family hosting the list of columns indexed for a specific table(row)
     */
    public static final String MASTER_INDEX_TABLE_IDXCOLS_CF_NAME = "idxcol";
    
    public static final String PRIMARYKEY_TREE_MAX_SIZE = "indexCoprocessor.primaryKeyTree.maxSize";
    
    public static final int PRIMARYKEY_TREE_MAX_SIZE_DEFAULT = Integer.MAX_VALUE;

    public static final String HTABLE_INDEX = "HTableIndex";

    public static final String PLUGGABLE_INDEX_NAMESPACE =
        "ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.pluggableIndex";

    public static final String HASHTABLE_INDEX =
        PLUGGABLE_INDEX_NAMESPACE + ".hashtableBased.RegionColumnIndex";

    public static final String HYBRID_INDEX =
        PLUGGABLE_INDEX_NAMESPACE + ".hybridBased.HybridIndex";

    public static final String DEFAULT_INDEX = HYBRID_INDEX;

    // Yousuf
    // Configuration
    public static final Configuration CONF;
    static { CONF = HBaseConfiguration.create(); }

    public static boolean SORT_INDEXED_QUERY_RESULTS;
    static {
      SORT_INDEXED_QUERY_RESULTS = CONF.getBoolean("hbase.client.index.scanner.sort", true);
    }
    //

  /**
   * Sufffix that all column index tables have
   */
  public static final String INDEX_TABLE_SUFFIX = "__idx";

  /**
   * Within each column index table, name of the column family where the column containing primary key values are held.
   */
  public static final String INDEX_TABLE_IDX_CF_NAME = "idx";

  /**
   * Within each column index table, idx column family, column name where the primary key values are held.
   */
  public static final String INDEX_TABLE_IDX_C_NAME = "pr";

  public static final String CONF_IDX_TABLENAME = "conf.tablenametoindex";
  public static final String CONF_IDX_COLFAM = "conf.colfamtoindex";
  public static final String CONF_IDX_COLQUAL = "conf.colqualtoindex";

  public static final byte[] INDEX_TABLE_IDX_PUT_TO_IDX =
      Bytes.toBytes("type_-00001-_put");
  public static final byte[] INDEX_TABLE_IDX_DEL_FROM_IDX =
      Bytes.toBytes("type_-00002-_del");
}
