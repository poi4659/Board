package jin.spring.member.dao;

import jin.spring.member.dto.MemberDTO;

public interface MemberDAO {
//	회원 가입
	public void register(MemberDTO memberDTO) throws Exception;
}
