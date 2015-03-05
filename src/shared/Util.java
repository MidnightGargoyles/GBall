package shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Util {
	public static byte[] pack(MsgData msg) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			
			/* found that casting weren't necessary
			 * switch(msg.getType()) {
			case MsgData.KEYFRAME: 
				oos.writeObject((Keyframe) msg);
				break;
			case MsgData.SUBFRAME: 
				oos.writeObject((Subframe) msg);
				break;
			case MsgData.CONNECTION: 
				oos.writeObject((Connection) msg);
				break;
			case MsgData.INPUT:
				oos.writeObject((Input) msg);
				break;
			default:
				System.err.println("Could not pack this! " + msg);
				return null;
			}*/
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return baos.toByteArray();
	}
	
	public static MsgData unpack(byte[] data) {
		
		ByteArrayInputStream baos = new ByteArrayInputStream(data);
	    try {
			ObjectInputStream oos = new ObjectInputStream(baos);
			return (MsgData) oos.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
}
