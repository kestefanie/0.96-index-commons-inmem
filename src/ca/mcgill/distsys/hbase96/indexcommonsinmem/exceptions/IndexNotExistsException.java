package ca.mcgill.distsys.hbase96.indexcommonsinmem.exceptions;

import java.io.IOException;

public class IndexNotExistsException extends IOException {

	
	private static final long serialVersionUID = -3542681297200745291L;
	
	public IndexNotExistsException (String msg) {
		super(msg);
	}
}
