package ca.mcgill.distsys.hbase96.indexcommons.exceptions;

import java.io.IOException;

public class IndexAlreadyExistsException extends IOException {
	private static final long serialVersionUID = 1104991619246147916L;

	public IndexAlreadyExistsException(String msg) {
		super(msg);
	}
}
