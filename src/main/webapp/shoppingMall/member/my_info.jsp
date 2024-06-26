<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>


<div class="container">
	<%@ include file="/shoppingMall/include/my_info_nav.jsp" %>
	<main>
		<div class="row">
			<form class="needs-validation" novalidate action="/shoppingMall/member/modify/mod_my_info" method="POST">
				<div class="row g-2">
					<div class="col-12">
						<label for="user_name">이름</label>
						<input type="text" class="form-control" id="user_name" name="user_name" placeholder="이름을 입력하세요" required value="${rgList.getUserName()}">
						<div class="invalid-feedback">한글, 영문 사용, 최소 2자, 최대 20자, 특수문자 사용 불가</div>
					</div>
					<div class="col-12">
						<label for="user_email">이메일</label>
						<input type="email" class="form-control" id="user_email" name="user_email" placeholder="이메일을 입력하세요" required value="${rgList.getUserId()}" readonly>
					</div>
					<div class="col-12">
						<label for="user_phone">전화번호</label>
						<input type="text" class="form-control" id="user_phone" name="user_phone" placeholder="숫자만 입력하세요" required value="${rgList.getUserPhone()}" maxlength="11">
						<div class="invalid-feedback">- 없이 숫자만 입력</div>
					</div>
					<div class="col-md-3">
						<label for="user_zip_code">우편번호(지원예정)</label>
						<input type="text" class="form-control" id="user_zip_code" name="user_zip_code" value="${rgList.getUserZipCode()}">
					</div>
					<div class="col-md-9">
						<label for="user_address">상세 주소(지원예정)</label>
						<input type="text" class="form-control" id="user_address" name="user_address" value="${rgList.getUserAddress()}">
					</div>
				</div>
				<hr class="my-4">
				<input type="submit" class="w-100 btn btn-primary btn-lg" value="회원 정보 수정">
			</form>
		</div>
	</main>
</div>

<%@ include file="/shoppingMall/include/footer.jsp"%>