package jin.spring.board.member.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import jin.spring.board.member.dao.MemberDAO;
import jin.spring.board.member.dto.MemberDTO;

//@Service: Spring에서 비즈니스 로직을 처리하는 서비스 클래스를 나타냄
@Service
public class MemberServiceImp implements MemberService{
//	final 키워드가 붙어 있어 한 번 초기화 후 변경할 수 없음->불변성 보장
	private final MemberDAO memberDAO;
	
//	생성자 주입
	public MemberServiceImp(@Qualifier("memberDAOImp") MemberDAO memberDAO) {
		/*
		 * this.memberDAO: memberDAOImp 클래스의 필드->final 키워드가 붙어 한번만 초기화 가능 
		 * = memberDAO: memberDAOImp 객체를 생성할 때 Spring 컨테이너에서 주입된 매개변수
		 * 여기서 주입되는 빈은 memberDAOImp 클래스가 됨
		 */
		this.memberDAO = memberDAO;
	}
	
//	회원가입
	@Override
	public void memberRegister(MemberDTO memberDTO) throws Exception {
//		memberDAO.register(memberDTO)를 통해 DAO로 전달하여 회원 데이터를 데이터베이스에 삽입
		memberDAO.register(memberDTO);
	}

//	로그인
	@Override
	public MemberDTO memberLogin(MemberDTO memberDTO) throws Exception {
		/*
		 * MemberDTO 객체에 로그인 정보를 담아 memberDAO.login(memberDTO)로 전달 
		 * DAO에서 로그인 쿼리를 실행하고, 해당하는 회원 정보를 반환받아 회원 정보를 MemberDTO로 반환 
		 * 만약 로그인 정보가 맞다면 해당 회원의 정보를 반환하고, 맞지 않으면 null을 반환할 수 있음
		 */
		return memberDAO.login(memberDTO);
	}

//	회원 정보 수정
//	MemberController에서 보내는 파라미터를 memberUpdate(MemberDTO memberDTO)로 받음
	@Override
	public void memberUpdate(MemberDTO memberDTO) throws Exception {
//		MemberDTO 객체에 수정된 회원 정보를 담아 memberDAO.memberUpdate(memberDTO)로 전달
		memberDAO.memberUpdate(memberDTO);
	}

//	회원 탈퇴
//	MemberController에서 보내는 파라미터를 memberDelete(MemberDTO memberDTO)로 받음
	@Override
	public void memberDelete(MemberDTO memberDTO) throws Exception {
//		MemberDTO 객체에 탈퇴할 회원의 정보를 담아 memberDAO.memberDelete(memberDTO)로 전달
		memberDAO.memberDelete(memberDTO);
	}

}
