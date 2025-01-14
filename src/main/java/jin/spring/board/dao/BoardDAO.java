package jin.spring.board.dao;

import java.util.List;

import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;

public interface BoardDAO {
//	게시글 작성
	public void insert(BoardDTO boardDTO) throws Exception;
	
//	게시글 목록 조회
	public List<BoardDTO> selectAll(Criteria cri) throws Exception;
	
//	게시물 총 개수
	public int listCount() throws Exception;
	
//	게시글 상세 조회
	public BoardDTO select(int bnum) throws Exception;
	
//	게시글 수정
	public void update(BoardDTO boardDTO) throws Exception;
	
//	게시글 삭제
	public void delete(int bnum) throws Exception;
}
