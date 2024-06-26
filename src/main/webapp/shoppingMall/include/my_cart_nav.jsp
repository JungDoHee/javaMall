<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean class="util.activatePage" id="activatePageMyCartNav" scope="page"/>
<jsp:setProperty name="activatePageMyCartNav" property="curPage" value="${pageContext.request.requestURI}"/>

<div>
	<nav>
		<div class="nav nav-tabs mb-3" id="nav-tab" role="tabList">
			<jsp:setProperty name="activatePageMyCartNav" property="targetPage" value="/shoppingMall/member/my_cart"/>
			<a href="/javaMall/shoppingMall/member/my_cart" class="nav-link <%= (activatePageMyCartNav.getCompare()) ? "active" : "" %>">
				장바구니
			</a>
			<jsp:setProperty name="activatePageMyCartNav" property="targetPage" value="/shoppingMall/member/my_order"/>
			<a href="/javaMall/shoppingMall/member/my_order" class="nav-link <%= (activatePageMyCartNav.getCompare()) ? "active" : "" %>">
				주문내역
			</a>
		</div>
	</nav>
</div>