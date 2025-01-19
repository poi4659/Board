<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원 탈퇴</title>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min_4.5.0.css">
<link rel="stylesheet" type="text/css" href="./css/global.css">
<script src="./js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="./js/bootstrap.min_4.5.0.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		// msg 값이 세션에서 전달된 경우
        var msg = '${msg}';  // JSTL을 사용하여 msg 값을 변수에 담음
        
        if (msg === "false") {
            alert("비밀번호가 맞지 않습니다. 다시 입력해주세요.");
        }
        
		// 취소 버튼 클릭 시 동작
		$("#cancel").on("click", function() {
			location.href = "./BoardList";
		});

		// 비밀번호 미 입력시 폼 제출 안되게 함
		$("#submit").on("click", function() {
			if ($("#memberPW").val() == "") {
				alert("비밀번호를 입력해주세요.");
				$("#memberPW").focus();
				return false;
			}
			// 폼 제출 허용
			return true;
		});
	});
</script>

</head>
<body>
	<header id="main-header" class="py-2 btn-dark text-white">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h1>회원 탈퇴</h1>
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
							<h5>회원 탈퇴</h5>
						</div>
						<div class="card-body">
							<%-- POST 방식으로 데이터를 서버에 전송 --%>
							<form method="post" id="sign_board">
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
										<label for="memberPW" class="ml-sm-3 col-form-label"> 비밀번호 </label>
										<div class="ml-sm-3">
											<input type="password" name="memberPW" id="memberPW" class="form-control form-control-sm bg-white">
										</div>
									</div>

									<div class="form-group row">
										<label for="memberName" class="ml-sm-3 col-form-label text-right"> 성명 </label>
										<div class="ml-sm-3">
											<input type="text" name="memberName" id="memberName" class="form-control form-control-sm bg-white" value="${member.memberName}" readonly>
										</div>
									</div>
									
									<div class="form-group">
										<%-- 사용자가 입력한 데이터를 서버로 제출하는 버튼 --%>
										<button type="submit" class="btn btn-secondary" id="submit">회원 탈퇴</button>
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