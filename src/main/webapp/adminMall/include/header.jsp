<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="util.activatePage" id="activatePage" scope="request"/>
<jsp:setProperty name="activatePage" property="curPage" value="${pageContext.request.requestURI}"/>
<html>
<header>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>관리자-쇼핑몰</title>
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
      

<!-- 내비게이션 영역 [시작] -->
<main class="d-flex flex-nowrap">
  <div class="d-flex flex-column flex-shrink-0 p-3 text-bg-dark" style="width: 280px; height:100%">
    <a href="/javaMall/adminMall/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
      <span class="fs-4">메인</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
      <li class="nav-item">
      	<jsp:setProperty name="activatePage" property="targetPage" value="/javaMall/adminMall/member_info/list"/>
        <a href="/javaMall/adminMall/member_info/list" class="nav-link <%= (activatePage.getCompare()) ? "active" : "" %>" aria-current="page">
          고객관리
        </a>
      </li>
    </ul>
    <ul class="nav nav-pills flex-column mb-auto">
      <li class="nav-item">
      	<jsp:setProperty name="activatePage" property="targetPage" value="/javaMall/adminMall/item_manage/list"/>
        <a href="/javaMall/adminMall/item_manage/list" class="nav-link <%= (activatePage.getCompare()) ? "active" : "" %>" aria-current="page">
          물품관리
        </a>
      </li>
    </ul>
    <ul class="nav nav-pills flex-column mb-auto">
      <li class="nav-item">
      	<jsp:setProperty name="activatePage" property="targetPage" value="/javaMall/adminMall/category_manage/list"/>
        <a href="/javaMall/adminMall/category_manage/list" class="nav-link <%= (activatePage.getCompare()) ? "active" : "" %>" aria-current="page">
          상품분류
        </a>
      </li>
    </ul>
    <ul class="nav nav-pills flex-column mb-auto">
      <li class="nav-item">
      	<jsp:setProperty name="activatePage" property="targetPage" value="/javaMall/adminMall/goods_order/list"/>
        <a href="/javaMall/adminMall/goods_order/list" class="nav-link <%= (activatePage.getCompare()) ? "active" : "" %>" aria-current="page">
          주문관리
        </a>
      </li>
    </ul>
    <hr>
    <div class="dropdown">
	    <c:choose>
	    	<c:when test="${ sessionScope.admin_no > 0 }">
	    		<a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
				  <strong>${ sessionScope.admin_name }</strong>
				</a>
				<ul class="dropdown-menu dropdown-menu-dark text-small shadow">
				  <li><a class="dropdown-item" href="/javaMall/adminMall/auth/logout">로그아웃</a></li>
				</ul>
	    	</c:when>
	    	<c:otherwise>
	    		<a href="/javaMall/adminMall/auth/login.jsp" class="d-flex align-items-center text-white text-decoration-none">
				  <strong>로그인</strong>
				</a>
				<a href="/javaMall/adminMall/auth/join.jsp" class="d-flex align-items-center text-white text-decoration-none">
				  <strong>관리자 등록</strong>
				</a>
	    	</c:otherwise>
	    </c:choose>
    </div>
  </div>
  <div class="b-example-divider b-example-vr"></div>
  <div class="container">
<!-- 내비게이션 영역 [종료] -->