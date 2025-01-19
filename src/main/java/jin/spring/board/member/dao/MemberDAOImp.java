package jin.spring.board.member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jin.spring.board.member.dto.MemberDTO;

//@Repository: Spring의 컴포넌트 스캔에 의해 이 클래스가 DAO 계층의 Bean으로 등록됨
//->"memberDAOImp" 이름을 가진 빈으로 등록
@Repository
public class MemberDAOImp implements MemberDAO{
	/*
	 * MemberDAOImp 클래스가 Spring 컨텍스트에 의해 생성될 때, 
	 * @Autowired 어노테이션에 의해 Spring은 sqlSessionTemplate Bean을 찾아서 해당 필드에 주입
	 * @Autowired: 자동으로 생성자 주입 방식이 사용됨
	 */
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
//	회원가입
	@Override
	public void register(MemberDTO memberDTO) throws Exception {
//		memberDTO에 담긴 파라미터들을 namespace="jin.spring.member"와 
//		id="register"에 해당하는 쿼리에 파라미터들을 넣어줌
		sqlSessionTemplate.insert("jin.spring.member.register", memberDTO);
	}

//	로그인
//	파라미터로 받은 MemberDTO 객체의 로그인 정보를 기준으로 selectOne 메서드를 사용하여 하나의 결과를 조회
	@Override
	public MemberDTO login(MemberDTO memberDTO) throws Exception {
//		memberDTO에 담긴 파라미터들을 namespace="jin.spring.member"와 
//		id="login"에 해당하는 SQL문을 실행하여 로그인 정보를 확인하고, 해당하는 회원 정보가 있으면 MemberDTO 객체로 반환
		return sqlSessionTemplate.selectOne("jin.spring.member.login", memberDTO);
	}

//	회원 정보 수정
	@Override
	public void memberUpdate(MemberDTO memberDTO) throws Exception {
//		memberDTO에 담긴 파라미터들을 namespace="jin.spring.member"와 
//		id="memberUpdate"에 해당하는 쿼리에 파라미터들을 넣어줌
		sqlSessionTemplate.update("jin.spring.member.memberUpdate", memberDTO);
	}

//	회원 탈퇴
	@Override
	public void memberDelete(MemberDTO memberDTO) throws Exception {
//		memberDTO에 담긴 파라미터들을 namespace="jin.spring.member"와 
//		id="memberDelete"에 해당하는 쿼리에 파라미터들을 넣어줌
		sqlSessionTemplate.delete("jin.spring.member.memberDelete", memberDTO);
	}

//	패스워드 체크
	@Override
	public int passCheck(MemberDTO memberDTO) throws Exception {
//		쿼리에서 조회한 값과 파라미터를 보내주는 값이 숫자이기 때문에 타입은 int형으로 선언해주고
//		MemberService에서 전달받은 파라미터를 memberMapper.xml에 보냄
		int result = sqlSessionTemplate.selectOne("jin.spring.member.passCheck", memberDTO);
		return result;
	}

}
