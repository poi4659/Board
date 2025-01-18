package jin.spring.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jin.spring.member.dao.MemberDAO;
import jin.spring.member.dto.MemberDTO;

@Service
public class MemberServiceImp implements MemberService{
	@Autowired
	private MemberDAO memberDAO;
	
	@Override
	public void memberRegister(MemberDTO memberDTO) throws Exception {
		memberDAO.register(memberDTO);
	}

}
