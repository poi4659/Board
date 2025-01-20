package jin.spring.board.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jin.spring.board.member.dto.MemberDTO;
import jin.spring.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
//final로 선언된 필드에 대해 생성자를 자동으로 생성해줌
@RequiredArgsConstructor
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private final MemberService memberService;

	@Autowired
	BCryptPasswordEncoder pwdEncoder;

//	회원 가입 뷰
	@GetMapping("/MemberRegister")
	public String register() throws Exception {
		logger.info("회원 가입 뷰");
//		회원 가입 뷰 반환
		return "./member/member_register";
	}

//	회원 가입
	@PostMapping("/MemberRegister")
	public String register(MemberDTO memberDTO) throws Exception {
		logger.info("회원 가입");

//		아이디 중복 체크 추가
//		아이디 중복 여부를 체크하는 서비스 메서드를 호출
//		idCheck 메서드는 아이디가 이미 존재하면 1을 반환하고, 존재하지 않으면 0을 반환하는 방식으로 동작
		int result = memberService.idCheck(memberDTO);
		try {
//			아이디 조회된게 있음->중복임
			if (result == 1) {
				return "redirect:/MemberRegister";
			} else if (result == 0) {
//				memberDTO 객체에서 사용자가 입력한 원본 비밀번호를 가져옴
				String inputPass = memberDTO.getMemberPW();

//				encode 메서드를 호출하여 입력된 비밀번호(inputPass)를 암호화
				String pwd = pwdEncoder.encode(inputPass);

//				암호화된 비밀번호(pwd)를 memberDTO 객체의 memberPW 필드에 다시 설정
				memberDTO.setMemberPW(pwd);

//				중복된 게 없으면 회원가입 실행
//				암호화된 비밀번호를 포함한 memberDTO 객체가 데이터베이스에 저장
				memberService.memberRegister(memberDTO);
			}
		} catch (Exception e) {
//			예외 발생 시 로그 출력
			logger.error("회원가입 중 오류 발생: ", e);
//	        자세한 예외 정보 제공
			throw new RuntimeException("회원가입 처리 중 문제가 발생했습니다.", e);
		}
//		실행 완료되면 로그인페이지로 리다이렉트
//		절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/MemberLogin";
	}

//	로그인 뷰
	@GetMapping("/MemberLogin")
	public String login() throws Exception {
		logger.info("로그인 뷰");
//		로그인 뷰 반환
		return "./member/member_login";
	}

	/*
	 * 로그인 HttpServletRequest req: HTTP 요청 객체, 세션을 가져오거나 요청 파라미터 처리 가능
	 * RedirectAttributes rttr: 리다이렉트 후에 사용자에게 메시지나 데이터를 전달할 수 있는 객체
	 */
	@PostMapping("/MemberLogin")
	public String login(MemberDTO memberDTO, HttpSession session, RedirectAttributes rttr) throws Exception {
		logger.info("로그인");

		session.getAttribute("member");

		/*
		 * memberService.memberLogin(memberDTO)가 호출되면서 데이터베이스에서 사용자가 입력한 아이디와 비밀번호에 해당하는
		 * 사용자가 있는지 확인 이 메서드는 로그인 정보가 맞으면 해당 사용자의 MemberDTO 객체를 반환하고, 맞지 않으면 null을 반환
		 */
		MemberDTO login = memberService.memberLogin(memberDTO);

		boolean pwdMatch = pwdEncoder.matches(memberDTO.getMemberPW(), login.getMemberPW());

//		로그인 실패 처리
//		만약 login이 null이면, 입력한 아이디와 비밀번호에 해당하는 사용자가 없다는 것
//		로그인 정보가 맞지 않거나 해당 사용자가 없으면 login은 null
		if (login != null && pwdMatch == true) {
			logger.info("로그인 성공");
//			로그인 성공 시, 세션에 로그인한 사용자의 정보를 member라는 이름으로 저장
			session.setAttribute("member", login);

			logger.info("세션에 저장된 사용자 정보: {}", session.getAttribute("member"));

//			절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
//			로그인 처리 끝난 후 BoardList 페이지로 리다이렉트
			return "redirect:/BoardList";
		} else {
			logger.info("로그인 실패");
//			로그인 실패 시, 세션에 저장된 사용자 정보를 null로 설정->로그인이 되어 있지 않은 상태로 처리됨
			session.setAttribute("member", null);

//			RedirectAttributes에 msg라는 속성을 추가하여 로그인 실패 메시지를 전달
			rttr.addFlashAttribute("msg", false);

//			로그인 실패 시 로그인 페이지로 리다이렉트
			return "redirect:/MemberLogin";

		}
	}

