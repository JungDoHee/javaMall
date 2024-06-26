<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="container py-5">
	<h4 class="mb-3">로그인</h4>
	<form class="needs-validation" novalidate action="/javaMall/adminMall/auth/login" method="POST">
	<div class="row g-3">		
		<div>
			<label for="admin_id">아이디</label>
			<input type="text" class="form-control" id="admin_id" name="admin_id" placeholder="아이디를 입력하세요" required>
			<div class="invalid-feedback">올바르지 않은 아이디입니다</div>
		</div>
		<div>
			<label for="admin_password">비밀번호</label>
			<input type="password" class="form-control" id="admin_password" name="admin_password" placeholder="비밀번호를 입력하세요" required>
			<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
		</div>
	</div>
		
	<hr class="my-4">
	<input type="submit" class="w-100 btn btn-primary btn-lg" value="로그인">
	</form>
</div>

<%@ include file="/adminMall/include/footer.jsp"%>