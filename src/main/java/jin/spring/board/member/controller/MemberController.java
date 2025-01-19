package jin.spring.board.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jin.spring.board.member.dto.MemberDTO;
import jin.spring.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private final MemberService memberService;

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
		memberService.memberRegister(memberDTO);

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
	public String login(MemberDTO memberDTO, HttpServletRequest req, RedirectAttributes rttr) throws Exception {
		logger.info("로그인");

//		현재 HTTP 요청에서 세션 객체를 가져옴
		HttpSession session = req.getSession();

		/*
		 * memberService.memberLogin(memberDTO)가 호출되면서 데이터베이스에서 사용자가 입력한 아이디와 비밀번호에 해당하는
		 * 사용자가 있는지 확인 이 메서드는 로그인 정보가 맞으면 해당 사용자의 MemberDTO 객체를 반환하고, 맞지 않으면 null을 반환
		 */
		MemberDTO login = memberService.memberLogin(memberDTO);

//		로그인 실패 처리
//		만약 login이 null이면, 입력한 아이디와 비밀번호에 해당하는 사용자가 없다는 것
//		로그인 정보가 맞지 않거나 해당 사용자가 없으면 login은 null
		if (login == null) {
			logger.info("로그인 실패");
//			로그인 실패 시, 세션에 저장된 사용자 정보를 null로 설정->로그인이 되어 있지 않은 상태로 처리됨
			session.setAttribute("member", null);

//			RedirectAttributes에 msg라는 속성을 추가하여 로그인 실패 메시지를 전달
			rttr.addFlashAttribute("msg", false); // 로그인 실패 메시지 설정
			logger.info("로그인 실패, msg 값: {}", false); // 로그 찍기

//			로그인 실패 시 로그인 페이지로 리다이렉트
			return "redirect:/MemberLogin";
		} else {
//			로그인 성공 시, 세션에 로그인한 사용자의 정보를 member라는 이름으로 저장
			session.setAttribute("member", login);
//			로그인 성공 후 세션에 저장된 'member' 값 로그로 출력
			logger.info("로그인 성공");
			logger.info("세션에 저장된 사용자 정보: {}", session.getAttribute("member"));
//			절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
//			로그인 처리 끝난 후 BoardList 페이지로 리다이렉트
			return "redirect:/BoardList";
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
	public String update(MemberDTO memberDTO, HttpSession session) throws Exception {
		logger.info("회원 정보 수정");

//		회원정보 수정 페이지에서 입력한 정보들을 memberService.memberUpdate(memberDTO)에 넣어줘서 
//		service로 보내줌
		memberService.memberUpdate(memberDTO);

//		세션을 무효화하여 모든 세션 데이터를 삭제
		session.invalidate();

//		로그인 페이지로 리다이렉트
		return "redirect:/MemberLogin";
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
		
//		회원 탈퇴
		memberService.memberDelete(memberDTO);
		
//		회원 탈퇴 성공 메시지 전달
	    rttr.addFlashAttribute("msg", "success");

//		세션을 무효화하여 모든 세션 데이터를 삭제
		session.invalidate();

//		절대 경로로 지정하여 중복 경로가 발생하지 않도록 함
		return "redirect:/BoardList";
	}
}
