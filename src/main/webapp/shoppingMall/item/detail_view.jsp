<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>

<div class="album py-5 bg-body-tertiary">
	<div class="container">
		<div class="row row-cols-2 row-cols-md-2 g-2">
			<!-- 썸네일 표시 영역 [시작] -->
			<figure class="figure col">
				<img src="${ strImageUrl }${ itemInfo[0].getThumbName() }" alt="${ itemInfo[0].getThumbName() }" class="bd-placeholder-img figure-img img-fluid rounded">	
			</figure>
			<!-- 썸네일 표시 영역 [종료] -->
			
			<!-- 물품 정보 표시 영역 [시작] -->
			<div class="col">
				<span class="badge bg-warning-subtle text-warning-emphasis rounded-pill">${ itemInfo[0].getItemStatusKor()[itemInfo[0].getItemStatus()] }</span>
				<h2 class="sticky-xl-top fw-bold pt-3 pt-xl-5 pb-2 pb-xl-3">${ itemInfo[0].getItemSubject() }</h2>
				<p><fmt:formatNumber value="${ itemInfo[0].getItemPrice() }" pattern="#,###"/> 원</p>
				<form name="orderFrm" method="post" action="/javaMall/shoppingMall/item/order" onsubmit="return beforeCheck()">
					<input type="hidden" name="item_seq" value="${ itemInfo[0].getItemSeq() }">
					<input type="hidden" name="origin_price" value="${ itemInfo[0].getItemPrice() }">
					<input type="hidden" name="payment_price">
					<input type="number" class="form-control" name="order_amount" id="order_amount" min="1" required onChange="amountCheck()">
					<div class="none" id="paymentPriceHelp"></div>
					<input type="submit" class="w-100 btn btn-primary" id="order_btn" value="구매하기" ${ itemInfo[0].getItemStatus() eq "a" ? "" : "disabled" }>
				</form>
				<hr/>
				
				<!-- 장바구니 표시 영역 [시작] -->
				<c:if test="${sessionScope.user_no > 0}">
					<form name="addCartForm" method="post" action="/javaMall/shoppingMall/item/cart/add">
						<input type="hidden" name="item_seq" value="${ itemInfo[0].getItemSeq() }">
						<div class="row">
							<div class="col-md-1">
								<i class="fas fa-shopping-cart"></i>
							</div>
							<div class="col-md-6">
				            	<input type="number" class="form-control" name="add_cart_amount" value="${cartInfo.getAmount()}" required>
				            </div>
				            <div class="col-md-4">
					            <c:choose>
					            	<c:when test="${ cartInfo.getAmount() > 0 }">
					            		<input type="submit" class="btn btn-sm btn-outline-primary" value="장바구니 수정">
					            		<a href="/javaMall/shoppingMall/item/cart/delete?item_seq=${itemInfo[0].getItemSeq()}" class="btn btn-sm btn-outline-danger">장바구니 비우기</a>
					            	</c:when>
					            	<c:otherwise>
					            		<input type="submit" class="btn btn-outline-primary" value="장바구니 담기">
					            	</c:otherwise>
					            </c:choose>
				            </div>
			            </div>	
					</form>
				</c:if>
				<!-- 장바구니 표시 영역 [종료] -->
				
				<hr/>
				<p class="mt-3">${ itemInfo[0].getItemInfo() }</p>
			</div>
			<!-- 물품 정보 표시 영역 [종료] -->
		</div>
	</div>
</div>

<hr/>

<!-- 이미지 표시 영역 [시작] -->
<div class="container text-center">
	<c:forEach var="imageList" items="${ itemInfo }" varStatus="loop">
		<img src="${ strImageUrl }${ imageList.getImageName() }" alt="${ imageList.getImageName() }" class="bd-placeholder-img figure-img img-fluid">
	</c:forEach>
</div>
<!-- 이미지 표시 영역 [종료] -->

<script>
	function beforeCheck(){
		const submitFrm = document.orderFrm;
		if( submitFrm.order_amount.value < 1 ) {
			alert("1개 이상 구매할 수 있습니다");
			return false;
		}
		return true;
	}
	
	function amountCheck(){
		const submitFrm = document.orderFrm;
		if( submitFrm.order_amount.value < 1 ) {
			return false;
		}
		document.getElementById("order_btn").value = submitFrm.order_amount.value+"개 구매하기";
		
		let nOriginPrice = submitFrm.origin_price.value;
		let paymentPriceHelp = document.getElementById("paymentPriceHelp");
		submitFrm.payment_price.value = (nOriginPrice * submitFrm.order_amount.value);
		
		
		if( paymentPriceHelp.classList.contains("none") ) {
			paymentPriceHelp.classList.remove("none");
		} else {
			paymentPriceHelp.classList.add("none");
		}
		document.getElementById("paymentPriceHelp").innerText = "구매 금액 : " + submitFrm.payment_price.value;
	}
</script>

<%@ include file="/shoppingMall/include/footer.jsp"%>