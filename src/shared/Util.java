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
			
			switch(msg.getType()) {
			case MsgData.KEYFRAME: 
				oos.writeObject((Keyframe) msg);
				break;
			case MsgData.SUBFRAME: 
				oos.writeObject((Subframe) msg);
				break;
			case MsgData.CONNECTION: 
				oos.writeObject((Connection) msg);
				break;
			}
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
