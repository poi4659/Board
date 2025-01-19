<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>마이페이지</title>
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
					<h1>마이페이지</h1>
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
							<h5>회원 정보</h5>
						</div>
						<div class="card-body">
							<table class="table table-hover">
								<thead class="thead-light">
									<tr class="text-center">
										<th>아이디</th>
										<th>성명</th>
										<th>가입일자</th>
									</tr>
								</thead>
								<tbody>
									<tr class="text-center">
										<%-- JSP EL을 사용하여 데이터를 출력 --%>
										<%-- 컨트롤러에서 전달된 boardDTO 객체의 데이터를 출력 --%>
										<td>${member.memberId}</td>
										<td>${member.memberName}</td>
										<td><fmt:formatDate value="${member.memberDate}" pattern="yyyy-MM-dd" /></td>
									</tr>
								</tbody>
							</table>
							<div class="row">
								<div class="col-md-4">
									<a href="./BoardList" class="btn btn-primary btn-block"> 게시판 </a>
								</div>
								<div class="col-md-4">
									<a href="./MemberUpdate?memberId=${member.memberId}" class="btn btn-warning btn-block"> 회원 정보 수정 </a>
								</div>
								<div class="col-md-4">
									<a href="./MemberDelete?memberId=${member.memberId}" class="btn btn-danger btn-block"> 회원 탈퇴 </a>
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