package jin.spring.board.member.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
//		아이디
		private String memberId;
//		비밀번호
		private String memberPW;
//		이름
		private String memberName;
//		가입일자
		private Date memberDate;
	}
