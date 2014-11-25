package ca.mcgill.distsys.hbase96.indexcommonsinmem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

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
    
    public static final String PLUGGABLE_INDEX_NAMESPACE =
        "ca.mcgill.distsys.hbase96.indexcoprocessorsinmem.pluggableIndex";

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
}
