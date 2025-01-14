<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시판</title>
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
					<h1>게시글 상세 조회</h1>
				</div>
			</div>
		</div>
	</header>
	<section id="actions" class="py-4 mb-4 bg-light"></section>
	<section id="details">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="card">
						<div class="card-header">
							<h5>게시글 상세 조회</h5>
						</div>
						<div class="card-body">
							<table class="table table-hover">
								<thead class="thead-light">
									<tr class="text-center">
										<th>번호</th>
										<th>제목</th>
										<th>내용</th>
										<th>작성자</th>
										<th>등록일</th>
									</tr>
								</thead>
								<tbody>
									<tr class="text-center">
										<%-- JSP EL을 사용하여 데이터를 출력 --%>
										<%-- 컨트롤러에서 전달된 boardDTO 객체의 데이터를 출력 --%>
										<td>${boardDTO.bnum}</td>
										<td>${boardDTO.btitle}</td>
										<td>${boardDTO.bcontent}</td>
										<td>${boardDTO.bwriter}</td>
										<td><fmt:formatDate value="${boardDTO.bdate}" pattern="yyyy-MM-dd" /></td>
									</tr>
								</tbody>
							</table>
							<div class="row">
								<div class="col-md-4">
									<%-- 게시글 목록 버튼을 클릭하면, 게시글 목록 페이지로 리디렉션 --%>
									<a href="./BoardList" class="btn btn-primary btn-block"> 게시글 목록 </a>
								</div>
								<div class="col-md-4">
									<%-- 게시글 수정 버튼을 클릭하면, bnum 값을 URL 파라미터로 전달하면서 게시글 수정 페이지로 리디렉션 --%>
									<a href="./BoardUpdate?bnum=${boardDTO.bnum}" class="btn btn-warning btn-block"> 게시글 수정 </a>
								</div>
								<div class="col-md-4">
									<%-- 게시글 삭제 버튼을 클릭하면, bnum 값을 URL 파라미터로 전달하면서 게시글 삭제 페이지로 이동 --%>
									<a href="./BoardDelete?bnum=${boardDTO.bnum}" class="btn btn-danger btn-block"> 게시글 삭제 </a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>