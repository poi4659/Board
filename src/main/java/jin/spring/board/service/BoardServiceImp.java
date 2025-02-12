package jin.spring.board.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jin.spring.board.controller.BoardController;
import jin.spring.board.dao.BoardDAO;
import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;
import jin.spring.board.file.FileUtils;
import lombok.RequiredArgsConstructor;

/* 
 * BoardDAO의 메서드를 호출하여 데이터를 처리하고, 추가적인 비즈니스 로직을 수행하는 구현체
 * BoardService 인터페이스를 구현한 서비스 계층의 클래스
 * 이 클래스는 비즈니스 로직을 처리하며, 데이터베이스 작업은 DAO 계층에 위임함
 * @Service: Spring의 서비스 컴포넌트를 나타냄
*/
@Service
public class BoardServiceImp implements BoardService{
	private static Logger logger = LoggerFactory.getLogger(BoardServiceImp.class);

	/*
	 * BoardDAO가 BoardServiceImp 클래스의 생성자를 통해 주입
	 * final은 해당 필드가 한 번만 초기화될 수 있도록 강제
	 * final로 선언하면, Spring이 생성자 주입을 통해 객체를 주입할 때만 초기화가 이루어지며, 
	 * 이 후에는 해당 필드를 변경할 수 없음
	 */
	private final BoardDAO boardDAO;
	
//	FileUtils 타입의 빈을 찾아서 fileUtils 변수에 주입
	@Autowired
	private FileUtils fileUtils;
	
	/*
	 * 생성자 주입 방식
	 * BoardServiceImp 클래스의 생성자에 BoardDAO 타입의 객체를 주입 
	 * ->Spring은 BoardDAO 타입의 구현체를 자동으로 찾아 주입
	 * 왜 주입? 서비스 계층에서 DAO 계층의 메서드를 호출하여 비즈니스 로직을 처리하기 위해
	 * @Qualifier: 여러 개의 BoardDAO 빈이 있을 때 특정한 빈을 주입하도록 지정하는 역할
	 */	
	@Autowired
	public BoardServiceImp(@Qualifier("boardDAOImp") BoardDAO boardDAO) {
		/*
		 * this.boardDAO: BoardServiceImp 클래스의 필드->final 키워드가 붙어 한번만 초기화 가능 
		 * = boardDAO: BoardServiceImp 객체를 생성할 때 Spring 컨테이너에서 주입된 매개변수
		 * boardDAO 매개변수는 Spring 컨테이너에서 찾은 boardDAOImp 빈을 의미
		 * 즉, boardDAO는 Spring이 관리하는 BoardDAO 타입의 빈
		 * 여기서 주입되는 빈은 BoardDAOImp 클래스가 됨
		 */
		this.boardDAO = boardDAO;
	}
	
//	게시글 작성
	@Override
	public void boardInsert(BoardDTO boardDTO, MultipartHttpServletRequest mpRequest) throws Exception{
		try {
//			게시글 등록
			boardDAO.insert(boardDTO);
			
			 // 게시글 번호 확인 로그 추가
			logger.info("boardDTO.getBnum() 값 확인: {}", boardDTO.getBnum());
			
//			fileUtils는 @Autowired로 주입된 FileUtils 클래스의 인스턴스
//			parseInsertFileInfo(): MultipartHttpServletRequest에서 파일을 추출하여 List<Map<String, Object>> 형태로 반환
			List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(boardDTO, mpRequest);
			
//			list.size()는 업로드된 파일의 개수를 구함
			int size = list.size();
			
			for (int i = 0; i < size; i++) {
//				list.get(i): 각 파일에 대한 정보를 담고 있는 Map<String, Object> 객체
//				각 파일의 정보를 데이터베이스에 삽입
				boardDAO.insertFile(list.get(i));
			}
		} catch (DataIntegrityViolationException e) {
//			무결성 제약조건 위반 시 발생
//			예외가 발생하면 printStackTrace()를 통해 예외의 정보를 콘솔에 출력
			e.printStackTrace();
		}
	}
	
//	게시글 목록 조회
	@Override
	public List<BoardDTO> boardSelectAll(Criteria cri) throws Exception {
//		게시글 목록 조회
		return boardDAO.selectAll(cri);
	}

//	게시물 총 개수
	@Override
	public int listCount() throws Exception {
		return boardDAO.listCount();
	}
	
//	게시글 상세 조회
	@Override
	public BoardDTO boardSelect(int bnum) throws Exception {
//		게시글 상세 조회
		return boardDAO.select(bnum);
	}

