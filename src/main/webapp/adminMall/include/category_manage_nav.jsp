<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean class="util.activatePage" id="activatePageCategoryNav" scope="page"/>
<jsp:setProperty name="activatePageCategoryNav" property="curPage" value="${pageContext.request.requestURI}"/>

<div>
	<nav>
		<div class="nav nav-tabs mb-3" id="nav-tab" role="tabList">
			<jsp:setProperty name="activatePageCategoryNav" property="targetPage" value="/adminMall/category_manage/list"/>
			<a href="/javaMall/adminMall/category_manage/list" class="nav-link <%= (activatePageCategoryNav.getCompare()) ? "active" : "" %>">
				분류 내역
			</a>
			<jsp:setProperty name="activatePageCategoryNav" property="targetPage" value="/adminMall/category_manage/category_reg_form"/>
			<a href="/javaMall/adminMall/category_manage/category_reg_form.jsp" class="nav-link <%= (activatePageCategoryNav.getCompare()) ? "active" : "" %>">
				분류 등록
			</a>
		</div>
	</nav>
</div>