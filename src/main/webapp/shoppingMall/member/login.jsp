<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>쇼핑몰</title>
	<link href="/javaMall/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</header>
<body>
<script src="/javaMall/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<c:if test="${not empty sessionScope.globalMsg}">
	<script>
		alert("${sessionScope.globalMsg}");
	</script>
	<c:set var="globalMsg" scope="session" value="${sessionScope.globalMsg}"/>
	<c:remove var="globalMsg"/>
</c:if>
<body class="d-flex align-items-center py-4 bg-body-tertiary">
	<main class="form-signin w-100 m-auto">
		 <form method="POST" action="/javaMall/shoppingMall/member/login">
			 <a href="/javaMall/" class="mb-4">메인 로고</a>
			 <h1 class="h3 mb-3 fw-normal">로그인</h1>
			
			 <div class="form-floating">
			   <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com" name="user_email">
			   <label for="floatingInput">아이디</label>
			 </div>
			 <div class="form-floating">
			   <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="user_password">
			   <label for="floatingPassword">비밀번호</label>
			 </div>
			 <button class="btn btn-primary w-100 py-2" type="submit">로그인</button>
			 <p class="mt-5 mb-3 text-body-secondary">JAVA 쇼핑몰 v2.0</p>
		 </form>
	</main>
</body>