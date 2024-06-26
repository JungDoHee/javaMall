package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import javax.swing.ImageIcon;

import model.item.ImageDTO;

public class ImageUpload {
	private String strMsg;
	private String ABSOLUT_IMAGE_PATH;
	private String SERVER_IMAGE_PATH;
	private int DEFAULT_THUMB_RATIO = 300;

	public ImageUpload(Map<String, String> path_info) {
		File file_path = new File(path_info.get("absolute"));
		this.ABSOLUT_IMAGE_PATH = file_path.getAbsolutePath();

		this.SERVER_IMAGE_PATH = path_info.get("server");

		if (path_info.get("ratio") != null) {
			this.DEFAULT_THUMB_RATIO = Integer.parseInt(path_info.get("ratio"));
		}
	}

	public ImageUpload(ServletContext application) {
		File file_path = new File(application.getInitParameter("uploadPath"));
		this.ABSOLUT_IMAGE_PATH = file_path.getAbsolutePath();

		this.SERVER_IMAGE_PATH = application.getInitParameter("imageServer");
	}

	public ImageUpload(ServletContext application, int ratio) {
		File file_path = new File(application.getInitParameter("uploadPath"));
		this.ABSOLUT_IMAGE_PATH = file_path.getAbsolutePath();

		this.SERVER_IMAGE_PATH = application.getInitParameter("imageServer");

		this.DEFAULT_THUMB_RATIO = ratio;
	}

	public Map<Integer, ImageDTO> uploadImage(List<Part> file_list) {
		Map<Integer, ImageDTO> rgReturnFileInfo = new HashMap<>();

		try {
			int nCounter = 0;
			System.out.println("counter => " + nCounter);
			for (Part file_info : file_list) {
				ImageDTO imageDto = new ImageDTO();
				if (file_info.getSubmittedFileName().isEmpty()) {
					throw new Exception("이미지 저장 실패 원본 파일명 : [" + file_info.getSubmittedFileName() + "]");
				}
				String imageAbsolutePath = this.ABSOLUT_IMAGE_PATH + File.separatorChar;

				String imageServerPath = this.SERVER_IMAGE_PATH + File.separatorChar;
				file_info.write(imageAbsolutePath + file_info.getSubmittedFileName());

				imageDto.setAbsoluteImagePath(imageAbsolutePath + file_info.getSubmittedFileName());
				imageDto.setHashName("");
				imageDto.setImageServerPath(imageServerPath + file_info.getSubmittedFileName());
				imageDto.setOriginalName(file_info.getSubmittedFileName());

				rgReturnFileInfo.put(nCounter, imageDto);
				nCounter++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.strMsg = e.getMessage();

			for (Iterator<Integer> iterator = rgReturnFileInfo.keySet().iterator(); iterator.hasNext();) {
				int fileCounter = iterator.next().intValue();
				if (rgReturnFileInfo.get(fileCounter).getOriginalName() != null) {
					deleteFile(rgReturnFileInfo.get(fileCounter).getOriginalName());
				}
			}

		}
		return rgReturnFileInfo;
	}

	public Map<String, String> createThumbNail(String origin_file_name) {
		Map<String, String> rgThumbImageInfo = new HashMap<>();
		try {
			Image image;
			File cFile = new File(this.ABSOLUT_IMAGE_PATH);
			String strAbsoluteOriginPath = cFile.getAbsolutePath() + cFile.getAbsolutePath() + File.separatorChar;

			File cOriginFile = new File(strAbsoluteOriginPath);
			int nIndex = origin_file_name.lastIndexOf(".");
			String strExtension = origin_file_name.substring(nIndex + 1);

			String strAbsoluteThumbPath = cFile.getAbsolutePath() + cFile.getAbsolutePath() + "썸네일_"
					+ File.separatorChar;
			String strServerThumbPath = this.SERVER_IMAGE_PATH + this.SERVER_IMAGE_PATH + "썸네일_" + File.separatorChar;

			File cThumbFile = new File(strAbsoluteThumbPath);

			BufferedImage originImage = ImageIO.read(cOriginFile);

			int nWidth = originImage.getWidth((ImageObserver) null);
			int nHeight = originImage.getHeight((ImageObserver) null);
			nHeight = this.DEFAULT_THUMB_RATIO * nHeight / nWidth;

			BufferedImage thumbImage = new BufferedImage(nWidth, nHeight, 4);

			Graphics2D createThumb = thumbImage.createGraphics();

			String[] strImageIoExtension = { "jpg", "png", "bmp" };
			List<String> rgImageIoExtension = new ArrayList<>(Arrays.asList(strImageIoExtension));
			if (rgImageIoExtension.contains(strExtension)) {
				image = originImage.getScaledInstance(nWidth, nHeight, 4);
			} else {
				image = (new ImageIcon(strAbsoluteOriginPath)).getImage();
			}

			createThumb.drawImage(image, 0, 0, nWidth, nHeight, null);
			createThumb.dispose();

			ImageIO.write(thumbImage, strExtension, cThumbFile);

			rgThumbImageInfo.put("path", strServerThumbPath);
			rgThumbImageInfo.put("name", "썸네일_" + cOriginFile.getName());
			rgThumbImageInfo.put("hash_name", "");
			rgThumbImageInfo.put("absolute_path", strAbsoluteThumbPath);
		} catch (Exception e) {
			this.strMsg = e.getMessage();
		}
		return rgThumbImageInfo;
	}

	public boolean deleteFile(String file_name) {
		boolean bDelete = false;
		try {
			String targetFilePath = this.ABSOLUT_IMAGE_PATH + this.ABSOLUT_IMAGE_PATH + File.separatorChar;
			File cFile = new File(targetFilePath);
			cFile.deleteOnExit();

			bDelete = true;
		} catch (Exception e) {
			this.strMsg = e.getMessage();
		}
		return bDelete;
	}

	public String getMessage() {
		return this.strMsg;
	}
}