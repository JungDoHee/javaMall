<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean class="util.activatePage" id="activatePageItemNav" scope="page"/>
<jsp:setProperty name="activatePageItemNav" property="curPage" value="${pageContext.request.requestURI}"/>

<div>
	<nav>
		<div class="nav nav-tabs mb-3" id="nav-tab" role="tabList">
			<jsp:setProperty name="activatePageItemNav" property="targetPage" value="/adminMall/item_manage/list"/>
			<a href="/javaMall/adminMall/item_manage/list" class="nav-link <%= (activatePageItemNav.getCompare()) ? "active" : "" %>">
				물품 내역
			</a>
			<jsp:setProperty name="activatePageItemNav" property="targetPage" value="/adminMall/item_manage/item_reg_form"/>
			<a href="/javaMall/adminMall/item_manage/item_reg_form" class="nav-link <%= (activatePageItemNav.getCompare()) ? "active" : "" %>">
				물품 등록
			</a>
		</div>
	</nav>
</div>