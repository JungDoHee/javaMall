<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>
<script src="/adminMall/js/checkout.js"></script>

<div class="container py-5">
	<h4 class="mb-3">관리자 등록</h4>
	<form class="needs-validation" novalidate action="/javaMall/adminMall/auth/join" method="POST">
	<div class="row g-3">
		<div class="col-12">
			<label for="admin_name" class="form-label">이름</label>
			<input type="text" class="form-control" id="admin_name" name="admin_name" placeholder="이름을 입력하세요" required>
			<div class="invalid-feedback">한글, 영문, 최소 2자, 최대 20자, 특수문자 사용 불가</div>
		</div>
		
		<div class="col-12">
			<label for="admin_id">아이디</label>
			<input type="text" class="form-control" id="admin_id" name="admin_id" placeholder="아이디를 입력하세요" required>
			<div class="invalid-feedback">올바르지 않은 아이디입니다</div>
		</div>
		<div class="col-12">
			<label for="admin_password">비밀번호</label>
			<input type="password" class="form-control" id="admin_password" name="admin_password" placeholder="비밀번호를 입력하세요" required>
			<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
		</div>
	</div>
		
	<hr class="my-4">
	<input type="submit" class="w-100 btn btn-primary btn-lg" value="관리자 등록">
	</form>
</div>

<%@ include file="/adminMall/include/footer.jsp"%>