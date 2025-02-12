package jin.spring.board.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jin.spring.board.dto.BoardDTO;
import jin.spring.board.dto.Criteria;
import jin.spring.board.service.BoardServiceImp;
import lombok.RequiredArgsConstructor;

/*MyBatis SQL 쿼리 실행 코드를 작성하고, 실제 데이터베이스와 상호작용하는 구현체
 * @Repository: Spring의 컴포넌트 스캔에 의해 이 클래스가 DAO 계층의 Bean으로 등록됨
 * ->"boardDAOImp" 이름을 가진 빈으로 등록
 * */
@Repository
public class BoardDAOImp implements BoardDAO {
	private static Logger logger = LoggerFactory.getLogger(BoardDAOImp.class);

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
	
//	첨부파일 업로드
//	게시글에 첨부된 파일 정보를 데이터베이스에 삽입
	@Override
	public void insertFile(Map<String, Object> map) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="insertFile"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		sqlSessionTemplate.insert("jin.spring.board.insertFile", map);
	}

//	첨부파일 조회
//	파라미터는 bnum
//	여러 개의 파일 정보를 조회할 수 있으므로 List<Map<String, Object>> 타입을 사용
	@Override
	public List<Map<String, Object>> selectFileList(int bnum) throws Exception {
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="selectFileList"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		return sqlSessionTemplate.selectList("jin.spring.board.selectFileList", bnum);
	}

//	첨부파일 다운로드
//	조회된 파일 정보를 Map 형태로 반환
//	파일 정보 조회 (검색 조건을 map으로 받음)
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		// map 값을 로깅하여 확인
	    System.out.println("Received map in selectFileInfo: " + map);
		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="selectFileInfo"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		return sqlSessionTemplate.selectOne("jin.spring.board.selectFileInfo", map);

	}

//	첨부파일 수정
//	메서드 파라미터로 Map을 받음-첨부파일 수정에 필요한 다양한 정보를 담고 있는 객체
	@Override
	public void updateFile(Map<String, Object> map) throws Exception {
		logger.info("Map 내용: {}", map.toString());

		/*
		 * XML 매퍼 파일에서 namespace="jin.spring.board"와 id="selectFileInfo"에 해당하는 
		 * SQL 쿼리를 실행 
		 */
		sqlSessionTemplate.update("jin.spring.board.updateFile", map);
	}

}
