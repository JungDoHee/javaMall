<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/shoppingMall/include/header.jsp"%>

<div class="container">
	<%@ include file="/shoppingMall/include/my_cart_nav.jsp" %>
	<main>
		<table class="table table-hover">
			<thead>
				 <tr class="align-middle text-center">
					<th scope="col" >분류</th>
					<th scope="col">구매 상품</th>
					<th scope="col">상품명</th>
					<th scope="col">단가</th>
					<th scope="col">할인받은 금액</th>
					<th scope="col">수량</th>
					<th scope="col">총 결제 금액</th>
					<th scope="col">비고</th>
				 </tr>
			</thead>
			<tbody>
			<c:choose>
				<c:when test="${rgList.size() < 1}">
					<tr>
						<td colspan="7">주문 내역이 없습니다</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="orderList" items="${ rgList }" varStatus="loop">
						<tr class="align-middle">
							<td>${ rgCategory[orderList.getCategorySeq()] }</td>
							<td>
								<img src="${ strImageUrl }${ orderList.getImageName() }" style="height:100px">
							</td>
							<td >${ orderList.getItemSubject() }</td>
							<td><fmt:formatNumber value="${ orderList.getUnitPrice() }" pattern="#,###"/></td>
							<td><fmt:formatNumber value="${ orderList.getDiscountedPrice() }" pattern="#,###"/></td>
							<td><fmt:formatNumber value="${ orderList.getAmount() }" pattern="#,###"/></td>
							<td><fmt:formatNumber value="${ orderList.getPaymentPrice() }" pattern="#,###"/></td>
							<td>
								<c:choose>
									<c:when test="${orderList.getOrderStatus() eq 'a'}">
										<button type="button" class="btn btn-secondary" data-type="del" data-order_seq="${orderList.getOrderSeq()}" onClick="fnCancelOrder(this)">주문 취소</button>
									</c:when>
									<c:otherwise>
										${rgOrderStatusInfo[orderList.getOrderStatus()]}
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			</tbody>
		</table>
	</main>
</div>

${pagination}

<form name="cancelFrm" method="POST">
	<input type="hidden" name="order_seq">
	<input type="hidden" name="method_type">
</form>

<script>
	function fnCancelOrder(obj){
		const strCancelUrl = "/javaMall/shoppingMall/member/my_order";
		const cancelFrm = document.cancelFrm;
		
		if( obj.dataset.order_seq == null || obj.dataset.order_seq < 1 ) {
			alert("주문을 취소할 수 없습니다. 새로고침 후 다시 이용하시기 바랍니다.");
			return false;
		} else if( obj.dataset.type != "del" ) {
			alert("잘못된 경로로 접근했습니다.");
			return false;
		}
		cancelFrm.order_seq.value = obj.dataset.order_seq;
		cancelFrm.method_type.value = obj.dataset.type;
		cancelFrm.action = strCancelUrl;
		cancelFrm.submit();
	}
</script>

<%@ include file="/shoppingMall/include/footer.jsp"%>