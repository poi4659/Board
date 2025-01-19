package jin.spring.board.member.service;

import jin.spring.board.member.dto.MemberDTO;

public interface MemberService {
//	회원 가입
	public void memberRegister(MemberDTO memberDTO) throws Exception;
	
//	로그인
	public MemberDTO memberLogin(MemberDTO memberDTO) throws Exception;
	
//	회원 정보 수정
	public void memberUpdate(MemberDTO memberDTO) throws Exception;
	
//	회원 탈퇴
	public void memberDelete(MemberDTO memberDTO) throws Exception;
	
//	패스워드 체크
	public int passCheck(MemberDTO memberDTO) throws Exception;
	
//	아이디 중복 체크
	public int idCheck(MemberDTO memberDTO) throws Exception;
}
