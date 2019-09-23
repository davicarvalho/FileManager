import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class App5 {

	public static void main(String[] args) throws Exception {
		String restoDoBloco = "04Davi06ahdoiuasgiuodas";
		
		Integer tamColuna = Integer.parseInt(restoDoBloco.substring(0, 2));
		String infoColuna = restoDoBloco.substring(2, 2+tamColuna);
		
		System.out.println(infoColuna);
		System.out.println(restoDoBloco);
		
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

	// 001101106hannah
	// 001101106hannah

}
