//使用方法：
public class Main {
	public static void main(String[] args) {
		String logoFilePath="F:\\aa.png";
		File logoFile = new File(logoFilePath);
		//带logo二维码
		File qr = QrcodeUtil.generateQRCodeWithLogo("hello", 200, logoFile, "png");
		//不带logo二维码
		File qr2 = QrcodeUtil.createPlainQrCode("dfdsfsdfsd", 300, "jpg");
		System.out.println(qr.getAbsolutePath());
		System.out.println(qr2.getAbsolutePath());
	}
}
