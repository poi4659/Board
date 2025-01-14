package jin.spring.board.dto;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

//페이징 처리
public class PageMaker {
//	전체 데이터 개수
	private int totalCount;
//	페이지 시작 번호
	private int startPage;
//	페이지 끝 번호
	private int endPage;
//	이전 페이지 버튼 활성화 여부
	private boolean prev;
//	다음 페이지 버튼 활성화 여부
	private boolean next;
//	한 번에 표시할 페이지 번호 개수
	private int displayPageNum = 10;
//	페이징 기준 정보 (현재 페이지, 페이지당 데이터 수 등)
	private Criteria cri;

//	페이징 기준 설정: 외부에서 Criteria 객체를 받아 설정
	public void setCri(Criteria cri) {
		this.cri = cri;
	}

//	전체 데이터 개수 설정
	public void setTotalCount(int totalCount) {
//		총 데이터 개수를 설정
		this.totalCount = totalCount;
//		calcData 메서드를 호출해 페이징 데이터를 계산
		calcData();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public Criteria getCri() {
		return cri;
	}

	
	/*	
	 * 현재 페이지 그룹에서 마지막 페이지 번호를 계산
	 * 페이지 그룹: 여러 페이지를 한 묶음으로 묶어서 페이지 네비게이션을 할 수 있도록 함
	 * 예) 한번에 표시할 수 있는 페이지는 10개로 묶어놓고, 
	 * 그 이후에는 다음 버튼을 눌러서 다음 그룹으로 이동하는 방식
	 * 
	*/
	private void calcData() {
		/*
		 * 현재 페이지가 속한 페이지 그룹의 끝 페이지 번호를 계산 
		 * cri.getPage(): 현재 페이지 번호를 가져옴 
		 * displayPageNum: 한 페이지 그룹에 표시할 페이지 개수 
		 * cri.getPage() / displayPageNum: 현재 페이지가 몇 번째 그룹에 속하는지 계산
		 * Math.ceil(): 올림하여 정수로 변환 displayPageNum: 해당 그룹의 끝 페이지 계산
		 */
		endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		
//		현재 페이지 그룹의 시작 페이지 번호를 계산
		startPage = (endPage - displayPageNum) + 1;

//		데이터 개수에 따라 전체 페이지 수를 계산
		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		
//		계산된 endPage가 전체 페이지 수를 초과하면, endPage를 전체 페이지 수로 설정
//		예: endPage = 20, tempEndPage = 10 → endPage = 10
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
//		현재 페이지 그룹에 이전 버튼이 필요한지 결정
//		현재 페이지 그룹의 시작 페이지 번호가 1이면 이전 그룹 없음 → prev = false.
//		시작 페이지 번호가 1보다 크면 이전 그룹 존재 → prev = true
		prev = startPage == 1 ? false : true;
		
//		현재 페이지 그룹에 다음 버튼이 필요한지 결정
//		endPage * cri.getPerPageNum(): 현재 그룹의 마지막 페이지 번호에 표시된 데이터의 마지막 행
//		>= totalCount: 이 값이 총 데이터 개수 이상이면 다음 페이지 그룹 없음 → next = false
		next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
	}

//	쿼리 문자열 생성하는 메서드->링크를 동적으로 생성하여 페이지 이동을 쉽게 처리할 수 있도록 도움
	public String makeQuery(int page) {
		/*
		 * UriComponentsBuilder를 사용해 쿼리 문자열을 생성 
		 * queryParam(): 쿼리 파라미터를 추가 
		 * build(): 위에서 설정한 값들을 바탕으로 UriComponents 객체를 생성
		 * 
		 * queryParam("page", page): URL에 page라는 쿼리 파라미터를 추가
		 * perPageNumcri: 한 페이지에 표시할 데이터의 개수
		 * cri.getPerPageNum()은 Criteria 클래스에서 한 페이지에 몇 개의 데이터를 표시할지를 결정하는 값을 가져옴
		 */
		UriComponents uriComponents = UriComponentsBuilder.newInstance().queryParam("page", page)
				.queryParam("perPageNum", cri.getPerPageNum()).build();

		/*
		 * toUriString(): UriComponents 객체를 실제 문자열 형태의 URI로 변환 
		 * 예) page=2와 perPageNum=10을 쿼리 파라미터로 포함한 URL이 반환
		 * -> 반환되는 URL: ...?page=2&perPageNum=10
		 */
		return uriComponents.toUriString();
	}
}