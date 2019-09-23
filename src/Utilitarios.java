import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utilitarios {
	
	public static String lpad2(Integer s) {
		if (s.toString().length() == 1) {
			return "0" + s;
		}
		return s.toString();
	}

	public static String lpad3(Integer s) {
		String str = s.toString();
		while (str.length() < 3) {
			str = "0" + str;
		}
		return str;
	}

	public static String lpad4(Integer s) {
		String str = s.toString();
		while (str.length() < 4) {
			str = "0" + str;
		}
		return str;
	}
	
	public static short bytesToShort(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public static byte[] shortToBytes(short value) {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte) (value & 0xff);
		returnByteArray[1] = (byte) ((value >>> 8) & 0xff);
		return returnByteArray;
	}
}
