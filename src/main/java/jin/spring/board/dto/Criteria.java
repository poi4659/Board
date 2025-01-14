package jin.spring.board.dto;

//페이징 기준 정보
public class Criteria {
//	현재 페이지
	private int page;
//	페이지당 데이터 수
	private int perPageNum;
//	시작 행
	private int rowStart;
//	끝 행
	private int rowEnd;
	
//	생성자
	public Criteria() {
//		page를 기본값 1로 설정
		this.page = 1;
//		perPageNum를 기본값 10으로 설정
		this.perPageNum = 10;
	}
	
	public void setPage(int page) {
//		page가 0 이하일 경우: setPage(-5)처럼 잘못된 값 입력될 경우
		if (page <= 0) {
//			page 기본값을 1로 설정
			this.page = 1;
//			실행 종료
			return;
		}
//		유효한 값이라면 그대로 설정
		this.page = page;
	}
	
	public void setPerPageNum(int perPageNum) {
//		perPageNum이 0 이하이거나 100을 초과하면
		if (perPageNum <= 0 || perPageNum > 100) {
//			perPageNum 기본값을 10으로 설정
			this.perPageNum = 10;
			return;
		}
		this.perPageNum = perPageNum;
	}
	
//	현재 페이지 번호를 반환
	public int getPage() {
		return page;
	}
	
//	데이터베이스에서 사용할 시작 인덱스를 반환
	public int getPageStart() {
//		예) page = 1, perPageNum = 10이면, getPageStart()는 0 (첫 번째 데이터부터 시작)
//		page = 2, perPageNum = 10이면, getPageStart()는 10
		return (this.page - 1) * perPageNum;
	}
	
	public int getPerPageNum() {
		return this.perPageNum;
	}
	
//	현재 페이지에서 데이터베이스 쿼리를 실행할 때 사용할 시작 행 번호를 계산
	public int getRowStart() {
		/*
		 * page - 1: 현재 페이지에서 이전 페이지들에 있는 
		 * 데이터의 총 개수를 구하기 위해 페이지 번호를 하나 줄임 
		 * 
		 * 한 페이지에 표시할 데이터 수(perPageNum)를 곱하여 
		 * 이전 페이지들에 있는 데이터의 총 개수를 계산
		 * 현재 페이지의 시작 데이터 번호를 구하기 위해 1을 더함
		 */		
		rowStart = ((page - 1) * perPageNum) + 1;
		return rowStart;
	}
	
//	현재 페이지에서의 마지막 데이터 행 번호를 계산
	public int getRowEnd() {
//		현재 페이지의 시작 데이터 번호 + 한 페이지의 데이터 개수
//		마지막 데이터 번호는 "개수"가 아니라 "위치"를 의미하므로, 1을 뺌
		rowEnd = rowStart + perPageNum - 1;
		return rowEnd;
	}

//	객체 정보를 문자열 형태로 반환
	@Override
	public String toString() {
		return "Criteria [page=" + page + ", perPageNum=" + perPageNum + ", rowStart=" + rowStart + ", rowEnd=" + rowEnd
				+ "]";
	}
	
}