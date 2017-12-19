package segmentedfilesystem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class dataPacket {
	
	//if evenly divisble by 2 then it is determined to be a header
	public boolean isHeader(byte[] comp) {
		if (comp[0] % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//if mod 4 has a remainder of 3 then the byte is considered to be the end
	
	public boolean isEnd(byte[] bytes){
		return bytes[0] % 4 == 3;
	}
	
	//return the packet Number for the byte
	public int packetNumber(byte[] bytes){
		
		return new BigInteger(new byte[]{bytes[2], bytes[3]}).intValue();
	} 
	
	//run sort on on the given byte array, uses custom define comparator.
	public void sorter(ArrayList<byte[]> packet){
		packet.sort(new comparator());
	}
	private class comparator implements Comparator<byte[]> {
		//compares two bytes
		//if the first byte is larger then it returns a 1
		//if the second byte is larger then it returns a -1
		//if the two are equal the return value is 0
		@Override
		public int compare(byte[] b1, byte[] b2) {
			if (packetNumber(b1) > packetNumber(b2)) {
				return 1;
			}
			if (packetNumber(b1) < packetNumber(b2)) {
				return -1;
			}
			return 0;
		}
	}
}