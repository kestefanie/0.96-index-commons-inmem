package ca.mcgill.distsys.hbase96.indexcommons.exceptions;

import java.io.IOException;

public class IndexNotFoundException extends IOException {
    private static final long serialVersionUID = 3504282839869350714L;

    public IndexNotFoundException() {
        super();
    }

    public IndexNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexNotFoundException(String message) {
        super(message);
    }

    public IndexNotFoundException(Throwable cause) {
        super(cause);
    }

}
