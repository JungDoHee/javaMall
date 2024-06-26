<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<%@ include file="/adminMall/include/category_manage_nav.jsp" %>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- 하단 리스트 [시작] -->
	<div class="row">
		<div class="row row-cols-md-3 mb-3 text-center text-bg-dark">
			<div class="col themed-grid-col">카테고리</div>
			<div class="col themed-grid-col">등록된 상품 수량</div>
			<div class="col themed-grid-col">비고</div>
		</div>
	</div>
	
	<c:choose>
		<c:when test="${ empty rgList }">
			<div class="row">
				<div>조회된 분류가 없습니다</div>
			</div>
		</c:when>
		<c:otherwise>
			<c:forEach items="${ rgList }" var="category_list" varStatus="list_key">
				<div class="row">
					<div class="row">
						<div class="row row-cols-md-3 mb-3 text-center">
							<div class="col themed-grid-col">${ category_list.getCategoryName() }</div>
							<div class="col themed-grid-col"><fmt:formatNumber value="${ category_list.getCategoryCount() }" pattern="#,###"/></div>
							<div class="col themed-grid-col">
								<c:if test="${not empty sessionScope.admin_no}">
									<a href="/javaMall/adminMall/category_manage/category_modify_form?category_seq=${category_list.getCategorySeq()}" class="btn btn-sm btn-outline-primary">수정하기</a>
									<form method="post" action="/javaMall/adminMall/category_manage/category_reg/delete">
										<input type="hidden" name="category_seq" value="${category_list.getCategorySeq()}">
										<input type="submit" class="btn btn-sm btn-danger" value="삭제하기">
									</form>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	<!-- 하단 리스트 [종료] -->
</div>

${ pagination }
          
<%@ include file="/adminMall/include/footer.jsp"%>