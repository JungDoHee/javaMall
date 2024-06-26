<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean class="util.activatePage" id="activatePageMyInfoNav" scope="page"/>
<jsp:setProperty name="activatePageMyInfoNav" property="curPage" value="${pageContext.request.requestURI}"/>

<div>
	<nav>
		<div class="nav nav-tabs mb-3" id="nav-tab" role="tabList">
			<jsp:setProperty name="activatePageMyInfoNav" property="targetPage" value="/shoppingMall/member/my_info"/>
			<a href="/javaMall/shoppingMall/member/my_info" class="nav-link <%= (activatePageMyInfoNav.getCompare()) ? "active" : "" %>">
				내정보
			</a>
			<jsp:setProperty name="activatePageMyInfoNav" property="targetPage" value="/shoppingMall/member/my_password"/>
			<a href="/javaMall/shoppingMall/member/my_password.jsp" class="nav-link <%= (activatePageMyInfoNav.getCompare()) ? "active" : "" %>">
				비밀번호 수정
			</a>
		</div>
	</nav>
</div>