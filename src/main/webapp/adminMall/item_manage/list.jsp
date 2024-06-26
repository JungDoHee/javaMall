<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<%@ include file="/adminMall/include/item_manage_nav.jsp" %>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- 하단 리스트 [시작] -->
	<div class="row  row-cols-1 row-cols-md-2 g-4">
		<c:choose>
			<c:when test="${ empty rgList }">
				<div>조회된 물품이 없습니다</div>
			</c:when>
			<c:otherwise>
				<c:forEach items="${ rgList }" var="list" varStatus="list_key">
					<div class="col">
						<div class="card">
							<img class="bd-placeholder-img card-img-top" src="${ strImageUrl }${ list.getThumbName() }">
							<title>${ list.getThumbName() }</title>
							<div class="card-body">
								<h5 class="card-title">${ list.getItemSubject() }</h5>
								<p class="card-text">금액
									<small class="text-body-secondary"><fmt:formatNumber value="${ list.getItemPrice() }" pattern="#,###"/>원</small>
								</p>
								<p class="card-text">등록자
									<small class="text-body-secondary">${ list.getAdminNo() }</small>
								</p>
								<p class="card-text">재고
									<small class="text-body-secondary">${ list.getInitStock() }</small>
								</p>
								<p class="card-text">분류
									<small class="text-body-secondary">${ rgCategory[list.getCategorySeq()] }</small>
								</p>
								<p class="card-text">등록일자
									<small class="text-body-secondary">${ list.getRegDate() }</small>
								</p>
								<div class="d-flex justify-content-between align-items-center">
									<a href="/javaMall/adminMall/item_manage/item_modify_form?seq=${list.getItemSeq()}" class="btn btn-primary">수정하기</a>
									<c:choose>
										<c:when test="${ list.getItemStatus() == 'a' }">
											<span class="badge bg-warning-subtle text-warning-emphasis rounded-pill">정상</span>
										</c:when>
										<c:when test="${ list.getItemStatus() == 'b' }">
											<span class="badge bg-primary-subtle text-warning-emphasis rounded-pill">품절</span>
										</c:when>
										<c:otherwise>
											<span class="badge bg-danger-subtle text-warning-emphasis rounded-pill">삭제</span>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
	<!-- 하단 리스트 [종료] -->
	
</div>          
          
<%@ include file="/adminMall/include/footer.jsp"%>