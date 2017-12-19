package segmentedfilesystem;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
      		System.out.println("Host Name?");
      		return;
    	}	
        
        //set up socket for the datagrams
        //set up buffer and address.
	DatagramSocket socketServe = new DatagramSocket();
    byte[] buffer = new byte[1028];
    InetAddress address = InetAddress.getByName(args[0]);
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 6014);
    socketServe.send(packet);
    
    //create array lists for the headers, data, and the number of packets
	List<byte[]> header = new ArrayList<byte[]>();
    List<byte[]> data = new ArrayList<byte[]>();
    List<byte[]> packet_num = new ArrayList<byte[]>();
		
    //initialize LP as the last packet
    //initialize TP as total, set to 3 as given in lab instructions
	int LP = 0, TP = 3, count = 0;
	//while currently not on the last packet check by both LP and Count
	while (LP != 3 || count != TP) {
		//create new datagram received packet for each 
		DatagramPacket RecievedP = new DatagramPacket(buffer, buffer.length);
		//set socket serve to receive the new datagram
		socketServe.receive(RecievedP);
		//Increment count + 1 to move to the next packet when the loop repeats
		count++;
		//create ne  byte to be use for received
		byte[] received = new byte[RecievedP.getLength()];
			//the byte array received to the value of the data from RecievedP
		for(int i = 0; i < received.length; i++){
			received[i] = RecievedP.getData()[i];
		}
		
		//new byte equal to the first value of received
		byte stat = received[0];
		//if the new byte stat is evenly divisible by 2 then add to the header
		//else if it is the 3 bit within the byte then incement lp by 1 
		//and add 1 + packetnum to TP
		if (stat % 2 == 0) {
			header.add(received);
		} else if (stat % 4 == 3) {
			int packetNum = packetNum(received[2], received[3]);
			TP += (packetNum + 1);
			LP++;
			data.add(received);
			packet_num.add(received);
		} else {
			data.add(received);
		}
			
	}
	
	List<byte[]> f0 = makeArrayList(data, listSize(packet_num, header.get(0)[1]), header.get(0)[1]);
	List<byte[]> f1 = makeArrayList(data, listSize(packet_num, header.get(1)[1]), header.get(1)[1]);
	List<byte[]> f2 = makeArrayList(data, listSize(packet_num, header.get(2)[1]), header.get(2)[1]);
		
	writer(f0, retrieveFN(header, 0));
	writer(f1, retrieveFN(header, 1));
	writer(f2, retrieveFN(header, 2));
	socketServe.close();
    }
	
    //check the byte values, if the are greater than zero set the packet equal to byte + 256
    //else set to the packet equal to the byte
	public static int packetNum(byte b1, byte b2) {
		int numberPacket, P1, P2;
		
		if(b1 < 0){
			P1 = b1 + 256;
		} else {
			P1 = b1;
		}
		if(b2 < 0){
			P2 = b2 + 256;
		} else {
			P2 = b2;
		}

		numberPacket = 256 * P1 + P2;
		return numberPacket;
	}
	public static int listSize(List<byte[]> amount, byte fID) {
		int PacketNumber = -1;
		for (int i = 0; i < amount.size(); i++) {
			byte[] arr = amount.get(i);
			if (arr[1] == fID) {
				PacketNumber = packetNum(arr[2], arr[3]) + 1;
			}
		}
		return PacketNumber;
	}
	
	//sort received into either header or data 
	public static void sorter(byte[] received, List<byte[]> header, List<byte[]> data) {
		if (received[0] % 2 == 0) {
			header.add(received);
		} else {
			data.add(received);
		}
	}
	
	//loop through the 2d array list and write each value to the file FileToBeWritten
	public static void writer(List<byte[]> list, String fn) throws IOException {
		FileOutputStream FileToBeWritten = new FileOutputStream(fn);
		
		for (int i = 0; i < list.size(); i++) {
			for (int j = 4; j < list.get(i).length; j++) {
				FileToBeWritten.write(list.get(i)[j]);
			}
		}
		FileToBeWritten.close();
	}    
	
	//loops through the array and sets each index equal to the file name from header
	public static String retrieveFN(List<byte[]> header, int filenumber) {
		String fileName = null;
		byte[] byteFN = new byte[header.get(filenumber).length - 2];
		
		for (int i = 0; i < byteFN.length; i++) {
			byteFN[i] = header.get(filenumber)[i + 2];
		}
		
		fileName = new String(byteFN);
		return fileName;
	}
	
	//
	public static List<byte[]> makeArrayList(List<byte[]> data, int fSize, byte fID) {
		List<byte[]> list = new ArrayList<byte[]>(fSize);
		for (int i = 0; i < fSize; i++) {
			list.add(null);
		}
		//loop through the data array and set packetNum to values of data at i
		for (int i = 0; i < data.size(); i++) {
			byte dataFID = data.get(i)[1];
			int packetNum = packetNum(data.get(i)[2], data.get(i)[3]);
			
			//of the data if an the file id are the same set the list to the packet num and data at i
			if (dataFID == fID) {
				list.set(packetNum, data.get(i));
			}
		}
		return list;
	}
}