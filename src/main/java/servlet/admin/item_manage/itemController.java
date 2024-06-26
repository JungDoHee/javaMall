package servlet.admin.item_manage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import Exception.FileException;
import common.regexpPattern;
import common.validator;
import model.item.ImageDTO;
import model.item.ItemDAO;
import model.item.ItemDTO;
import util.ImageUpload;
import util.dateFormat;

@MultipartConfig(maxFileSize = 1048576L, maxRequestSize = 8388608L)
@WebServlet({ "/adminMall/item_manage/item_reg/*" })
public class itemController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;

	private HttpServletResponse res;

	private String msg;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall/item_manage/list.jsp";
		try {
			this.req = req;
			this.res = res;
			String[] rgUri = req.getRequestURI().split("/");
			String strLastUri = rgUri[rgUri.length - 1];
			switch (strLastUri) {
			case "delete":
				doDelete();
				break;
			case "modify":
				doModify();
				break;
			case "reg":
				doReg();
				break;
			default:
				throw new FileException("잘못된 경로로 접근했습니다", strGoUrl);
			}

		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	protected void doReg() throws IOException, ServletException {
		int nItemSeq = 0;
		try {
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);
			String[] rgRequired = { "item_title", "item_price", "item_contents", "item_category", "item_stock" };
			for (String valid_check : rgRequired) {
				if (valid.isEmpty(valid_check)) {
					throw new Exception("필수값을 모두 입력하시기 바랍니다");
				}
			}
			if (this.req.getPart("item_thumb").getSize() < 1L) {
				throw new Exception("썸네일을 선택해주시기 바랍니다");
			}
			String[] rgNumeric = { "item_category", "item_stock", "item_price" };
			for (String pattern_numeric : rgNumeric) {
				if (!req.getParameter(pattern_numeric).matches(regexpPattern.REGEXP_NUMERIC)) {
					throw new Exception(pattern_numeric + "을(를) 숫자 형식으로 입력하시기 바랍니다");
				}
			}
			ImageUpload CUpload = new ImageUpload(getServletContext());
			List<Part> partThumb = new ArrayList<>();
			partThumb.add(this.req.getPart("item_thumb"));
			Map<Integer, ImageDTO> rgThumbInfo = CUpload.uploadImage(partThumb);

			if (rgThumbInfo.get(0).getOriginalName() == null || rgThumbInfo.get(0).getOriginalName().isEmpty()) {
				throw new Exception(CUpload.getMessage());
			}
			List<Part> partFile = new ArrayList<>();
			for (Part part_info : this.req.getParts()) {
				if (part_info.getName().equals("item_image[]")) {
					partFile.add(part_info);
				}
			}
			Map<Integer, ImageDTO> rgFileInfo = CUpload.uploadImage(partFile);
			ItemDTO item_dto = new ItemDTO();
			ItemDAO dao = new ItemDAO(getServletContext());
			item_dto.setItemSubject(this.req.getParameter("item_title"));
			item_dto.setItemPrice(this.req.getParameter("item_price"));
			item_dto.setItemStatus("a");
			item_dto.setItemInfo(this.req.getParameter("item_contents"));
			item_dto.setCategorySeq(this.req.getParameter("item_category"));
			item_dto.setInitStock(this.req.getParameter("item_stock"));
			item_dto.setThumbPath(rgThumbInfo.get(0).getImageServerPath());
			item_dto.setThumbHashName(rgThumbInfo.get(0).getHashName());
			item_dto.setThumbName(rgThumbInfo.get(0).getOriginalName());
			item_dto.setThumbPath(rgThumbInfo.get(0).getImageServerPath());
			item_dto.setThumbHashName(rgThumbInfo.get(0).getHashName());
			item_dto.setThumbName(rgThumbInfo.get(0).getOriginalName());
			item_dto.setAdminNo(session.getAttribute("admin_no").toString());
			nItemSeq = dao.itemRegProc(item_dto, rgFileInfo);
			if (nItemSeq < 1) {
				throw new Exception(dao.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}
		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/item_manage/list");
	}

	protected void doModify() throws IOException {
		System.out.println("doModify");
		try {
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").toString().equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);

			String[] rgRequired = { "item_seq", "item_price", "item_info", "item_category", "item_stock" };
			for (String valid_check : rgRequired) {
				if (valid.isEmpty(valid_check)) {
					throw new Exception("필수값을 모두 입력하시기 바랍니다");
				}
			}

			Map<String, String> uploadFileInfo = new HashMap<>();
			if (this.req.getPart("item_thumb") != null && this.req.getPart("item_thumb").getSize() > 0L) {
				uploadFileInfo = uploadFile(this.req.getPart("item_thumb"));
				if (uploadFileInfo.get("name") == null || ((String) uploadFileInfo.get("name")).equals("")) {
					throw new Exception("파일 업로드 실패했습니다");
				}
			}
			ItemDTO dto = new ItemDTO();
			dto.setItemSeq(this.req.getParameter("item_seq"));
			dto.setItemSubject(this.req.getParameter("item_subject"));
			dto.setItemPrice(this.req.getParameter("item_price"));
			dto.setItemStatus(this.req.getParameter("item_status"));
			dto.setItemInfo(this.req.getParameter("item_info"));
			dto.setCategorySeq(this.req.getParameter("item_category"));
			dto.setInitStock(this.req.getParameter("item_stock"));
			if (this.req.getPart("item_thumb") != null && this.req.getPart("item_thumb").getSize() > 0L) {
				dto.setThumbPath(uploadFileInfo.get("path"));
				dto.setThumbName(uploadFileInfo.get("name"));
			}
			dto.setModifyDate(dateFormat.getNow());
			ItemDAO dao = new ItemDAO(getServletContext());
			int nUpdateResult = dao.modifyItemProc(dto);
			if (nUpdateResult < 1)
				throw new Exception("물품 수정 실패했습니다");
			if (this.req.getPart("item_thumb") != null && this.req.getPart("item_thumb").getSize() > 0L) {
				String itemSeq = this.req.getParameter("item_seq");
				List<ItemDTO> deleteImageInfo = dao.getItemDetail(itemSeq);
				for (ItemDTO imageInfo : deleteImageInfo) {
					if (imageInfo.getThumbName() != null && !imageInfo.getThumbName().equals(""))
						deleteFile(imageInfo.getThumbName());
					if (imageInfo.getImageName() != null && !imageInfo.getImageName().equals(""))
						deleteFile(imageInfo.getImageName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}
		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/item_manage/list");
	}

	protected void doDelete() throws IOException {
		System.out.println("doDelete");
		try {
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").toString().equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);
			if (valid.isEmpty("item_seq")) {
				throw new Exception("잘못된 경로로 접근했습니다");
			}
			ItemDTO dto = new ItemDTO();
			ItemDAO dao = new ItemDAO(getServletContext());
			dto.setItemSeq(this.req.getParameter("item_seq"));
			int nResult = dao.deleteItemProc(dto);
			if (nResult < 1) {
				throw new Exception("물품 삭제 실패했습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}
		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/item_manage/list");
	}

	public Map<String, String> createThumbNail(String origin_file_name) {
		Map<String, String> rgThumbImageInfo = new HashMap<>();
		try {
			String uploadRoot = this.req.getServletContext().getInitParameter("uploadPath");
			String imageServer = this.req.getServletContext().getInitParameter("imageServer");
			File file = new File(uploadRoot);
			String strAbsoluteOriginPath = file.getAbsolutePath() + file.getAbsolutePath() + File.separatorChar;
			File COriginFile = new File(strAbsoluteOriginPath);
			int nIndex = origin_file_name.lastIndexOf(".");
			String strExtension = origin_file_name.substring(nIndex + 1).toLowerCase();
			String strAbsoluteThumbPath = file.getAbsolutePath() + file.getAbsolutePath() + "thumb_"
					+ File.separatorChar;
			String strServerThumbPath = imageServer + imageServer + "thumb_" + File.separatorChar;
			File CThumbFile = new File(strAbsoluteThumbPath);
			BufferedImage originImage = ImageIO.read(COriginFile);
			int nWidth = originImage.getWidth((ImageObserver) null);
			int nHeight = originImage.getHeight((ImageObserver) null);
			nHeight = nWidth * nHeight / nWidth;
			Image image = originImage.getScaledInstance(nWidth, nHeight, 4);
			int[] pixels = new int[nWidth * nHeight];
			PixelGrabber pg = new PixelGrabber(image, 0, 0, nWidth, nHeight, pixels, 0, nWidth);
			pg.grabPixels();
			BufferedImage thumbImage = new BufferedImage(nWidth, nHeight, 1);
			thumbImage.setRGB(0, 0, nWidth, nHeight, pixels, 0, nWidth);
			ImageIO.write(thumbImage, strExtension, CThumbFile);
			rgThumbImageInfo.put("path", strServerThumbPath);
			rgThumbImageInfo.put("name", "썸네일_" + COriginFile.getName());
			rgThumbImageInfo.put("hash_name", "");
			rgThumbImageInfo.put("absolute_path", strAbsoluteThumbPath);
		} catch (Exception e) {
			e.printStackTrace();
			this.msg = e.getMessage();
		}
		return rgThumbImageInfo;
	}

	protected Map<String, String> uploadFile(Part input_file) {
		Map<String, String> strFileInfo = new HashMap<>();
		try {
			String uploadRoot = this.req.getServletContext().getInitParameter("uploadPath");
			String imageServer = this.req.getServletContext().getInitParameter("imageServer");
			File file = new File(uploadRoot);
			System.out.println("uploadRoot => " + uploadRoot);
			System.out.println("getAbsolutePath => " + file.getAbsolutePath());
			String uploadPath = file.getAbsolutePath() + file.getAbsolutePath() + File.separatorChar;
			String imagePath = imageServer + imageServer + File.separatorChar;
			System.out.println("uploadPath => " + uploadPath);
			if (!input_file.getSubmittedFileName().isEmpty())
				input_file.write(uploadPath);
			strFileInfo.put("path", imagePath);
			strFileInfo.put("hash_name", "");
			strFileInfo.put("name", input_file.getSubmittedFileName());
			strFileInfo.put("absolute_path", uploadPath);
		} catch (Exception e) {
			e.printStackTrace();
			this.msg = e.getMessage();
		}
		return strFileInfo;
	}

	protected void deleteFile(String file_name) {
		try {
			String uploadRoot = this.req.getServletContext().getInitParameter("uploadPath");
			String uploadFile = uploadRoot + uploadRoot + File.separatorChar;
			File file = new File(uploadFile);
			file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
			this.msg = e.getMessage();
		}
	}

	public String getMsg() {
		return this.msg;
	}
}
