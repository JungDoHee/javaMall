<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>
<script src="/shoppingMall/js/checkout.js"></script>
<div class="container">
	<main>
		<div class="row">
			<h4>회원가입</h4>
			<form class="needs-validation" novalidate action="/shoppingMall/member/join" method="POST">
				<div class="row g-2">
					<div class="col-12">
						<label for="user_name">이름</label>
						<input type="text" class="form-control" id="user_name" name="user_name" placeholder="이름을 입력하세요" required value="">
						<div class="invalid-feedback">한글, 영문 사용, 최소 2자, 최대 20자, 특수문자 사용 불가</div>
					</div>
					<div class="col-12">
						<label for="user_email">이메일</label>
						<input type="email" class="form-control" id="user_email" name="user_email" placeholder="이메일을 입력하세요" required value="">
						<div class="invalid-feedback">올바르지 않은 아이디입니다</div>
					</div>
					<div class="col-12">
						<label for="user_password">비밀번호</label>
						<input type="password" class="form-control" id="user_password" name="user_password" placeholder="비밀번호를 입력하세요" required value="">
						<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
					</div>
					<div class="col-12">
						<label for="user_password">비밀번호 확인</label>
						<input type="password" class="form-control" id="user_password_confirm" name="user_password_confirm" placeholder="동일한 비밀번호를 입력하세요" required value="">
						<div class="invalid-feedback">최소 8자, 최대 30자, 대소문자, 숫자 포함</div>
					</div>
					<div class="col-12">
						<label for="user_phone">전화번호</label>
						<input type="text" class="form-control" id="user_phone" name="user_phone" placeholder="숫자만 입력하세요" required value="" maxlength="11">
						<div class="invalid-feedback">- 없이 숫자만 입력</div>
					</div>
					<div class="col-md-3">
						<label for="user_zip_code">우편번호(지원예정)</label>
						<input type="text" class="form-control" id="user_zip_code" name="user_zip_code">
					</div>
					<div class="col-md-9">
						<label for="user_address">상세 주소(지원예정)</label>
						<input type="text" class="form-control" id="user_address" name="user_address">
					</div>
				</div>
				<hr class="my-4">
				<div class="form-check">
					<h4>이용약관</h4>
					<p>이용약관 영역</p>
					<input type="checkbox" class="form-check-input" id="join_agree" name="join_agree" checked>
					<label class="form-check-label" for="join_agree">이용 약관에 동의합니다</label>
				</div>
				<hr class="my-4">
				<input type="submit" class="w-100 btn btn-primary btn-lg" value="회원가입">
			</form>
		</div>
	</main>
</div>

<%@ include file="/shoppingMall/include/footer.jsp"%>