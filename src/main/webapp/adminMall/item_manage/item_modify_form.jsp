<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<div class="d-flex justify-content-between align-items-center">
		<h3>물품 수정</h3>
		<form action="/javaMall/adminMall/item_manage/item_reg/delete" method="POST">
			<input type="hidden" name="item_seq" value="${ rgItemInfo[0].getItemSeq() }">
			<input type="submit" class="btn btn-outline-secondary" value="물품 삭제">
		</form>
	</div>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- form 영역 [시작] -->
	<div>
		<form action="/javaMall/adminMall/item_manage/item_reg/modify" method="POST" enctype="multipart/form-data">
			<div class="row g-6">
				<input type="hidden" name="item_seq" value="${ rgItemInfo[0].getItemSeq() }">
				<div class="mb-3">
					<label for="item_category" class="form-label">상품분류</label>
		            <select name="item_category" class="form-control" id="item_category" required>
						<c:forEach items="${rgCategory}" var="category_list" varStatus="list_key">
							<option value="${category_list.getCategorySeq()}" ${ (category_list.getCategorySeq() eq rgItemInfo[0].getCategorySeq()) ? "selected" : "" }>${category_list.getCategoryName()}</option>						
						</c:forEach>
					</select>
					
	          	</div>
				<div class="mb-3">
					<label for="item_subject" class="form-label">제목</label>
		            <input type="text" class="form-control" id="item_subject" name="item_subject" aria-describedby="itemTitleHelp" maxlength="50" value="${ rgItemInfo[0].getItemSubject() }" required>
		            <div id="itemTitleHelp" class="form-text">최소 1자, 최대 50자, 등록 가능 문자 제한 없음</div>
	          </div>
	          <div class="row g-6">
		          <div class="col-md-6">
						<label for="item_price" class="form-label">금액</label>
			            <input type="number" class="form-control" id="item_price" name="item_price" aria-describedby="itemPriceHelp" value="${ rgItemInfo[0].getItemPrice() }" required>
			            <div id="itemPriceHelp" class="form-text">숫자만 입력</div>
		          </div>
		          <div class="col-md-6">
						<label for="item_stock" class="form-label">수량</label>
			            <input type="number" class="form-control" id="item_stock" name="item_stock" aria-describedby="stockPriceHelp" value="${ rgItemInfo[0].getInitStock() }" required>
			            <div id="stockPriceHelp" class="form-text">숫자만 입력</div>
		     	</div>
	     	</div>
	          <div class="mb-3">
	          		<label for="item_status" class="form-label">물품 상태</label>
	          		<select class="form-select" id="item_status" name="item_status">
	          			<option value="a" ${ rgItemInfo[0].getItemStatus() == "a" ? "selected" : ""}>정상</option>
	          			<option value="b" ${ rgItemInfo[0].getItemStatus() == "b" ? "selected" : ""}>품절</option>
	          		</select>
	          </div>
	          <div class="mb-3">
					<label for="item_info" class="form-label">물품 정보</label>
					<textarea rows="10" class="form-control" id="item_info" name="item_info">${ rgItemInfo[0].getItemInfo() }</textarea>
	          </div>
	          <div class="mb-3">
					<label for="item_thumb" class="form-label">썸네일</label>
					<input type="file" class="form-control" id="item_thumb" name="item_thumb">
					<div id="itemPriceHelp" class="form-text">파일 입력 시 썸네일이 수정됩니다</div>
	          </div>
	          <div class="mb-3">
					<label for="item_image" class="form-label">사진1</label>
					<input type="file" class="form-control" id="item_image" name="item_image">
					<div id="itemPriceHelp" class="form-text">동적 파일 영역 추가는 추후 작업</div>
					<div id="itemPriceHelp" class="form-text">파일 입력 시 이미지가 수정됩니다</div>
	          </div>
	          <hr>
	          <input type="submit" class="w-100 btn btn-primary btn-lg" value="수정">
          </div>
		</form>
		<a href="/javaMall/adminMall/item_manage/list" class="btn w-100 btn-outline-secondary btn-lg">닫기</a>
	</div>
	<!-- form 영역 [종료] -->
</div>          

<%@ include file="/adminMall/include/footer.jsp"%>