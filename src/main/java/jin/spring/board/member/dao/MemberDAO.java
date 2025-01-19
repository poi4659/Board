package jin.spring.board.member.dao;

import jin.spring.board.member.dto.MemberDTO;

public interface MemberDAO {
//	회원 가입
	public void register(MemberDTO memberDTO) throws Exception;

//	로그인
	public MemberDTO login(MemberDTO memberDTO) throws Exception;
	
//	회원 정보 수정
	public void memberUpdate(MemberDTO memberDTO) throws Exception;
	
//	회원 탈퇴
	public void memberDelete(MemberDTO memberDTO) throws Exception;
	
//	패스워드 체크
	public int passCheck(MemberDTO memberDTO) throws Exception;
	
//	아이디 중복 체크
	public int idCheck(MemberDTO memberDTO) throws Exception;
}