//	로그아웃
	@GetMapping("/MemberLogout")
	public String logout(HttpSession session) throws Exception {
//		세션을 무효화하여 모든 세션 데이터를 삭제
		session.invalidate();

		logger.info("로그아웃");

//		절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}

//	회원 마이페이지 뷰
	@GetMapping("/MemberMypage")
	public String mypage() {
		logger.info("회원 마이페이지 뷰");
//		회원 정보 수정 뷰 반환
		return "./member/member_mypage";
	}

//	회원 정보 수정 뷰
	@GetMapping("/MemberUpdate")
	public String update() {
		logger.info("회원 정보 수정 뷰");
//		회원 정보 수정 뷰 반환
		return "./member/member_update";
	}

//	회원 정보 수정
	@PostMapping("/MemberUpdate")
	public String update(MemberDTO memberDTO, HttpSession session, HttpServletRequest request, RedirectAttributes rttr)
	        throws Exception {
		logger.info("회원 정보 수정");

	    // 세션에서 회원 정보 가져오기
	    MemberDTO sessionMember = (MemberDTO) session.getAttribute("member");
	    if (sessionMember == null) {
//	    	세션 정보가 없으면 로그인 페이지로 리다이렉트
	    	logger.error("세션 정보가 없습니다.");
	        return "redirect:/MemberLogin";
	    }

	    // 데이터베이스의 비밀번호 가져오기
	    String encodedPassword = sessionMember.getMemberPW();
	    if (encodedPassword == null) {
	        logger.error("저장된 비밀번호가 없습니다.");
//	        비밀번호가 없으면 메시지 추가
	        rttr.addFlashAttribute("msg", "noPassword");
	        
//	        다시 회원 정보 수정 페이지로 리다이렉트
	        return "redirect:/MemberUpdate";
	    }

		/*
		 * 현재 입력된 비밀번호와 세션에 저장된 비밀번호(암호화된 비밀번호) 비교 
		 * pwdEncoder.matches(rawPassword, encodedPassword)에서 
		 * rawPassword는 암호화하기 전의 평문 비밀번호이어야 하고, 
		 * encodedPassword는 이미 암호화된 비밀번호여야 함
		 * rawPassword를 암호화하면, 해당 값은 matches() 메서드에서 비교할 수 없으며, 
		 * matches() 메서드는 평문 비밀번호를 필요로 하므로 결과적으로 null이나 비교 오류가 발생
		 */
	    boolean pwdMatch = pwdEncoder.matches(memberDTO.getMemberPW(), encodedPassword);
	    if (pwdMatch) {
//	    	비밀번호가 일치하는 경우
	        logger.info("현재 비밀번호 일치");
	        
//	        새 비밀번호 가져오기
	    	String newPassword = request.getParameter("newMemberPW");
	    	
//	    	새 비밀번호 암호화
	        String newEncodedPassword = pwdEncoder.encode(newPassword);
	        
//	        암호화된 새 비밀번호를 DTO에 설정
	        memberDTO.setMemberPW(newEncodedPassword);

//	        회원 정보 업데이트
	        memberService.memberUpdate(memberDTO);
	        
	        logger.info("비밀번호 수정 성공");
	        
//	        세션 무효화 (사용자가 다시 로그인하도록 유도)
	        session.invalidate();

//	        로그인 페이지로 리다이렉트
	        return "redirect:/MemberLogin";
	    } else {
	        logger.info("현재 비밀번호 불일치");
//	        비밀번호 불일치 메시지 추가
	        rttr.addFlashAttribute("msg", false);
	        
//	        회원 정보 수정 페이지로 리다이렉트
	        return "redirect:/MemberUpdate";
	    }
	}

//	회원 탈퇴 뷰
	@GetMapping("/MemberDelete")
	public String delete() {
		logger.info("회원 탈퇴 뷰");
//		회원 정보 수정 뷰 반환
		return "./member/member_delete";
	}

//	회원 탈퇴
	@PostMapping("/MemberDelete")
	public String delete(MemberDTO memberDTO, HttpSession session, RedirectAttributes rttr) throws Exception {
		logger.info("회원 탈퇴");

//		세션에 저장되어 있는 member 가져와서 member에 넣어줌
		MemberDTO member = (MemberDTO) session.getAttribute("member");

//		세션에 저장되어 있는 비밀번호
		String sessionPW = member.getMemberPW();

//		MemberDTO로 들어오는 비밀번호
		String memberDTOPW = memberDTO.getMemberPW();

//		만약 세션에 저장되어 있는 비밀번호와 DTO에 들어오는 비밀번호가 일치하지 않으면
		if (!(sessionPW.equals(memberDTOPW))) {
//			msg에 false값을 넣어 member_delete에 보내줌
			rttr.addFlashAttribute("msg", false);
//			redirect를 해야 플래시 속성이 전달됨
			return "redirect:/MemberDelete";
		}

//		삭제 데이터 확인
		logger.info("memberDelete 요청 데이터: {}", memberDTO);

//		회원 탈퇴
		memberService.memberDelete(memberDTO);

//		회원 탈퇴 성공 메시지 전달
		rttr.addFlashAttribute("msg", "success");

//		세션을 무효화하여 모든 세션 데이터를 삭제
		session.invalidate();

//		절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}

	/*
	 * 패스워드 체크
	 * 
	 * @ResponseBody: 메서드가 반환하는 값을 HTTP 응답 본문에 직접 작성할 수 있도록 해줌 여기서는 int 타입을 반환하고, 이
	 * 값은 클라이언트에 JSON 형식으로 전달
	 */
	@ResponseBody
//	POST 방식으로 /MemberPassCheck 경로에 요청이 들어올 때 이 메서드가 실행되도록 함
//	MemberDTO memberDTO: 클라이언트에서 보낸 데이터가 MemberDTO 객체로 변환되어 메서드의 파라미터로 전달됨
	@PostMapping("/MemberPassCheck")
	public boolean passCheck(MemberDTO memberDTO) throws Exception {
		MemberDTO login = memberService.memberLogin(memberDTO);

		boolean pwdChk = pwdEncoder.matches(memberDTO.getMemberPW(), login.getMemberPW());

		return pwdChk;
	}

	/*
	 * 아이디 중복 체크
	 * 
	 * @ResponseBody: 메서드가 반환하는 값을 HTTP 응답 본문에 직접 작성할 수 있도록 해줌 여기서는 int 타입을 반환하고, 이
	 * 값은 클라이언트에 JSON 형식으로 전달
	 */
	@ResponseBody
//	POST 방식으로 /MemberIdCheck 경로에 요청이 들어올 때 이 메서드가 실행되도록 함
//	MemberDTO memberDTO: 클라이언트에서 보낸 데이터가 MemberDTO 객체로 변환되어 메서드의 파라미터로 전달됨
	@PostMapping("/MemberIdCheck")
	public int idCheck(MemberDTO memberDTO) throws Exception {
		int result = memberService.idCheck(memberDTO);
		return result;
	}

}
