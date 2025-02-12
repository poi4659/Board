package jin.spring.board.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import jin.spring.board.dto.BoardDTO;
import jin.spring.board.service.BoardServiceImp;

//이 클래스가 Spring Bean으로 등록되어 다른 클래스에서 주입(@Autowired)해서 사용할 수 있음
@Component("fileUtils")
public class FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

//	파일이 저장될 로컬 디렉토리 경로(C:\board\file) 지정
	private static final String filePath = "C:\\board\\file\\";

	/*
	 * 매개변수: MultipartHttpServletRequest mpRequest: 업로드된 파일 정보가 들어 있는 Spring의 파일 업로드
	 * 객체
	 * 
	 * 반환값: 파일 정보를 담은 List<Map<String, Object>> 형태로 반환 각 파일에 대한 정보를 Map으로 저장하고, 여러
	 * 개의 파일을 List로 관리
	 */
	public List<Map<String, Object>> parseInsertFileInfo(BoardDTO boardDTO, MultipartHttpServletRequest mpRequest)
			throws Exception {
		/*
		 * 업로드된 파일들의 이름을 Iterator로 가져옴 Iterator는 컬렉션의 항목을 순차적으로 탐색할 수 있는 객체
		 * mpRequest.getFileNames(): 업로드된 파일 이름 가져옴 String 타입의 Iterator: 파일 이름들이 String
		 * 형태로 순차적으로 반환될 수 있도록 만듦
		 */
		Iterator<String> iterator = mpRequest.getFileNames();

//		파일 담을 변수
		MultipartFile multipartFile = null;

//		원본 파일 이름
		String originalFileName = null;

//		파일 확장자
		String originalFileExtension = null;

//		서버에 저장될 파일 이름->랜덤으로 생성
		String storedFileName = null;

		/*
		 * 파일 정보 담을 리스트 여러개의 파일이 업로드될 수 있으므로 각 파일 정보를 맵으로 저장
		 */
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		/*
		 * 개별 파일 정보 저장할 Map 한번에 한개의 파일 정보 저장
		 */
		Map<String, Object> listMap = null;

//		현재 게시글 번호(bnum) 가져오기
		int bnum = boardDTO.getBnum();

//		파일이 저장될 디렉토리 객체 생성
		File file = new File(filePath);

//		파일이 저장될 폴더가 없으면 생성
		if (file.exists() == false) {
			file.mkdirs();
		}

		/*
		 * 업로드된 파일 개수만큼 반복 iterator: 컬렉션(리스트, 셋 등)의 요소를 하나씩 가져올 수 있는 객체 ->데이터를 순차적으로
		 * 탐색하는데 사용 hasNext(): 다음 요소가 있는지 확인하는 메서드
		 */
		while (iterator.hasNext()) {
			/*
			 * 하나씩 가져옴 iterator.next(): 현재 파일의 이름을 반환하고, 다음 파일로 이동 mpRequest.getFile(파일명):
			 * 해당 파일의 데이터를 가져옴 결과적으로 multipartFile에 현재 처리 중인 파일이 저장됨
			 */
			multipartFile = mpRequest.getFile(iterator.next());

//			만약 파일이 비어있지 않다면 처리
			if (multipartFile.isEmpty() == false) {
//	            파일 크기 확인 코드 추가
				long size = multipartFile.getSize();
				System.out.println("File size: " + size); // 파일 크기 출력

				/*
				 * 업로드된 파일의 원본 파일 이름 가져옴 예: 사용자가 "photo.png"라는 파일을 올리면 originalFileName =
				 * "photo.png"
				 */
				originalFileName = multipartFile.getOriginalFilename();

				/*
				 * 파일 확장자(.png, .jpg, .txt 등)만 추출 "photo.png"의 경우,
				 * originalFileName.lastIndexOf(".") -> "photo.png"에서 . 위치 찾음: 5
				 * originalFileName.substring(5) -> ".png" 추출 originalFileExtension = ".png"
				 */
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

				/*
				 * 랜덤한 파일명을 생성하여 서버에 저장 랜덤 문자열+확장자로 서버에 저장
				 */
				storedFileName = getRandomString() + originalFileExtension;

				/*
				 * 저장될 경로 + 랜덤 파일 이름으로 파일 객체 생성 "C:\\board\\file\\" + "a1b2c3d4e5f6g7h8i9j0.png"
				 * file 객체는 "C:\board\file\a1b2c3d4e5f6g7h8i9j0.png"을 가리키게 됨
				 */
				file = new File(filePath + storedFileName);

//				사용자가 업로드한 파일을 서버에 저장
				multipartFile.transferTo(file);

//				파일 정보 저장할 새로운 Map 생성
				listMap = new HashMap<String, Object>();

//				JSP에서 EL이 자동으로 키를 변환하는 문제를 방지하기 위해 대문자로 저장
//				게시물 번호 맵에 저장
				listMap.put("BNUM", bnum);

//				원본 파일 이름 맵에 저장
				listMap.put("ORGFILENAME", originalFileName);

//				서버에 저장된 파일 이름 맵에 저장
				listMap.put("STOREDFILENAME", storedFileName);

//				파일 크기(바이트 단위) 맵에 저장
				listMap.put("MPFILESIZE", size);

				/*
				 * 모든 파일 정보 리스트에 추가 여러개의 파일이 업로드될 경우, 각 파일마다 listMap을 새로 생성하여 list에 저장
				 */
				list.add(listMap);
			}

		}
//		모든 파일이 처리되면 최종적으로 list 반환
		return list;
	}

	/*
	 * 파일 수정 및 삭제
	 * BoardDTO boardDTO: 게시글 정보를 담고 있는 DTO 객체
	 * files: 기존에 존재하는 파일들의 파일 번호를 담고 있는 배열
	 * fileNames: 기존 파일들의 파일 이름
	 * mpRequest: 새로 업로드된 파일들의 정보를 담고 있는 요청 객체
	 * 
	 * 반환값: 파일들의 정보가 담긴 List<Map<String, Object>> 객체
	 */
	public List<Map<String, Object>> parseUpdateFileInfo(BoardDTO boardDTO, String[] files, String[] fileNames,
			MultipartHttpServletRequest mpRequest) throws Exception {
//		업로드된 파일들의 이름을 Iterator로 가져옴->각 파일 순차적으로 처리
		Iterator<String> iterator = mpRequest.getFileNames();

//		파일 담을 변수
		MultipartFile multipartFile = null;

//		원본 파일 이름
		String originalFileName = null;

//		파일 확장자
		String originalFileExtension = null;

//		서버에 저장될 파일 이름->랜덤으로 생성
		String storedFileName = null;

//		최종적으로 반환할 파일 정보 리스트-여러개일 수 있어서 리스트 사용
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

//		개별 파일 정보 저장할 Map 한번에 한개의 파일 정보 저장
		Map<String, Object> listMap = null;

//		현재 게시글 번호(bnum) 가져오기
		int bnum = boardDTO.getBnum();
		logger.info("게시글 번호: {}", boardDTO.getBnum());  // boardDTO의 bnum 값 확인

		while (iterator.hasNext()) {
			/*
			 * iterator.next(): 현재 파일의 이름을 반환하고, 다음 파일로 이동 mpRequest.getFile(파일명): 해당 파일의
			 * 데이터를 가져옴 결과적으로 multipartFile에 현재 처리 중인 파일이 저장됨
			 */
			multipartFile = mpRequest.getFile(iterator.next());

//			만약 파일이 비어있지 않다면 처리
			if (multipartFile.isEmpty() == false) {
//				업로드된 파일의 원본 파일 이름 가져옴
				originalFileName = multipartFile.getOriginalFilename();

//				파일 확장자(.png, .jpg, .txt 등)만 추출 
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

//				랜덤한 파일명을 생성하여 서버에 저장
				storedFileName = getRandomString() + originalFileExtension;

//				저장될 경로 + 랜덤 파일 이름으로 파일 객체 생성
//				사용자가 업로드한 파일을 서버에 저장
				multipartFile.transferTo(new File(filePath + storedFileName));

//				파일 정보 저장할 새로운 Map 생성
				listMap = new HashMap<String, Object>();

//				JSP에서 EL이 자동으로 키를 변환하는 문제를 방지하기 위해 대문자로 저장
//				새로 업로드된 파일은 "IS_NEW" 키에 "Y" 값을 넣어 새로운 파일임을 구분
				listMap.put("IS_NEW", "Y");

//				게시물 번호 맵에 저장
				listMap.put("BNUM", bnum);

//				원본 파일 이름 맵에 저장
				listMap.put("ORGFILENAME", originalFileName);

//				서버에 저장된 파일 이름 맵에 저장
				listMap.put("STOREDFILENAME", storedFileName);

//				파일 크기(바이트 단위) 맵에 저장
				listMap.put("MPFILESIZE", multipartFile.getSize());

				/*
				 * 모든 파일 정보 리스트에 추가 여러개의 파일이 업로드될 경우, 각 파일마다 listMap을 새로 생성하여 list에 저장
				 */
				list.add(listMap);
			}
		}
		
//		기존 파일 처리
//		만약 files와 fileNames가 null이 아니면, 기존에 있던 파일들의 정보를 처리
		if (files != null && fileNames != null) {
			for (int i = 0; i < fileNames.length; i++) {
				listMap = new HashMap<String, Object>();

//				IS_NEW 값을 N으로 설정하여 기존 파일임을 구분
				listMap.put("IS_NEW", "N");

//				기존 파일 번호 맵에 저장
				listMap.put("FILENUM", files[i]);

//				기존 파일 정보 리스트에 추가
				list.add(listMap);
			}
		}

//		모든 파일이 처리되면 최종적으로 list 반환
		return list;
	}

//	랜덤 파일명 생성 메서드
	public static String getRandomString() {
//		UUID.randomUUID().toString(): 랜덤 문자열 생성
//		replace("-", ""): 하이픈(-) 제거해서 순수한 랜덤 문자열 반환
		return UUID.randomUUID().toString().replace("-", "");
	}
}