package jin.spring.board.dao;

import java.util.List;

import jin.spring.board.dto.BoardDTO;

public interface BoardDAO {
//	게시글 작성
	public void insert(BoardDTO boardDTO) throws Exception;
	
//	게시글 목록 조회
	public List<BoardDTO> selectAll() throws Exception;
	
//	게시글 상세 조회
	public BoardDTO select(int bnum) throws Exception;
	
//	게시글 수정
	public void update(BoardDTO boardDTO) throws Exception;
	
//	게시글 삭제
	public void delete(int bnum) throws Exception;
}
