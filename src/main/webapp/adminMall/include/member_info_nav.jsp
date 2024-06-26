<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean class="util.activatePage" id="activatePageMemberInfoNav" scope="page"/>
<jsp:setProperty name="activatePageMemberInfoNav" property="curPage" value="${pageContext.request.requestURI}"/>

<div>
	<nav>
		<div class="nav nav-tabs mb-3" id="nav-tab" role="tabList">
			<jsp:setProperty name="activatePageMemberInfoNav" property="targetPage" value="/adminMall/member_info/list"/>
			<a href="/javaMall/adminMall/member_info/list" class="nav-link <%= (activatePageMemberInfoNav.getCompare()) ? "active" : "" %>">
				고객 목록
			</a>
		</div>
	</nav>
</div>