	/*
	 * 게시글 수정
	 * BoardDTO boardDTO: 게시글의 정보를 담고 있는 DTO 객체
	 * String[] files: 기존에 이미 업로드된 파일들의 파일 번호를 담고 있는 배열
	 * String[] fileNames: 기존에 이미 업로드된 파일들의 파일 이름을 담고 있는 배열
	 * MultipartHttpServletRequest mpRequest: 새로운 파일들이 업로드된 요청 객체
	 */
	@Override
	public void boardUpdate(BoardDTO boardDTO,
							String[] files, 
							String[] fileNames,
							MultipartHttpServletRequest mpRequest) throws Exception {
		 // 게시글 정보 출력
	    logger.info("게시글 정보: {}", boardDTO);

	    // 게시글 수정 처리
	    boardDAO.update(boardDTO);

	    // 파일 정보 출력
	    logger.info("기존 파일 번호: {}", Arrays.toString(files));
	    logger.info("기존 파일 이름: {}", Arrays.toString(fileNames));

	    // 새로 업로드된 파일 처리
	    logger.info("새로운 파일들: {}", mpRequest.getFileMap().keySet());
	    
//		게시글 수정 처리
		boardDAO.update(boardDTO);
		
//		첨부파일 수정 코드 추가
//		parseUpdateFileInfo 메서드를 호출하여 파일 수정 및 추가 작업 처리
//		반환되는 리스트는 각 파일의 정보를 Map으로 저장
//		"IS_NEW" 키가 "Y"이면 새로운 파일을, "N"이면 기존 파일을 의미
		List<Map<String, Object>> list = fileUtils.parseUpdateFileInfo(boardDTO, files, fileNames, mpRequest);
	
		Map<String, Object> tempMap = null;
		
		int size = list.size();
		logger.info("파일 목록 크기: {}", size);
		
//		리스트에 담긴 파일 정보들을 순차적으로 처리
		for (int i = 0; i < size; i++) {
			tempMap = list.get(i);
	        logger.info("현재 처리 중인 파일 정보: {}", tempMap);
			
//			"IS_NEW" 값이 "Y"이면 새로운 파일
			if (tempMap.get("IS_NEW").equals("Y")) {
				logger.info("새로운 파일 삽입 tempMap: {}", tempMap);
//				새로운 파일일 경우, boardDAO.insertFile(tempMap)을 호출하여 파일을 데이터베이스에 삽입
				boardDAO.insertFile(tempMap);
			} else {
				logger.info("기존 파일 업데이트 tempMap: {}", tempMap);
//				기존 파일일 경우 boardDAO.updateFile(tempMap)을 호출하여 기존 파일을 업데이트
				tempMap.put("DELGB", "Y");  // 삭제 상태로 설정
				boardDAO.updateFile(tempMap);
			}
		}
	}

//	게시글 삭제
	@Override
	public void boardDelete(int bnum) throws Exception {
//		게시글 삭제
		boardDAO.delete(bnum);
	}

//	첨부파일 조회
//	반환값: List<Map<String, Object>>
//	여러 개의 파일 정보를 조회할 수 있으므로 리스트(List) 형태로 반환
	@Override
	public List<Map<String, Object>> boardSelectFileList(int bnum) throws Exception {
		/*
		 * boardDAO 객체의 selectFileList(bnum) 메서드를 호출하여 
		 * 실제로 데이터베이스에서 데이터를 가져옴
		 */
		return boardDAO.selectFileList(bnum);
	}

//	첨부파일 다운로드
//	반환값: Map<String, Object>->조회된 파일 정보를 Map 형태로 반환
	@Override
	public Map<String, Object> boardSelectFileInfo(Map<String, Object> map) throws Exception {
		// map 값을 로깅하여 확인
	    System.out.println("Received map in boardSelectFileInfo: " + map);
	    
		return boardDAO.selectFileInfo(map);
	}

}
