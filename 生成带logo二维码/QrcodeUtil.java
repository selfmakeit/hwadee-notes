package qrcode;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrcodeUtil {
	
	private OutputStream generatedFileStream;
	private File logoFile;

	/** Main function for console app. Just launch without parameters
	 * @param args
	 */
//	public static void main(String[] args) {
//		
//		String content="https://music.163.com/#/song?id=20954458";
//		String logoFilePath="F:\\aa.png";
//		String saveFilePath="F:\\qr2code.png";
//		String filename="F:\\plcode.png";
//		Qrcode generator = new Qrcode();
//		generator.setLogoFile( new File( logoFilePath));
//		File targetFile = new File( saveFilePath);
//		
//		OutputStream os=null;
//		try {
//			generator.setGeneratedFileStream( new FileOutputStream(targetFile));
//			generator.createQrCode( content, 300, "png");
//			os=generator.getGeneratedFileStream();
//			os.flush();//强制刷新出去
//			System.out.println("done.");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//			if(null!=os){
//				try {
//					os.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					System.out.println("关闭输出资源失败");
//				}
//			}
//		}
//
//	}
	public static File  generateQRCodeWithLogo(String content,int size,File logoFile,String qrFileType) {
		QrcodeUtil generator = new QrcodeUtil();
		generator.setLogoFile( logoFile);
		File targetFile = new File(System.getProperty( "user.dir" )+(new Date()).getTime()+"."+qrFileType);
		OutputStream os=null;
		try {
			generator.setGeneratedFileStream( new FileOutputStream(targetFile));
			generator.createQrCode( content, 300, "png");
			os=generator.getGeneratedFileStream();
			os.flush();//强制刷新出去
//			System.out.println("done.");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null!=os){
				try {
					os.close();
				} catch (IOException e) {
					System.out.println("close faild");
				}
			}
		}
			return targetFile;
	}
	/**
	 * Call this method to create a QR-code image. You must provide the
	 * OutputStream where the image data can be written.
	 * 
	 * @param content
	 *            The string that should be encoded with the QR-code.
	 * @param qrCodeSize
	 *            The QR-code must be quadratic. So this is the number of pixel
	 *            in width and height.
	 * @param imageFormat
	 *            The image format in which the image should be rendered. As
	 *            Example 'png' or 'jpg'. See @javax.imageio.ImageIO for more
	 *            information which image formats are supported.
	 * @throws Exception
	 *             If an Exception occur during the create of the QR-code or
	 *             while writing the data into the OutputStream.
	 */
	
		private void createQrCode(String content, int qrCodeSize, String imageFormat){
        try {
        	// Correction level - HIGH - more chances to recover message
        	Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap =
        			new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // Generate QR-code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content,
            		BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
            
            // Start work with picture
            int matrixWidth = bitMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
            		BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
            graphics.setFont(font);
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            Color mainColor = new Color(51, 102, 153);
            graphics.setColor(mainColor);
            // Write message under the QR-code
           
//            graphics.drawString( content, 30, image.getHeight() - graphics.getFont().getSize());

            //Write Bit Matrix as image
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            
            // Add logo to QR code
            BufferedImage logo = ImageIO.read( this.getLogoFile());

            //scale logo image and insert it to center of QR-code
            double scale = calcScaleRate(image, logo);
            logo = getScaledImage( logo,
            		(int)( logo.getWidth() * scale),
            		(int)( logo.getHeight() * scale) );
            graphics.drawImage( logo,
            		image.getWidth()/2 - logo.getWidth()/2,
            		image.getHeight()/2 - logo.getHeight()/2,
            		image.getWidth()/2 + logo.getWidth()/2,
            		image.getHeight()/2 + logo.getHeight()/2,
            		0, 0, logo.getWidth(), logo.getHeight(), null);
            
            // Check correctness of QR-code
            if ( isQRCodeCorrect(content, image)) {
            	ImageIO.write(image, imageFormat, this.getGeneratedFileStream());
//            	System.out.println("Your QR-code was succesfully generated.");
            } else {
            	System.out.println("Sorry, your logo has broke QR-code. ");
            }            
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
		/**
		 * generate QrCode without logo
		 * @param content
		 * @param qrCodeSize
		 * @param filename
		 */
		private void createPlainQrCode(String content, int qrCodeSize, String filename){
			try {
				BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
				MatrixToImageWriter.writeToFile(matrix, filename.substring(filename.lastIndexOf('.')+1), new File(filename));
				
			} catch (IOException | WriterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	/**
	 * Calc scale rate of logo. It is 30% of QR-code size
	 * @param image
	 * @param logo
	 * @return
	 */
	private double calcScaleRate(BufferedImage image, BufferedImage logo){
		 double scaleRate = logo.getWidth() / image.getWidth();
         if (scaleRate > 0.3){
        	 scaleRate = 0.3;
         } else {
        	 scaleRate = 1;
         }
		 return scaleRate;
	}
	
	/**
	 * Check is QR-code correct
	 * @param content
	 * @param image
	 * @return
	 */
	private boolean isQRCodeCorrect(String content, BufferedImage image){
		boolean result = false;
		Result qrResult = decode(image);
		if (qrResult != null && content != null && content.equals(qrResult.getText())){
			result = true;
		}		
		return result;
	}
	/**
	 * Decode QR-code.
	 * @param image
	 * @return
	 */
	private Result decode(BufferedImage image){
		if (image == null) {
			return null;
		}
		try {
			LuminanceSource source = new BufferedImageLuminanceSource(image);	      
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));	      
			Result result = new MultiFormatReader().decode(bitmap, Collections.EMPTY_MAP);	      
			return result;
		} catch (NotFoundException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}
	/**
	 * Scale image to required size
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	private BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
		int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(
	    		scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}

	public OutputStream getGeneratedFileStream() {
		return generatedFileStream;
	}

	public void setGeneratedFileStream(OutputStream generatedFileStream) {
		this.generatedFileStream = generatedFileStream;
	}

	public File getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(File logoFile) {
		this.logoFile = logoFile;
	}
	
}