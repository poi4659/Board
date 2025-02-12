package jin.spring.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import jin.spring.board.dao.BoardDAO;
import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;
import jin.spring.board.dto.PageMaker;
import jin.spring.board.service.BoardService;
import lombok.RequiredArgsConstructor;

/*
 * @Controller: 이 클래스는 Spring MVC의 컨트롤러로, HTTP 요청을 처리하는 역할
 * Spring이 이 클래스를 관리하는 빈으로 인식
*/
@Controller

/*
 * @RequiredArgsConstructor: Lombok 어노테이션으로, final 필드에 대해 자동으로 생성자를 생성 여기서는
 * BoardService를 생성자 주입 방식으로 주입
 */
@RequiredArgsConstructor
public class BoardController {
	private static Logger logger = LoggerFactory.getLogger(BoardController.class);

//	서비스 계층(BoardService)을 의존성 주입받음
	@Autowired
	private final BoardService boardService;

//	게시글 작성 뷰
	@GetMapping("/BoardInsert")
	public String insert() {
//		게시글 작성 뷰 반환
		return "./board/board_insert";
	}

//	게시판 글 작성
//	첨부파일의 파라미터값을 받을수 있는 MultipartHttpServlertRequest mpRequest를 추가
	@PostMapping("/BoardInsert")
	public String insert(BoardDTO boardDTO, MultipartHttpServletRequest mpRequest) throws Exception {
		logger.info("insert");

//		서비스 실행 시 받는 파라미터에도 mpRequest 추가
		boardService.boardInsert(boardDTO, mpRequest);

		// 절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}

//	게시글 목록 조회
	@GetMapping("/BoardList")
//	Criteria 객체는 페이지 번호와 페이지당 표시할 데이터 개수를 포함하는 객체
	public String selectAll(Model model, Criteria cri) throws Exception {
		logger.info("list");
		/*
		 * boardService에서 전체 게시글 목록을 가져오는 서비스 메서드 cri를 파라미터로 전달하여 페이징된 결과를 가져옴
		 */
		model.addAttribute("list", boardService.boardSelectAll(cri));

//		페이징 처리 코드 추가
//		PageMaker는 페이징 처리를 담당하는 객체로, 전체 페이지의 시작 페이지, 끝 페이지 등을 계산
		PageMaker pageMaker = new PageMaker();

//		cri 객체를 PageMaker에 설정하여, 현재 페이지와 페이지당 표시할 데이터 개수를 사용하도록 함
		pageMaker.setCri(cri);

//		boardService.listCount()를 호출하여 전체 게시글의 수를 가져오고, 
//		이를 PageMaker에 설정하여 전체 페이지 수를 계산할 수 있게 함
		pageMaker.setTotalCount(boardService.listCount());

		model.addAttribute("pageMaker", pageMaker);

//		게시글 목록 뷰 반환
		return "./board/board_select";
	}

//	게시글 상세 조회
	@GetMapping("/BoardSelectDetail")
	public String select(Model model, BoardDTO boardDTO) throws Exception {
		logger.info("select");
//		게시글 번호로 DB에서 조회한 값을 boardDTO란 이름으로 뷰에 전달
		model.addAttribute("boardDTO", boardService.boardSelect(boardDTO.getBnum()));

//		첨부파일 조회
		List<Map<String, Object>> fileList = boardService.boardSelectFileList(boardDTO.getBnum());

//		조회된 파일 정보 출력
		for (Map<String, Object> file : fileList) {
			System.out.println("파일 정보: " + file);
		}

//	    디버깅용 코드 추가
		logger.info("파일 리스트 크기: " + fileList.size());

		model.addAttribute("fileList", fileList);

//		게시글 상세 조회 뷰 반환
		return "./board/board_select_detail";
	}

//	게시글 수정 뷰
	@GetMapping("/BoardUpdate")
	public String update(Model model, BoardDTO boardDTO) throws Exception {
		logger.info("update");
		model.addAttribute(boardService.boardSelect(boardDTO.getBnum()));

//		수정 뷰에 파일 리스트 보이게 추가
		List<Map<String, Object>> fileList = boardService.boardSelectFileList(boardDTO.getBnum());
		model.addAttribute("fileList", fileList);
		
//		게시글 수정 뷰 반환
		return "./board/board_update";
	}

//	게시글 수정
//	files: 사용자가 삭제할 파일들의 파일 번호를 담은 배열
//	fileNames: 사용자가 삭제할 파일들의 파일 이름을 담은 배열
//	mpRequest: 파일 업로드 시 파일 데이터를 담은 요청 객체
	@PostMapping("BoardUpdate")
	public String update(BoardDTO boardDTO,
						@RequestParam(value = "fileNumDel[]") String[] files,
						@RequestParam(value = "fileNameDel[]") String[] fileNames,
						MultipartHttpServletRequest mpRequest) throws Exception {
		 // 파일 번호 배열 출력
	    System.out.println("fileNumDel 배열: ");
	    for (String file : files) {
	        System.out.println(file);
	    }

	    // 파일 이름 배열 출력
	    System.out.println("fileNameDel 배열: ");
	    for (String fileName : fileNames) {
	        System.out.println(fileName);
	    }
		
//		서비스 계층의 boardUpdate 메서드를 호출하여 게시글 수정 작업을 처리
		boardService.boardUpdate(boardDTO, files, fileNames, mpRequest);
		
		// 절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}

//	게시글 삭제 뷰
	@GetMapping("/BoardDelete")
	public String delete(Model model, BoardDTO boardDTO) throws Exception {
		logger.info("delete");
		model.addAttribute(boardService.boardSelect(boardDTO.getBnum()));

//		게시글 삭제 뷰 반환
		return "./board/board_delete";
	}

//	게시글 삭제
	@PostMapping("BoardDelete")
	public String delete(BoardDTO boardDTO) throws Exception {
		boardService.boardDelete(boardDTO.getBnum());

		// 절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}

	/*
	 * @RequestMapping: HTTP 요청 URL에 /BoardFileDown이 들어올 때 fileDown 메서드가 실행됨, GET,
	 * POST 등 모든 HTTP 메서드처리 가능
	 * 
	 * @RequestParam: 클라이언트에서 보내는 요청 파라미터를 Map<String, Object> 형태로 받음
	 */
	@RequestMapping("BoardFileDown")
	public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
//		FILENUM이 존재하면 Integer로 변환
	    if (map.get("FILENUM") != null) {
	        Integer fileNum = Integer.parseInt(map.get("FILENUM").toString());
	        map.put("FILENUM", fileNum);
	    }
	    
		logger.info("map: " + map); // map에 전달된 값 확인

//		boardService.boardSelectFileInfo(map) 호출을 통해 파일 정보를 DB에서 조회
		Map<String, Object> resultMap = boardService.boardSelectFileInfo(map);

		if (resultMap == null || resultMap.isEmpty()) {
			logger.error("No file found for FILENUM: " + map.get("FILENUM"));
			return; // 파일이 없으면 종료
		}
		logger.info("resultMap: " + resultMap);

//		resultMap에서 STOREDFILENAME 값을 꺼내 storedFileName에 저장-서버에 저장된 파일의 이름
		String storedFileName = (String) resultMap.get("STOREDFILENAME");

//		resultMap에서 ORGFILENAME 값을 꺼내 orgFileName에 저장-업로드한 원본 파일 이름
		String orgFileName = (String) resultMap.get("ORGFILENAME");

//		storedFileName에 해당하는 파일을 서버에서 읽어들여 byte[] 배열로 변환->실제로 다운로드될 파일 데이터
		byte fileByte[] = FileUtils.readFileToByteArray(new File("C:\\board\\file\\" + storedFileName));

//		Content-Type을 application/octet-stream으로 설정하여, 응답이 바이너리 파일임을 클라이언트에 알림
		response.setContentType("application/octet-stream");

//		다운로드할 파일의 크기를 응답 헤더에 설정
		response.setContentLength(fileByte.length);

//		Content-Disposition 헤더를 attachment로 설정하여, 브라우저가 파일을 다운로드하도록 유도
//		파일 이름이 한글이거나 특수문자가 포함되어 있을 때 인코딩하여, 파일 이름이 깨지지 않도록 함
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(orgFileName));

//		파일 데이터를 응답의 출력 스트림으로 전송
		response.getOutputStream().write(fileByte);

//		응답 스트림을 강제로 플러시(전송)-버퍼에 담긴 데이터를 즉시 전송하는 역할
		response.getOutputStream().flush();

//		파일 전송이 끝난 후 출력 스트림을 닫음
		response.getOutputStream().close();
	}
}
