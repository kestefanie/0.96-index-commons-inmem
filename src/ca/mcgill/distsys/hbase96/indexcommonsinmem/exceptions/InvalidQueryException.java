package ca.mcgill.distsys.hbase96.indexcommonsinmem.exceptions;

public class InvalidQueryException extends Exception {
    private static final long serialVersionUID = 1455208121281616331L;

    public InvalidQueryException(String msg) {
        super(msg);
    }

}
