package jin.spring.member.service;

import jin.spring.member.dto.MemberDTO;

public interface MemberService {
//	회원 가입
	public void memberRegister(MemberDTO memberDTO) throws Exception;
}
