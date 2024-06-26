<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<header>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>쇼핑몰</title>
	<link href="/javaMall/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</header>
<body class="d-flex flex-column h-100">
<script src="/javaMall/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css" />
<script src="https://kit.fontawesome.com/d29904c170.js" crossorigin="anonymous"></script>

<c:if test="${not empty sessionScope.globalMsg}">
	<script>
		alert("${sessionScope.globalMsg}");
	</script>
	<c:set var="globalMsg" scope="session" value="${sessionScope.globalMsg}"/>
	<c:remove var="globalMsg" scope="session"/>
</c:if>

<!-- 헤더 영역 [시작] -->
<div class="container">
	<header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
		<a href="/javaMall/shoppingMall/main" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">메인 로고</a>
		<c:if test="${not empty sessionScope.user_no && sessionScope.user_no > 0}">
			<ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
				<li>
					<a href="/javaMall/shoppingMall/member/my_info" class="nav-link px-2 link-secondary"><i class="fas fa-user"></i></a>
					
				</li>
				<li>
					<a href="/javaMall/shoppingMall/member/my_cart" class="nav-link px-2 link-secondary"><i class="fas fa-shopping-cart"></i></a>
				</li>
			</ul>
		</c:if>
		<div class="col-md-3 text-end">
			<c:choose>
				<c:when test="${not empty sessionScope.user_no && sessionScope.user_no > 0}">
					<a href="/javaMall/shoppingMall/member/logout" class="btn btn-outline-primary me-2">로그아웃</a>
				</c:when>
				<c:otherwise>
					<a href="/javaMall/shoppingMall/member/login.jsp" class="btn btn-primary">로그인</a>
					<a href="/javaMall/shoppingMall/member/join.jsp" class="btn btn-outline-primary me-2">회원가입</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>
</div>
<!-- 헤더 영역 [종료] -->