package ca.mcgill.distsys.hbase96.indexcommons.proto;

public class Range {

	private byte[] lowerBound, higherBound;
	
	public Range(byte [] lower, byte[] higher) {
		this.lowerBound = lower;
		this.higherBound = higher;
	}
	
	public byte [] getLowerBound() {
		return lowerBound;
	}
	
	public byte [] getHigherBound() {
		return higherBound;
	}

}
