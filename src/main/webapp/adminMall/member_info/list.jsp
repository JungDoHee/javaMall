<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<%@ include file="/adminMall/include/member_info_nav.jsp" %>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- 하단 리스트 [시작] -->
	<div class="row">
		<div class="row row-cols-md-5 mb-5 text-center text-bg-dark">
			<div class="col themed-grid-col">회원번호</div>
			<div class="col themed-grid-col">아이디</div>
			<div class="col themed-grid-col">성명</div>
			<div class="col themed-grid-col">주소</div>
			<div class="col themed-grid-col">전화</div>
		</div>
	</div>
	<c:choose>
		<c:when test="${ empty rgList }">
			<div class="row">
				<div>조회된 회원이 없습니다</div>
			</div>
		</c:when>
		<c:otherwise>
			<c:forEach items="${ rgList }" var="member_list" varStatus="list_key">
				<div class="row">
					<div class="row row-cols-md-5 mb-5 text-center">
						<div class="col themed-grid-col">${ member_list.getUserNo() }</div>
						<div class="col themed-grid-col">${ member_list.getUserId() }</div>
						<div class="col themed-grid-col">${ member_list.getUserName() }</div>
						<div class="col themed-grid-col">(${ member_list.getUserZipCode() }) ${ member_list.getUserAddress() }</div>
						<div class="col themed-grid-col">${ member_list.getUserPhone() }</div>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	<!-- 하단 리스트 [종료] -->
</div>

${pagination}

<%@ include file="/adminMall/include/footer.jsp"%>