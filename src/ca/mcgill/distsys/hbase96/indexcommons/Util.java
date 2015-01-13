package ca.mcgill.distsys.hbase96.indexcommons;

import ca.mcgill.distsys.hbase96.indexcommons.exceptions.InvalidCriterionException;
import ca.mcgill.distsys.hbase96.indexcommons.exceptions.InvalidQueryException;
import ca.mcgill.distsys.hbase96.indexcommons.proto.ByteArrayCriterion;
import ca.mcgill.distsys.hbase96.indexcommons.proto.Column;
import ca.mcgill.distsys.hbase96.indexcommons.proto.Criterion;
import ca.mcgill.distsys.hbase96.indexcommons.proto.Criterion.CompareType;
import ca.mcgill.distsys.hbase96.indexcommons.proto.IndexedColumnQuery;
import ca.mcgill.distsys.hbase96.indexcommons.proto.Range;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.IndexCoprocessorCreateRequest;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.IndexCoprocessorDeleteRequest;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.IndexedQueryRequest;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoByteArrayCriterion;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoColumn;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoCompareType;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoCriteriaList;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoCriteriaList.ProtoOperator;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoKeyValue;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoRange;
import ca.mcgill.distsys.hbase96.indexcoprocessors.inmem.protobuf.generated.IndexCoprocessorInMem.ProtoResult;
import com.google.protobuf.ByteString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class Util {

  private static final Log LOG = LogFactory.getLog(Util.class);


  public static void main(String[] args) {

    List<Column> colList = Util.buildColumnList("cf:;cf:b;cf:c;");
    System.out.println(colList.toString());
  }


  // Added by Cong
  // Serialize object to byte array
  public static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    ObjectOutputStream o = new ObjectOutputStream(b);
    o.writeObject(obj);
    byte[] bytes = b.toByteArray();
    o.close();
    b.close();
    return bytes;
  }

  // Added by Cong
  // Deserialize byte array to object
  public static Object deserialize(byte[] bytes)
  throws IOException, ClassNotFoundException {
    ByteArrayInputStream b = new ByteArrayInputStream(bytes);
    ObjectInputStream o = new ObjectInputStream(b);
    Object a = o.readObject();
    o.close();
    b.close();
    return a;
  }


  public static byte[] concatByteArray(byte[] array1, byte[] array2) {

    // check if either of the array is null or not
    if (array1 == null && array2 == null) {
      return null;
    } else {
      if (array1 == null) {
        byte[] result = new byte[array2.length];
        System.arraycopy(array2, 0, result, 0, array2.length);
        return result;
      } else {
        if (array2 == null) {
          byte[] result = new byte[array1.length];
          System.arraycopy(array1, 0, result, 0, array1.length);
          return result;
        }
      }
    }

    // Both array are not null
    byte[] result = new byte[array1.length + array2.length];
    int position = 0;

    System.arraycopy(array1, 0, result, position, array1.length);
    position += array1.length;

    System.arraycopy(array2, 0, result, position, array2.length);

    return result;
  }

  public static Result toResult(final ProtoResult protoResult) {
    List<ProtoKeyValue> values = protoResult.getKeyValueList();
    List<KeyValue> keyValues = new ArrayList<KeyValue>(values.size());
    for (ProtoKeyValue kv : values) {
      keyValues.add(toKeyValue(kv));
    }
    return new Result(keyValues);
  }

  public static KeyValue toKeyValue(final ProtoKeyValue kv) {
    return new KeyValue(kv.getRow().toByteArray(), kv.getFamily().toByteArray(),
        kv.getQualifier().toByteArray(), kv.getTimestamp(),
        KeyValue.Type.codeToType((byte) kv.getKeyType().getNumber()),
        kv.getValue().toByteArray());
  }

  public static ProtoResult toResult(final Result result) {
    ProtoResult.Builder builder = ProtoResult.newBuilder();
    Cell[] cells = result.raw();
    if (cells != null) {
      for (Cell c : cells) {
        builder.addKeyValue(toKeyValue(c));
      }
    }
    return builder.build();
  }

  public static List<ProtoResult> toProtoResults(final List<Result> results) {
    List<ProtoResult> protoResultList =
        new ArrayList<ProtoResult>(results.size());

    for (Result result : results) {
      protoResultList.add(toResult(result));
    }

    return protoResultList;
  }

  public static List<Result> toResults(final List<ProtoResult> protoResults) {
    List<Result> resultList = new ArrayList<Result>(protoResults.size());

    for (ProtoResult protoResult : protoResults) {
      resultList.add(toResult(protoResult));
    }

    return resultList;
  }

  public static ProtoKeyValue toKeyValue(final Cell kv) {
    ProtoKeyValue.Builder kvbuilder = ProtoKeyValue.newBuilder();
    kvbuilder.setRow(ByteString
        .copyFrom(kv.getRowArray(), kv.getRowOffset(), kv.getRowLength()));
    kvbuilder.setFamily(ByteString
        .copyFrom(kv.getFamilyArray(), kv.getFamilyOffset(),
            kv.getFamilyLength()));
    kvbuilder.setQualifier(ByteString
        .copyFrom(kv.getQualifierArray(), kv.getQualifierOffset(),
            kv.getQualifierLength()));
    kvbuilder.setTimestamp(kv.getTimestamp());
    kvbuilder.setValue(ByteString
        .copyFrom(kv.getValueArray(), kv.getValueOffset(),
            kv.getValueLength()));
    return kvbuilder.build();
  }

  public static IndexedQueryRequest buildQuery(IndexedColumnQuery query)
      throws InvalidQueryException {
    IndexedQueryRequest.Builder requestBuilder =
        IndexedQueryRequest.newBuilder();
    ProtoCriteriaList.Builder criteriaListBuilder =
        ProtoCriteriaList.newBuilder();

    for (Column queryCol : query.getColumnList()) {
      ProtoColumn.Builder columnBuilder = ProtoColumn.newBuilder();

      if (queryCol.getFamily() == null) {
        throw new InvalidQueryException(
            "Invalid Column in the query, a column MUST have a family.");
      }

      columnBuilder.setFamily(ByteString.copyFrom(queryCol.getFamily()));
      if (queryCol.getQualifier() != null) {
        columnBuilder
            .setQualifier(ByteString.copyFrom(queryCol.getQualifier()));
      }
      requestBuilder.addColumn(columnBuilder.build());
    }

    if (query.isMustPassAllCriteria()) {
      criteriaListBuilder.setOperator(ProtoOperator.MUST_PASS_ALL);
    } else {
      criteriaListBuilder.setOperator(ProtoOperator.MUST_PASS_ONE);
    }

    marshalCriteria(criteriaListBuilder, query.getCriteria());

    requestBuilder.setCriteriaList(criteriaListBuilder.build());

    // added by Cong
    //requestBuilder.setIsMultiColumns(query.isMultiColumn());

    return requestBuilder.build();
  }

  private static void marshalCriteria(
      ProtoCriteriaList.Builder criteriaListBuilder,
      List<Criterion<?>> queryCriteriaList)
      throws InvalidCriterionException {

    if (queryCriteriaList == null || queryCriteriaList.isEmpty()) {
      throw new InvalidCriterionException(
          "An indexed query must contain at least one comparison criterion to run on an indexed column.");
    }
    for (Criterion<?> queryCriterion : queryCriteriaList) {
      if (queryCriterion.getCompareColumn() == null || queryCriterion
          .getCompareColumn().getQualifier() == null
          || queryCriterion.getCompareColumn().getFamily() == null) {
        throw new InvalidCriterionException(
            "A criterion's must have a non null column comprised of a non null family and non null qualifier.");
      }

      ProtoColumn.Builder columnBuilder = ProtoColumn.newBuilder();
      columnBuilder.setFamily(
          ByteString.copyFrom(queryCriterion.getCompareColumn().getFamily()));
      columnBuilder.setQualifier(ByteString
          .copyFrom(queryCriterion.getCompareColumn().getQualifier()));

      if (queryCriterion instanceof ByteArrayCriterion) {
        ProtoByteArrayCriterion.Builder protoByteArrayCriterionBuilder =
            ProtoByteArrayCriterion.newBuilder();
        protoByteArrayCriterionBuilder.setCompareToValue(
            ByteString.copyFrom((byte[]) queryCriterion.getComparisonValue()));
        protoByteArrayCriterionBuilder.setCompareColumn(columnBuilder.build());
        ProtoCompareType compareType = getProtoCompareOpFromComparisonType(
            queryCriterion.getComparisonType());
        protoByteArrayCriterionBuilder.setCompareOp(compareType);
        // Added by Cong
        if (compareType == ProtoCompareType.RANGE) {

          ProtoRange.Builder rangeBuilder = ProtoRange.newBuilder();
          Range range = queryCriterion.getRange();
          rangeBuilder
              .setLowerBound(ByteString.copyFrom(range.getLowerBound()));
          rangeBuilder
              .setHigherBound(ByteString.copyFrom(range.getHigherBound()));
          protoByteArrayCriterionBuilder.setRange(rangeBuilder.build());
        }
        criteriaListBuilder
            .addByteArrayCriteria(protoByteArrayCriterionBuilder.build());
      } else {
        throw new InvalidCriterionException(
            "Unknown criterion type: " + queryCriterion.getClass().getName());
      }
    }

  }

  private static ProtoCompareType getProtoCompareOpFromComparisonType(
      CompareType comparisonType) {
    switch (comparisonType) {
      case EQUAL:
        return ProtoCompareType.EQUAL;
      case GREATER:
        return ProtoCompareType.GREATER;
      case GREATER_OR_EQUAL:
        return ProtoCompareType.GREATER_OR_EQUAL;
      case LESS:
        return ProtoCompareType.LESS;
      case LESS_OR_EQUAL:
        return ProtoCompareType.LESS_OR_EQUAL;
      case NOT_EQUAL:
        return ProtoCompareType.NOT_EQUAL;
      // Added by Cong
      case RANGE:
        return ProtoCompareType.RANGE;
      default:
        return ProtoCompareType.NO_OP;
    }
  }

  public static IndexedColumnQuery buildQuery(
      IndexedQueryRequest indexedQueryRequest) {
    IndexedColumnQuery query = new IndexedColumnQuery();

    if (indexedQueryRequest.getCriteriaList()
        .getOperator() == ProtoOperator.MUST_PASS_ALL) {
      query.setMustPassAllCriteria(true);
    } else {
      query.setMustPassAllCriteria(false);
    }

    for (ProtoColumn requestCol : indexedQueryRequest.getColumnList()) {
      Column queryColumn = new Column(requestCol.getFamily().toByteArray());
      if (requestCol.hasQualifier()) {
        queryColumn.setQualifier(requestCol.getQualifier().toByteArray());
      }
      query.addColumn(queryColumn);
    }

    for (ProtoByteArrayCriterion requestBACriterion : indexedQueryRequest
        .getCriteriaList().getByteArrayCriteriaList()) {
      ByteArrayCriterion queryBACriterion = new ByteArrayCriterion(
          requestBACriterion.getCompareToValue().toByteArray());

      initCriterionFromRequest(requestBACriterion, queryBACriterion);
      query.addCriterion(queryBACriterion);
    }

    // added by Cong
    //query.setMultiColumn(indexedQueryRequest.getIsMultiColumns());

        /* TODO: Add processing for other criterion types */

    return query;
  }

  private static void initCriterionFromRequest(Object requestCriterion,
      Criterion<?> queryCriterion) {
    ProtoCompareType requestCompareType;
    ProtoColumn requestColumn;
    try {
      Method getCompareOp =
          requestCriterion.getClass().getMethod("getCompareOp");
      requestCompareType =
          (ProtoCompareType) getCompareOp.invoke(requestCriterion);
      Method getCompareColumn =
          requestCriterion.getClass().getMethod("getCompareColumn");
      requestColumn = (ProtoColumn) getCompareColumn.invoke(requestCriterion);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    LOG.debug("Util.class: " + requestCompareType);

    switch (requestCompareType) {
      case EQUAL:
        queryCriterion.setComparisonType(CompareType.EQUAL);
        break;
      case GREATER:
        queryCriterion.setComparisonType(CompareType.GREATER);
        break;
      case GREATER_OR_EQUAL:
        queryCriterion.setComparisonType(CompareType.GREATER_OR_EQUAL);
        break;
      case LESS:
        queryCriterion.setComparisonType(CompareType.LESS);
        break;
      case LESS_OR_EQUAL:
        queryCriterion.setComparisonType(CompareType.LESS_OR_EQUAL);
        break;
      case NOT_EQUAL:
        queryCriterion.setComparisonType(CompareType.NOT_EQUAL);
        break;
      // Added by Cong
      case RANGE:
        queryCriterion.setComparisonType(CompareType.RANGE);
        queryCriterion.setRange(
            ((ProtoByteArrayCriterion) requestCriterion).getRange()
                .getLowerBound().toByteArray(),
            ((ProtoByteArrayCriterion) requestCriterion).getRange()
                .getHigherBound().toByteArray());
        LOG.debug("Util.class: lowerBound " + Bytes
            .toString(queryCriterion.getRange().getLowerBound()));
        LOG.debug("Util.class: higherBound " + Bytes
            .toString(queryCriterion.getRange().getHigherBound()));
        break;
      default:
        queryCriterion.setComparisonType(CompareType.NO_OP);
        break;
    }

    Column criterionColumn = new Column(
        requestColumn.getFamily().toByteArray(),
        requestColumn.getQualifier().toByteArray());
    queryCriterion.setCompareColumn(criterionColumn);

  }

  // Concatenate the columns in order to get "cf1.a_cf1.b_cf2.c"
  public static String concatColumnsToString(List<Column> colList) {
    String string = "";
    for (Column column : colList) {
      string = string + column.toString() + " ";
    }
    // trim trailing comma
    string = string.trim().replace(" ", "_");
    return string;
  }

  // Concatenate the columns in order to get "cf1.a_c1f.b_cf2.c"
  public static byte[] concatColumnsToBytes(List<Column> colList) {
    byte[] concatColumns = Bytes.toBytes("");
    for (Column column : colList) {
      concatColumns = Util.concatByteArray(concatColumns,
          Util.concatByteArray(column.getConcatByteArray(),
              Bytes.toBytes("_")));
    }
    return concatColumns;
  }

  public static List<Column> buildColumnList(String str) {
    List<Column> colList = new ArrayList<Column>();
    String[] strColumn = null;
    String[] strList = str.split("_");
    for (int i = 0; i < strList.length; i++) {
      strColumn = strList[i].split(".");
      if (strColumn.length == 1) {
        colList.add(new Column(Bytes.toBytes(strColumn[0])));
      } else {
        colList.add(new Column(Bytes.toBytes(strColumn[0]),
            Bytes.toBytes(strColumn[1])));
      }
    }
    return colList;
  }

  public static List<Column> buildColumnList(
      List<ProtoColumn> protoColumnList) {
    List<Column> colList = new ArrayList<Column>();
    Column queryColumn = null;
    for (ProtoColumn requestCol : protoColumnList) {
      queryColumn = new Column(
          requestCol.getFamily().toByteArray(),
          requestCol.getQualifier().toByteArray());
      colList.add(queryColumn);
    }
    return colList;
  }

  /**
   * Gets the name of the HTable where the specific secondary index is
   * located. The format of the table name is the concatenation of the byte
   * values of : the primary table name, the column family name, the column
   * name and '_idx'
   *
   * @param primaryTableName - the table name where the values to be indexed reside.
   * @param family           - the column family where the column needs to be indexed
   * @param qualifier        - the column name
   * @return
   * @throws IOException
   *
  public static byte[] getSecondaryIndexTableName(byte[] primaryTableName,
      byte[] family, byte[] qualifier) {
    byte[] suffix = Bytes.toBytes(SecondaryIndexConstants.INDEX_TABLE_SUFFIX);
    byte[] tableName =
        new byte[primaryTableName.length + family.length + qualifier.length + suffix.length];
    int position = 0;

    System.arraycopy(primaryTableName, 0, tableName, position,
        primaryTableName.length);
    position += primaryTableName.length;

    System.arraycopy(family, 0, tableName, position, family.length);
    position += family.length;

    System.arraycopy(qualifier, 0, tableName, position, qualifier.length);
    position += qualifier.length;

    System.arraycopy(suffix, 0, tableName, position, suffix.length);
    position += suffix.length;

    return tableName;
  }*/

  public static byte[] getSecondaryIndexTableName(byte[] primaryTableName,
      byte[] family, byte[] qualifier) {
    return getSecondaryIndexTableName(primaryTableName,
        new Column(family, qualifier));
  }
  public static byte[] getSecondaryIndexTableName(byte[] primaryTableName,
      IndexedColumn indexedColumn) {
    return Util.concatByteArray(primaryTableName,
        Util.concatByteArray(Bytes.toBytes(indexedColumn.toString()),
            Bytes.toBytes(SecondaryIndexConstants.INDEX_TABLE_SUFFIX)));
  }
  public static byte[] getSecondaryIndexTableName(byte[] primaryTableName,
      Column indexedColumn) {
    return Util.concatByteArray(primaryTableName,
        Util.concatByteArray(Bytes.toBytes(indexedColumn.toString()),
            Bytes.toBytes(SecondaryIndexConstants.INDEX_TABLE_SUFFIX)));
  }
  public static String getSecondaryIndexTableName(String primaryTableName,
      IndexedColumn indexedColumn) {
    return primaryTableName + indexedColumn.toString() +
        SecondaryIndexConstants.INDEX_TABLE_SUFFIX;
  }
  public static String getSecondaryIndexTableName(String primaryTableName,
      Column indexedColumn) {
    return primaryTableName + indexedColumn.toString() +
        SecondaryIndexConstants.INDEX_TABLE_SUFFIX;
  }

  /**
   * Deserializes the TreeSet containing the primary row keys from a byte array.
   *
   * @param idxBytes
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("unchecked")
  public static TreeSet<byte[]> deserializeIndex(final byte[] idxBytes)
      throws IOException, ClassNotFoundException {
    ObjectInputStream objInputStream =
        new ObjectInputStream(new ByteArrayInputStream(idxBytes));
    return (TreeSet<byte[]>) objInputStream.readObject();
  }

  /**
   * Serializes a the TreeSet containing the primary row keys into a byte array
   *
   * @param idxTree
   * @return
   * @throws IOException
   */
  public static byte[] serializeIndex(final TreeSet<byte[]> idxTree)
      throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ObjectOutputStream objOutputStream = new ObjectOutputStream(os);
    objOutputStream.writeObject(idxTree);
    return os.toByteArray();
  }
}
