<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>

<div class="container">
	<%@ include file="/shoppingMall/include/my_info_nav.jsp" %>
	<main>
		<div class="row">
			<form class="needs-validation" novalidate action="/shoppingMall/member/modify/mod_my_password" method="POST">
				<div class="row g-2">
					<div class="col-12">
						<label for="user_password">현재 비밀번호</label>
						<input type="password" class="form-control" id="user_password_origin" name="user_password_origin" placeholder="현재 비밀번호를 입력하세요" required value="">
						<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
					</div>
					<div class="col-12">
						<label for="user_password">변경할 비밀번호</label>
						<input type="password" class="form-control" id="user_password" name="user_password" placeholder="변경할 비밀번호를 입력하세요" required value="">
						<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
					</div>
					<div class="col-12">
						<label for="user_password">변경할 비밀번호 확인</label>
						<input type="password" class="form-control" id="user_password_confirm" name="user_password_confirm" placeholder="변경할 동일한 비밀번호를 입력하세요" required value="">
						<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
					</div>
				</div>
				<hr class="my-4">
				<input type="submit" class="w-100 btn btn-primary btn-lg" value="비밀번호 수정">
			</form>
		</div>
	</main>
</div>

<%@ include file="/shoppingMall/include/footer.jsp"%>