<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 삭제</title>
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
					<h1>게시글 삭제</h1>
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
							<h5>글 삭제</h5>
						</div>
						<div class="card-body">
							<%-- POST 방식으로 데이터를 서버에 전송 --%>
							<form method="post" id="sign_board">
								<fieldset>
									<div class="form-group row">
										<div class="ml-sm-3">
											<%-- value="${param.bnum}": URL에서 bnum 파라미터를 가져옴 --%>
											<input type="hidden" id="bnum" name="bnum" value="${param.bnum}" />

											<%-- 작성자는 수정할 수 없도록 readonly 속성으로 설정 --%>
											<label for="bwriter" class="ml-sm-3 col-form-label text-right"> 작성자 </label> 
											<input type="text" name="bwriter" id="bwriter" class="form-control form-control-sm bg-white" value="${boardDTO.bwriter}" readonly>
										</div>
									</div>
									<div class="form-group row">
										<label for="btitle" class="ml-sm-3 col-form-label"> 제목 </label>
										<div class="ml-sm-3">
											<input type="text" name="btitle" id="btitle" class="form-control form-control-sm" value="${boardDTO.btitle}" readonly>
										</div>
									</div>
									<div class="form-group row">
										<label for="bcontent" class="ml-sm-3 col-form-label"> 내용 </label>
										<div class="ml-sm-3">
											<input type="text" name="bcontent" id="bcontent" class="form-control form-control-sm" value="${boardDTO.bcontent}" readonly>
										</div>
									</div>
									<div class="form-group">
										<%-- 사용자가 입력한 데이터를 서버로 제출하는 버튼 --%>
										<button type="submit" class="btn btn-secondary">삭제</button>
										<%-- 폼을 초기화하여 입력된 데이터를 지우는 버튼 --%>
										<button type="reset" class="btn btn-secondary">취소</button>
									</div>
								</fieldset>
							</form>
							<div class="row">
								<div class="col-md-4">
									<a href="./BoardSelect" class="btn btn-primary btn-block"> 게시글 목록 </a>
								</div>
								<div class="col-md-4">
									<a href="./BoardInsert" class="btn btn-success btn-block"> 게시글 등록 </a>
								</div>
								<div class="col-md-4">
									<a href="./BoardUpdate?bnum=${param.bnum}" class="btn btn-warning btn-block"> 게시글 수정 </a>
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