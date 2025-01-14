<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>

<style type="text/css">
/* 페이징 가로 정렬 */
ul {
    list-style: none;
    padding: 0;
    text-align: center;
    margin: 0 auto;  /* 가로로 중앙 정렬 */
}

li {
    display: inline-block; /* inline-block을 사용하여 가로로 나열 */
    padding: 6px;
}

li a {
    text-decoration: none;
    padding: 8px 16px;
    border: 1px solid #ddd;
    border-radius: 4px;
    color: #333;
}

li a:hover {
    background-color: #f8f9fa;
}

</style>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min_4.5.0.css">
<link rel="stylesheet" type="text/css" href="./css/global.css">
<script src="./js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="./js/bootstrap.min_4.5.0.js" type="text/javascript"></script>
</head>
<body>
	<header id="main-header" class="py-2 btn-dark text-white">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h1>게시판</h1>
				</div>
			</div>
		</div>
	</header>
	<section id="dept" class="py-4 mb-4 bg-light"></section>
	<section id="details">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="card">
						<div class="card-header">
							<h5>게시판 목록</h5>
						</div>
						<div class="card-body">
							<table class="table table-hover">
								<thead class="thead-light">
									<tr class="text-center">
										<th>번호</th>
										<th>제목</th>
										<th>작성자</th>
										<th>등록일</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="list" items="${list}">
										<tr class="text-center">
											<%-- JSP EL을 사용하여 데이터를 출력 --%>
											<td>${list.bnum}</td>
											<td>${list.btitle}</td>
											<td>${list.bwriter}</td>
											<td><fmt:formatDate value="${list.bdate}" pattern="yyyy-MM-dd" /></td>
											<%-- 번호를 URL 파라미터로 넘겨서 상세 페이지로 이동 --%>
											<td><a href="./BoardSelectDetail?bnum=${list.bnum}" class="btn btn-outline-info"> 게시글 상세 보기 </a></td>
										</tr>
									</c:forEach>
									<%--list가 null일 경우에 반환한다.--%>
									<c:if test="${empty list}">
										<tr>
											<td colspan="4">등록된 게시글이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
							<div>
								<%-- 클릭 시 ./BoardInsert로 이동하여 게시글 내용을 입력할 수 있는 페이지로 리디렉션 --%>
								<a href="./BoardInsert" class="btn btn-success btn-block"> 게시글 작성</a>
							</div>
							<%-- 페이징 처리 --%>
							<div style="margin-top: 30px;">
								<ul>
									<c:if test="${pageMaker.prev}">
										<li><a href="./BoardList${pageMaker.makeQuery(pageMaker.startPage - 1)}">이전</a></li>
									</c:if>

									<c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
										<li><a href="./BoardList${pageMaker.makeQuery(idx)}">${idx}</a></li>
									</c:forEach>

									<c:if test="${pageMaker.next && pageMaker.endPage > 0}">
										<li><a href="./BoardList${pageMaker.makeQuery(pageMaker.endPage + 1)}">다음</a></li>
									</c:if>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>