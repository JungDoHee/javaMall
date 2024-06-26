<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>

<main>
	<div class="album py-5 bg-body-tertiary">
		<!-- 더보기 영역 [시작] -->
		<div class="container my-2">
			<div class="btn-group">
				<a href="/javaMall/shoppingMall/item/list" class="btn btn-sm btn-outline-secondary">더보기</a>
			</div>
		</div>
		<!-- 더보기 영역 [종료] -->
	
		<div class="container">
			<div class="row row-cols-1 row-cols-md-3 g-3">
				<!-- 물품 내용 [시작] -->
				<c:forEach var="itemList" items="${ rgItemList }" varStatus="loop">
					<div class="col">
						<div class="card shadow-sm">
						
							<img src="${ strImageUrl }${ itemList.getThumbName() }" class="bd-placeholder-img card-img-top">
							<div class="card-body">
								<p class="card-text">${ itemList.getItemSubject() }</p>
								<div class="d-flex justify-content-between align-items-center">
									<div class="btn-group">
										<a href="/javaMall/shoppingMall/item/detail_view?item_seq=${ itemList.getItemSeq() }" class="btn btn-sm btn-outline-secondary">물품 상세보기</a>
									</div>
									<small class="text-body-secondary"><fmt:formatNumber value="${ itemList.getItemPrice() }" pattern="#,###"/>원</small>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<!-- 물품 내용 [종료] -->
			</div>
		</div>
	</div>
</main>

<%@ include file="include/footer.jsp"%>