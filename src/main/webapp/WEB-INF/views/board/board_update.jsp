<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시판 글 수정</title>
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min_4.5.0.css">
<link rel="stylesheet" type="text/css" href="./css/global.css">
<script src="./js/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="./js/bootstrap.min_4.5.0.js" type="text/javascript"></script>
</head>

<script type="text/javascript">
    $(document).ready(function() {
        var fileNoArry = new Array(); // 삭제된 파일 번호를 저장할 배열
        var fileNameArry = new Array(); // 삭제된 파일 이름을 저장할 배열

        // "파일 추가" 버튼 클릭 시 새로운 파일 입력 필드 추가
        $("#fileAdd_btn").on("click", function() {
            console.log("파일 추가 버튼 클릭됨");

            // 파일이 선택되지 않은 경우 경고
            var fileInput = $("#fileInput")[0];
            if (fileInput.files.length == 0) {
                alert("파일을 선택해주세요.");
                return;
            }

            // 파일 목록에 추가할 div
            var fileList = $("#fileIndex"); // 파일 목록을 관리하는 div

            // 선택한 파일을 HTML에 추가
            for (var i = 0; i < fileInput.files.length; i++) {
                var file = fileInput.files[i];

                // 기존 파일 목록에 새로운 파일 추가
                fileList.append(
                    "<div>" +
                        "<input type='hidden' name='fileNumDel[]' value='" + file.name + "' />" +
                        "<input type='hidden' name='fileNameDel[]' value='" + file.name + "' />" +
                        file.name + " (" +
                        (file.size / 1024).toFixed(2) + " KB)" +
                        "<button type='button' class='btn btn-danger btn-sm fileDelBtn' id='fileDelBtn'>삭제</button>" +
                    "</div>"
                );
            }
        });

        // 동적으로 추가된 파일 삭제 버튼의 클릭 이벤트 처리
        $(document).on("click", ".fileDelBtn", function() {
            console.log("파일 삭제 버튼 클릭됨");
            var fileDiv = $(this).parent(); // 삭제된 파일의 div

            // 삭제된 파일의 정보 가져오기
            var fileName = fileDiv.find("input[name='fileNameDel[]']").val();
            var fileNum = fileDiv.find("input[name='fileNumDel[]']").val();

            // 삭제된 파일 정보를 배열에 추가
            fileNoArry.push(fileNum);
            fileNameArry.push(fileName);

            // 배열을 hidden input에 반영
            $("#fileNumDel").val(fileNoArry.join(","));
            $("#fileNameDel").val(fileNameArry.join(","));

            // 파일 항목 삭제
            fileDiv.remove();
        });
    });
</script>


<body>
	<header id="main-header" class="py-2 btn-dark text-white">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h1>게시글 수정</h1>
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
							<h5>글 수정</h5>
						</div>
						<div class="card-body">
							<form action="./BoardUpdate" method="post" name="updateForm" role="form" enctype="multipart/form-data">
								<fieldset>
									<%-- 로그인 시에만 수정할 수 있게 추가--%>
									<c:if test="${member.memberId != null}">
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
												<input type="text" name="btitle" id="btitle" class="form-control form-control-sm">
											</div>
										</div>
										<div class="form-group row">
											<label for="bcontent" class="ml-sm-3 col-form-label"> 내용 </label>
											<div class="ml-sm-3">
												<input type="text" name="bcontent" id="bcontent" class="form-control form-control-sm">
											</div>
										</div>

										<!-- 파일 삭제 관련 숨겨진 입력 필드에 설정하여 제출 -->
										<input type="hidden" name="fileNumDel[]" value="">
										<input type="hidden" name="fileNameDel[]" value="">

										<!-- 첨부파일 -->
										<div id="fileIndex">
											<span>파일 목록</span>

											<!-- 기존 파일이 있으면 목록에 표시 -->
											<c:if test="${not empty fileList}">
												<c:forEach var="file" items="${fileList}" varStatus="var">
													<div>
														<!-- 파일 번호와 파일 이름을 hidden input으로 관리 -->
														<input type="hidden" name="FILENUMDEL[]" value="${file.FILENUM}">
														<input type="hidden" name="FILENAMEDEL[]" value="${file.ORGFILENAME}">
														${file.ORGFILENAME} (${file.MPFILESIZE}KB)
														<button type="button" class="btn btn-danger btn-sm fileDelBtn">삭제</button>
													</div>
												</c:forEach>
											</c:if>
										</div>
										<!-- 새로운 파일을 추가할 수 있는 입력 폼 -->
										<div class="form-group">
											<div class="ml-sm-3 d-flex align-items-center">
												<input type="file" name="file" class="form-control form-control-sm" multiple id="fileInput">
												<button type="button" id="fileAdd_btn" class="btn btn-info">추가</button>
											</div>
										</div>

										<div class="form-group">

											<%-- 사용자가 입력한 데이터를 서버로 제출하는 버튼 --%>
											<button type="submit" class="btn btn-secondary">저장</button>
											<%-- 폼을 초기화하여 입력된 데이터를 지우는 버튼 --%>
											<button type="reset" class="btn btn-secondary">취소</button>

										</div>
								</fieldset>
								</c:if>
								<c:if test="${member.memberId == null}">
									<p>로그인 후에 작성하실 수 있습니다.</p>
								</c:if>
							</form>


							<div class="row">
								<div class="col-md-4">
									<%-- BoardSelect 페이지로 이동하는 링크로, 게시글 목록을 확인할 수 있음 --%>
									<a href="./BoardList" class="btn btn-primary btn-block"> 게시글 목록 </a>
								</div>
								<div class="col-md-4">
									<%-- 새로운 게시글을 입력할 수 있는 페이지로 이동하는 링크 --%>
									<a href="./BoardInsert" class="btn btn-success btn-block"> 게시글 등록</a>
								</div>
								<div class="col-md-4">
									<%-- 현재 수정 중인 게시글 번호를 삭제하는 링크. bnum 파라미터를 URL에 포함시켜 삭제할 게시글 번호를 전달 --%>
									<a href="./BoardDelete?bnum=${param.bnum}" class="btn btn-danger btn-block"> 게시글 삭제 </a>
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
