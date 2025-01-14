package jin.spring.board.service;

import java.util.List;

import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;

//인터페이스의 메서드 접근 제한자는 기본적으로 public
//따라서 public을 생략해도 동일하게 처리되며, 코드가 간결해짐
//서비스 인터페이스의 구현 클래스에서는 해당 메서드를 public으로 구현해야 함
public interface BoardService {
//	게시글 작성
	void boardInsert(BoardDTO boardDTO) throws Exception;
	
//	게시글 목록 조회
	List<BoardDTO> boardSelectAll(Criteria cri) throws Exception;
	
//	게시물 총 개수
	int listCount() throws Exception;
	
//	게시글 상세 조회
	BoardDTO boardSelect(int bnum) throws Exception;
	
//	게시글 수정
	void boardUpdate(BoardDTO boardDTO) throws Exception;
	
//	게시글 삭제
	void boardDelete(int bnum) throws Exception;
}
