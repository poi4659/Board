package jin.spring.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;
import lombok.RequiredArgsConstructor;

/*MyBatis SQL 쿼리 실행 코드를 작성하고, 실제 데이터베이스와 상호작용하는 구현체
 * @Repository: Spring의 컴포넌트 스캔에 의해 이 클래스가 DAO 계층의 Bean으로 등록됨
 * ->"boardDAOImp" 이름을 가진 빈으로 등록
 * */
@Repository

/*
 * Lombok 어노테이션으로, final로 선언된 필드에 대해 생성자를 자동으로 생성
 * ->sqlSessionTemplate에 대해 생성자가 자동으로 생성됨
 */
@RequiredArgsConstructor
public class BoardDAOImp implements BoardDAO {
	/*
	 * BoardDAOImp 클래스가 Spring 컨텍스트에 의해 생성될 때, 
	 * @Autowired 어노테이션에 의해 Spring은 sqlSessionTemplate Bean을 찾아서 해당 필드에 주입
	 * Spring은 sqlSessionTemplate이 final로 선언되었기 때문에 
	 * 생성자 주입 방식으로 주입을 수행
	 */
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
//	게시글 작성
	@Override
	public void insert(BoardDTO boardDTO) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="insert"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		sqlSessionTemplate.insert("jin.spring.board.insert", boardDTO);
	}
	
//	게시글 목록 조회
//	listPage로 수정
	@Override
	public List<BoardDTO> selectAll(Criteria cri) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="listPage"에 해당하는 
		 * SQL 쿼리를 실행 
		 * 쿼리 결과는 List<BoardDTO> 형태로 반환
		 */
		return sqlSessionTemplate.selectList("jin.spring.board.listPage", cri);
	}

//	게시물 총 개수 코드 추가
	@Override
	public int listCount() throws Exception {
		return sqlSessionTemplate.selectOne("jin.spring.board.listCount");
	}
	
//	게시글 상세 조회
	@Override
	public BoardDTO select(int bnum) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="select"에 해당하는 
		 * SQL 쿼리를 실행 
		 * 쿼리 결과는 BoardDTO 형태로 반환
		 */
		return sqlSessionTemplate.selectOne("jin.spring.board.select", bnum);
	}

//	게시글 수정
	@Override
	public void update(BoardDTO boardDTO) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="update"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		sqlSessionTemplate.update("jin.spring.board.update", boardDTO);
	}

//	게시글 삭제
	@Override
	public void delete(int bnum) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="delete"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		sqlSessionTemplate.delete("jin.spring.board.delete", bnum);
	}

}
