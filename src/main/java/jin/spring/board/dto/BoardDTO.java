package jin.spring.board.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
//	게시글 번호
	private int bnum;
//	게시글 제목
	private String btitle;
//	게시글 내용
	private String bcontent;
//	게시글 작성자
	private String bwriter;
//	게시글 등록일
	private Date bdate;
}
