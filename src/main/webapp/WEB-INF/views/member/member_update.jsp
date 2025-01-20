<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원 정보 수정</title>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min_4.5.0.css">
<link rel="stylesheet" type="text/css" href="./css/global.css">
<script src="./js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="./js/bootstrap.min_4.5.0.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		// msg 값이 세션에서 전달된 경우
		var msg = '${msg}'; // JSTL을 사용하여 msg 값을 변수에 담음

		if (msg === "false") {
			alert("현재 비밀번호가 틀렸습니다. 다시 입력해주세요.");
			$("#memberPW").focus();
		} else if (msg === "noPassword") {
			// 비밀번호가 없다는 메시지
			alert("저장된 비밀번호가 없습니다. 다시 로그인해주세요.");
			$("#memberPW").focus();
		}

		// 취소 버튼 클릭 시 동작
		$("#cancel").on("click", function() {
			location.href = "./BoardList";
		});

		// 수정 버튼 클릭 시 동작
		$("#submit").on("click", function() {
	    	// 나머지 입력값 검증
			if ($("#memberName").val() == "") {
				alert("성명을 입력해주세요.");
				$("#memberName").focus();
				return false;
			}
			if ($("#memberPW").val() == "") {
				alert("현재 비밀번호를 입력해주세요.");
				$("#memberPW").focus();
				return false;
			}
			if ($("#newMemberPW").val() == "") {
				alert("새 비밀번호를 입력해주세요.");
				$("#newMemberPW").focus();
				return false;
			}
			if ($("#newMemberPWChk").val() == "") {
				alert("새 비밀번호 확인을 입력해주세요.");
				$("#newMemberPWChk").focus();
				return false;
			}
			// 새 비밀번호와 새 비밀번호 확인 비교
	        if ($("#newMemberPW").val() !== $("#newMemberPWChk").val()) {
	            alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
	            $("#newMemberPWChk").focus();
	            return false;  // 새 비밀번호 확인이 일치하지 않으면 폼 제출을 막음
	        }
			// AJAX로 패스워드 체크
			// AJAX 요청을 통해 서버와 비동기적으로 데이터를 주고받을 수 있음
			$.ajax({
				// 요청을 보낼 URL
				url : "./MemberPassCheck",
				// HTTP 요청 방식
				type : "POST",
				// 서버에서 받을 응답의 데이터 형식
				dataType : "json",
				// delForm 폼의 데이터를 직렬화(serialize)하여 서버에 전달
				data : $("#updateForm").serializeArray(),
				// 요청이 성공적으로 완료되면 success 콜백 함수가 실행됨
				success : function(data) {
					if (data) {
						if (confirm("회원 정보를 수정하시겠습니까?")) {
							$("#updateForm").submit();
						}
					} else {
						alert("현재 비밀번호가 틀렸습니다.");
					}
				}
			});
		});
	});
</script>
</head>
<body>
	<header id="main-header" class="py-2 btn-dark text-white">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h1>회원 정보 수정</h1>
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
							<h5>회원 정보 수정</h5>
						</div>
						<div class="card-body">
							<%--action을 생략해도 되지만, readonly 기능이 적용되지 않을 수도 있으므로 생략하지 않는다.--%>
							<%-- 폼이 제출되면 DeptUpdate URL로 POST 요청을 보냄 --%>
							<form action="./MemberUpdate" method="post" id="updateForm">
								<fieldset>
									<div class="form-group row">
										<label for="memberId" class="ml-sm-3 col-form-label text-right"> 아이디 </label>
										<div class="ml-sm-3">
											<%-- value="${param.memberId}": URL에서 memberId 파라미터를 가져옴 --%>
											<%-- 아이디는 수정할 수 없도록 readonly 속성으로 설정 --%>
											<input type="text" id="memberId" name="memberId" class="form-control form-control-sm bg-white" value="${member.memberId}" readonly />

										</div>
									</div>

									<div class="form-group row">
										<label for="memberName" class="ml-sm-3 col-form-label text-right"> 성명 </label>
										<div class="ml-sm-3">
											<input type="text" name="memberName" id="memberName" class="form-control form-control-sm bg-white" value="${member.memberName}">
										</div>
									</div>

									<div class="form-group row">
										<label for="memberPW" class="ml-sm-3 col-form-label"> 현재 비밀번호 </label>
										<div class="ml-sm-3">
											<input type="password" name="memberPW" id="memberPW" class="form-control form-control-sm bg-white">
										</div>
									</div>

									<div class="form-group row">
										<label for="memberPW" class="ml-sm-3 col-form-label"> 새 비밀번호 </label>
										<div class="ml-sm-3">
											<input type="password" name="newMemberPW" id="newMemberPW" class="form-control form-control-sm bg-white">
										</div>
									</div>
									
									<div class="form-group row">
										<label for="memberPW" class="ml-sm-3 col-form-label"> 새 비밀번호 확인 </label>
										<div class="ml-sm-3">
											<input type="password" name="newMemberPWChk" id="newMemberPWChk" class="form-control form-control-sm bg-white">
										</div>
									</div>


									<div class="form-group">
										<%-- 사용자가 입력한 데이터를 서버로 제출하는 버튼 --%>
										<button type="submit" class="btn btn-secondary" id="submit">수정</button>
										<%-- 폼을 초기화하여 입력된 데이터를 지우는 버튼 --%>
										<button type="reset" class="btn btn-secondary" id="cancel">취소</button>
									</div>
								</fieldset>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>
