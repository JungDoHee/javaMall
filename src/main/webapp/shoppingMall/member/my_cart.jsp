<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>


<div class="container">

	<%@ include file="/shoppingMall/include/my_cart_nav.jsp" %>

	<div class="row row-cols-1 row-cols-md-2 g-2">
		<!-- 물품 내용 [시작] -->
		<c:choose>
			<c:when test="${ rgList.size() < 1 }">
				<div class="col">장바구니 내역이 없습니다</div>
			</c:when>
			<c:otherwise>
				<c:forEach var="itemList" items="${ rgList }" varStatus="loop">
					<div class="col">
						<div class="card shadow-sm">
							<img src="${ strImageUrl }${ itemList.getThumbName() }" class="bd-placeholder-img card-img-top">
							<div class="card-body">
								<p class="card-text">${ itemList.getItemSubject() }</p>
								<div class="d-flex justify-content-between align-items-center">
									<div class="btn-group">
										<a href="/javaMall/shoppingMall/item/detail_view?item_seq=${ itemList.getItemSeq() }" class="btn btn-sm btn-outline-secondary">물품 상세보기</a>
									</div>
									<small class="text-body-secondary">장바구니 수량 : <fmt:formatNumber value="${ itemList.getCartAmount() }" pattern="#,###"/>개</small>
									<small class="text-body-secondary"><fmt:formatNumber value="${ itemList.getItemPrice() }" pattern="#,###"/>원</small>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		
		<!-- 물품 내용 [종료] -->
	</div>
</div>

<!-- 페이징 [시작] -->
${ pagination }
<!-- 페이징 [종료] -->

<%@ include file="/shoppingMall/include/footer.jsp"%>