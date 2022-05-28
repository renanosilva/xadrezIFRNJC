package uteis;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;

public class CompressaoImagemUtils {

	
	/** Faz a compressão de uma imagem para que ela fique com um tamanho em bytes menor. */
	public static byte[] resize(InputStream inputStream) throws IOException {
		
//		File imageFile = new File("pai.bmp");
		File compressedImageFile = new File("compressed_file.jpg");
//
//		InputStream inputStream = new FileInputStream(imageFile);
		OutputStream outputStream = new FileOutputStream(compressedImageFile);

		float imageQuality = 0.3f;

		//Create the buffered image
		BufferedImage bufferedImage = ImageIO.read(inputStream);

		//Get image writers
		Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

		if (!imageWriters.hasNext())
			throw new IllegalStateException("Writers Not Found!!");

		ImageWriter imageWriter = (ImageWriter) imageWriters.next();
		ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
		imageWriter.setOutput(imageOutputStream);

		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

		//Set the compress quality metrics
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(imageQuality);

		//Created image
		imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
		
		// close all streams
		inputStream.close();
		outputStream.close();
		imageOutputStream.close();
		imageWriter.dispose();
		
		InputStream inputStreamCompressed = new FileInputStream(compressedImageFile);
		return IOUtils.toByteArray(inputStreamCompressed);
    }
	
	/** Faz a compressão de uma imagem para que ela fique com um tamanho em bytes menor. */
	public static byte[] resize(InputStream inputStream, String extensao) throws IOException {
		
		//Salvando dados EXIF da imagem (para manter orientação)
		ImageReader reader = ImageIO.getImageReadersBySuffix(extensao).next();
		reader.setInput(ImageIO.createImageInputStream(inputStream));
		IIOMetadata metadata = reader.getImageMetadata(0); 
		
		BufferedImage bi = reader.read(0);
		
		//File compressedImageFile = new File("compressed_file.jpg");
		
		// I'm writing to byte array in memory, but you may use any other stream
		ByteArrayOutputStream os = new ByteArrayOutputStream(255);
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);

		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter writer = iter.next();
		writer.setOutput(ios);

		//You may want also to alter jpeg quality
		ImageWriteParam iwParam = writer.getDefaultWriteParam();
		iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwParam.setCompressionQuality(0.3f);

		//Note: we're using metadata we've already saved.
		writer.write(null, new IIOImage(bi, null, metadata), iwParam);
		writer.dispose();

		// close all streams
		inputStream.close();
		
		ByteArrayInputStream bai = new ByteArrayInputStream(os.toByteArray());
		RenderedImage out = ImageIO.read(bai);
		int size = os.toByteArray().length;

		return os.toByteArray();
		
    }
	
}
