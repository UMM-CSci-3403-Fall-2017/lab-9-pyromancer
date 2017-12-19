package segmentedfilesystem;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.util.ArrayList;

import org.junit.Test;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */
public class Tests {

	//test given bytes to determine header
	@Test
    public void HeaderTest() {
        byte[] test = {0, 20, 35, 89, 23, 90, 78};
        dataPacket packet = new dataPacket();
        
        assertTrue(packet.isHeader(test));
    }
	
	@Test
    public void packetNumberTest(){
    	byte[] test = {3, 34, 3, 3, 23, 89, 34, 78};
    	dataPacket packet = new dataPacket();
    	
    	assertEquals(packet.packetNumber(test), 771);
	}
	
    //test given bytes to determine end
	@Test 
	public void EndTest() {
		byte[] test = {7, 15, 19, 23, 27, 31};
    	dataPacket packet = new dataPacket();
    	
    	assertTrue(packet.isEnd(test));
	}

	@Test
    //Tests our packet sorting
    public void packetSortTest(){
    	byte[] firstPacket = {3,43,0,0,5,34,23};
    	ArrayList<byte[]> packets = new ArrayList<>();
    	packets.add(firstPacket);
    	dataPacket test = new dataPacket();
    	
    	test.sorter(packets);
    	assertEquals(test.packetNumber(packets.get(0)), 0);
    }
	
	
}
