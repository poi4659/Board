<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원가입</title>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min_4.5.0.css">
<link rel="stylesheet" type="text/css" href="./css/global.css">
<script src="./js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="./js/bootstrap.min_4.5.0.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		 // fn_idChk 함수 정의
        function fn_idChk() {
            $.ajax({
                url: "./MemberIdCheck",
                type: "POST",
                dataType: "json",
                data: { "memberId": $("#memberId").val() },
                success: function(data) {
                    if (data == 1) {
                        alert("중복된 아이디입니다.");
                    } else if (data == 0) {
                        $("#idChk").val("Y");
                        alert("사용 가능한 아이디입니다.");
                    }
                },
                error: function(xhr, status, error) {
                    alert("오류가 발생했습니다: " + error);
                }
            });
        }

        // 버튼 클릭 이벤트
        $("#idChk").on("click", function() {
        	if($("#memberId").val()==""){
				alert("아이디를 입력해주세요.");
				$("#memberId").focus();
				return false;
			}
            fn_idChk(); // fn_idChk 호출
        });
		
		// 취소 버튼 클릭 시 동작
		$("#cancel").on("click", function() {
			location.href = "./BoardList";
		});

		$("#submit").on("click", function(){
			if($("#memberId").val()==""){
				alert("아이디를 입력해주세요.");
				$("#userId").focus();
				return false;
			}
			if($("#memberPW").val()==""){
				alert("비밀번호를 입력해주세요.");
				$("#memberPW").focus();
				return false;
			}
			if($("#memberName").val()==""){
				alert("성명을 입력해주세요.");
				$("#memberName").focus();
				return false;
			}
			var idChkVal = $("#idChk").val();
			if(idChkVal == "N"){
				alert("중복확인 버튼을 눌러주세요.");
			}else if(idChkVal == "Y"){
				$("#regForm").submit();
			}
		});
	})
	
</script>

</head>
<body>
	<header id="main-header" class="py-2 btn-dark text-white">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h1>회원가입</h1>
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
							<h5>회원가입</h5>
						</div>
						<div class="card-body">
							<%-- 폼 데이터를 서버에 POST 방식으로 전송 --%>
							<form method="post" action="./MemberRegister" id="regForm">
								<fieldset>
									<div class="form-group row">
										<label for="memberId" class="ml-sm-3 col-form-label"> 아이디 </label>
										<div class="ml-sm-3">
											<%-- 각 항목에 대해 name 속성은 서버에서 데이터를 받는 키로 사용 --%>
											<input type="text" name="memberId" id="memberId" class="form-control form-control-sm">
										</div>
										<div class="ml-sm-3">
											<button type="button" id="idChk" data-id="N" class="btn btn-secondary">중복확인</button>
										</div>
									</div>
									<div class="form-group row">
										<label for="memberPW" class="ml-sm-3 col-form-label"> 비밀번호 </label>
										<div class="ml-sm-3">
											<input type="password" name="memberPW" id="memberPW" class="form-control form-control-sm">
										</div>
									</div>
									<div class="form-group row">
										<label for="memberName" class="ml-sm-3 col-form-label"> 성명 </label>
										<div class="ml-sm-3">
											<input type="text" name="memberName" id="memberName" class="form-control form-control-sm">
										</div>
									</div>
									<div class="form-group">
										<%-- 사용자가 입력한 데이터를 서버로 제출하는 버튼 --%>
										<button type="submit" class="btn btn-secondary" id="submit">회원가입</button>
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