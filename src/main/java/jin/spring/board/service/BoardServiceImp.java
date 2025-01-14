package jin.spring.board.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jin.spring.board.dao.BoardDAO;
import jin.spring.board.dto.BoardDTO;
import lombok.RequiredArgsConstructor;

/* 
 * BoardDAO의 메서드를 호출하여 데이터를 처리하고, 추가적인 비즈니스 로직을 수행하는 구현체
 * BoardService 인터페이스를 구현한 서비스 계층의 클래스
 * 이 클래스는 비즈니스 로직을 처리하며, 데이터베이스 작업은 DAO 계층에 위임함
 * @Service: Spring의 서비스 컴포넌트를 나타냄
*/
@Service
public class BoardServiceImp implements BoardService{
	/*
	 * BoardDAO가 BoardServiceImp 클래스의 생성자를 통해 주입
	 * final은 해당 필드가 한 번만 초기화될 수 있도록 강제
	 * final로 선언하면, Spring이 생성자 주입을 통해 객체를 주입할 때만 초기화가 이루어지며, 
	 * 이 후에는 해당 필드를 변경할 수 없음
	 */
	private final BoardDAO boardDAO;
	
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
	public void boardInsert(BoardDTO boardDTO) throws Exception{
		try {
//			게시글 등록
			boardDAO.insert(boardDTO);
		} catch (DataIntegrityViolationException e) {
//			무결성 제약조건 위반 시 발생
//			예외가 발생하면 printStackTrace()를 통해 예외의 정보를 콘솔에 출력
			e.printStackTrace();
		}
	}
	
//	게시글 목록 조회
	@Override
	public List<BoardDTO> boardSelectAll() throws Exception {
//		게시글 목록 조회
		return boardDAO.selectAll();
	}

//	게시글 상세 조회
	@Override
	public BoardDTO boardSelect(int bnum) throws Exception {
//		게시글 상세 조회
		return boardDAO.select(bnum);
	}

//	게시글 수정
	@Override
	public void boardUpdate(BoardDTO boardDTO) throws Exception {
//		게시글 수정
		boardDAO.update(boardDTO);
	}

//	게시글 삭제
	@Override
	public void boardDelete(int bnum) throws Exception {
//		게시글 삭제
		boardDAO.delete(bnum);
	}

}
