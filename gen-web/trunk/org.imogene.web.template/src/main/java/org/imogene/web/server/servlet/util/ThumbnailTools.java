package org.imogene.web.server.servlet.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ThumbnailTools {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	public static final String IMAGE_JPEG = "jpeg";
	public static final String IMAGE_JPG = "jpg";
	public static final String IMAGE_PNG = "png";

	private ImageIcon image;
	private ImageIcon thumb;

	public ThumbnailTools(Image pImage) {
		image = new ImageIcon(pImage);
	}

	public ThumbnailTools(String fileName) {
		image = new ImageIcon(fileName);
	}

	/**
	 * Create a thumbnail for the current image
	 * @param size the size
	 * @param dir the direction, Horizontal or Vertical
	 * @return the thumbnail image
	 */
	public Image getThumbnail(int size, int dir) {
		if (dir == HORIZONTAL) {
			thumb = new ImageIcon(image.getImage().getScaledInstance(size, -1,
					Image.SCALE_SMOOTH));
		} else {
			thumb = new ImageIcon(image.getImage().getScaledInstance(-1, size,
					Image.SCALE_SMOOTH));
		}
		return thumb.getImage();
	}

	/**
	 * Create a thumbnail for the current image
	 * @param size the size
	 * @param dir the direction, Horizontal or Vertical
	 * @param scale the scale
	 * @return the thumbnail image
	 */
	public Image getThumbnail(int size, int dir, int scale) {
		if (dir == HORIZONTAL) {
			thumb = new ImageIcon(image.getImage().getScaledInstance(size, -1,
					scale));
		} else {
			thumb = new ImageIcon(image.getImage().getScaledInstance(-1, size,
					scale));
		}
		return thumb.getImage();
	}

	/**
	 * Save the thumbnail to the specified file, with the specified type
	 * @param file the file
	 * @param imageType the image type
	 */
	public void saveThumbnail(File file, String imageType) {
		if (thumb != null) {
			BufferedImage bi = new BufferedImage(thumb.getIconWidth(), thumb
					.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.drawImage(thumb.getImage(), 0, 0, null);
			try {
				ImageIO.write(bi, imageType, file);
			} catch (IOException ioe) {
				throw new RuntimeException("Error occured saving thumbnail");
			}
		} else {
			throw new RuntimeException("Thumbnail have to be created before.");
		}
	}
	
}
