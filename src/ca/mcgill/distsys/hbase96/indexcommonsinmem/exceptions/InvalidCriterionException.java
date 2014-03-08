package ca.mcgill.distsys.hbase96.indexcommonsinmem.exceptions;

public class InvalidCriterionException extends InvalidQueryException {
    private static final long serialVersionUID = 1804716158410102667L;

    public InvalidCriterionException(String msg) {
        super(msg);
    }
}
