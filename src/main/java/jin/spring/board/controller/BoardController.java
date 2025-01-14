package jin.spring.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;import jin.spring.board.dao.BoardDAO;
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
 * @RequiredArgsConstructor: Lombok 어노테이션으로, 
 * final 필드에 대해 자동으로 생성자를 생성 
 * 여기서는 BoardService를 생성자 주입 방식으로 주입
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
	@PostMapping("/BoardInsert")
	public String insert(BoardDTO boardDTO) throws Exception {
		logger.info("insert");
		boardService.boardInsert(boardDTO);
		
		// 절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
	    return "redirect:/BoardList";
	}
	
//	게시글 목록 조회
	@GetMapping("/BoardList")
//	Criteria 객체는 페이지 번호와 페이지당 표시할 데이터 개수를 포함하는 객체
	public String selectAll(Model model, Criteria cri) throws Exception {
		logger.info("list");
		/*
		 * boardService에서 전체 게시글 목록을 가져오는 서비스 메서드 
		 * cri를 파라미터로 전달하여 페이징된 결과를 가져옴
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
		
//		게시글 상세 조회 뷰 반환
		return "./board/board_select_detail";
	}
	
//	게시글 수정 뷰
	@GetMapping("/BoardUpdate")
	public String update(Model model, BoardDTO boardDTO) throws Exception {
		logger.info("update");
		model.addAttribute(boardService.boardSelect(boardDTO.getBnum()));
		
//		게시글 수정 뷰 반환
		return "./board/board_update";
	}
	
//	게시글 수정
	@PostMapping("BoardUpdate")
	public String update(BoardDTO boardDTO) throws Exception {
		boardService.boardUpdate(boardDTO);
		
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
	
}
