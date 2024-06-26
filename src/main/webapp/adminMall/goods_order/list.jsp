<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="py-5">
<div class="container">
	<main>
		<table class="table table-hover">
			<thead>
				 <tr class="align-middle text-center">
					<th scope="col">회원번호</th>
					<th scope="col">분류</th>
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
							<input type="hidden" name="order_seq" value="${orderList.getOrderSeq()}">
							<td>${ orderList.getUserNo() }</td>
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
								<select class="form-control" name="order_status" data-order_seq="${orderList.getOrderSeq()}" onChange="fnSetStatus(this)">
									<c:forEach var="orderStatus" items="${rgOrderStatusInfo}">
										<option value="${orderStatus.key}" ${orderStatus.key eq orderList.getOrderStatus() ? "selected" : ""}>${orderStatus.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			</tbody>
		</table>
	</main>
</div>
</div>

${pagination}

<script>
	function fnSetStatus(obj) {
		console.log(obj.value, obj.dataset.order_seq);
		if( !confirm("상태를 변경하시겠습니까?") ) {
			return false;
		}		
		
		const xhr = new XMLHttpRequest();
		const ajaxUrl = "/javaMall/adminMall/goods_order/list";
		
 		let params = "";
 		params += "order_seq=" + obj.dataset.order_seq;
 		params += "&";
 		params += "order_status=" + obj.value;
		
		xhr.open("POST", ajaxUrl, true);
		xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		xhr.send(params);
		
		xhr.onload = function(){
			if( xhr.status == 200 ) {
				
			} else {
				alert("상태 변경 실패했습니다");
				return false;
			}
		}
	}
</script>
<%@ include file="/adminMall/include/footer.jsp"%>