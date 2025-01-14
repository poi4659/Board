package jin.spring.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;import jin.spring.board.dao.BoardDAO;
import jin.spring.board.dto.BoardDTO;
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
	public String selectAll(Model model) throws Exception {
		logger.info("list");
		model.addAttribute("list", boardService.boardSelectAll());
		
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
