<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<c:if test="${empty sessionScope.admin_no}">
	<script>
		alert("로그인 후 이용하시기 바랍니다");
		location.href = "/javaMall/adminMall/item_manage/list";
	</script>
</c:if>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<%@ include file="/adminMall/include/item_manage_nav.jsp" %>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- form 영역 [시작] -->
	<div>
		<form action="/javaMall/adminMall/item_manage/item_reg/reg" method="POST" enctype="multipart/form-data">
			<div class="row g-6">
				<div class="mb-3">
					<label for="item_category" class="form-label">상품분류</label>
		            <select name="item_category" class="form-control" id="item_category" required>
						<c:forEach items="${rgCategory}" var="category_list" varStatus="list_key">
							<option value="${category_list.getCategorySeq()}">${category_list.getCategoryName()}</option>						
						</c:forEach>
					</select>
					
	          	</div>
				<div class="mb-3">
					<label for="item_title" class="form-label">제목</label>
		            <input type="text" class="form-control" id="item_title" name="item_title" aria-describedby="itemTitleHelp" maxlength="50" required>
		            <div id="itemTitleHelp" class="form-text">최소 1자, 최대 50자, 등록 가능 문자 제한 없음</div>
	          	</div>
	          	<div class="row g-6">
		          	<div class="col-md-6">
						<label for="item_price" class="form-label">금액</label>
			            <input type="number" class="form-control" id="item_price" name="item_price" aria-describedby="itemPriceHelp" required>
			            <div id="itemPriceHelp" class="form-text">숫자만 입력</div>
		          	</div>
		          	<div class="col-md-6">
						<label for="item_stock" class="form-label">수량</label>
			            <input type="number" class="form-control" id="item_stock" name="item_stock" aria-describedby="stockPriceHelp" required>
			            <div id="stockPriceHelp" class="form-text">숫자만 입력</div>
		          	</div>
	          	</div>
	          	<div class="mb-3">
					<label for="item_contents" class="form-label">물품 정보</label>
					<textarea rows="10" class="form-control" id="item_contents" name="item_contents"></textarea>
	          	</div>
	          	<div class="mb-3">
					<label for="item_thumb" class="form-label">썸네일</label>
					<input type="file" class="form-control" id="item_thumb" name="item_thumb" required>
	          	</div>
	
	          	<div class="row">
	          		<div class="col mb-3">
						<label for="item_image_1" class="form-label">사진 1</label>
						<input type="file" class="form-control" data-image_seq="1" id="item_image_1" name="item_image[]">
	          		</div>
	          		<div class="col-2">
	          			<button type="button" class="btn btn-outline-secondary" onclick="fnAddFileList()">파일 추가</button>
	          		</div>
	          	</div>
	          	<div id="add_file_list"></div>
	          	<hr>
	          	<input type="submit" class="w-100 btn btn-primary btn-lg" value="등록">
          	</div>
		</form>
		<a class="w-100 btn btn-lg btn-outline-secondary" href="/adminMall/item_manage/list.jsp">이전</a>
	</div>
	<!-- form 영역 [종료] -->
</div>
<%@ include file="/adminMall/include/footer.jsp"%>

<script>
	function fnAddFileList() {
		const create_image_index = document.getElementsByName("item_image[]").length +1;

		const divFileList = document.getElementById("add_file_list");
		const newDiv = document.createElement("DIV");
		newDiv.classList.add("mb-3");
		const newLabel = document.createElement("LABEL");
		newLabel.setAttribute("for", "item_image_" + create_image_index);
		newLabel.classList.add("form-label");
		newLabel.innerText = "사진 "+create_image_index;
		const newInput = document.createElement("input");
		newInput.setAttribute("type", "file");
		newInput.setAttribute("id", "item_image_" + create_image_index);
		newInput.setAttribute("name", "item_image[]");
		newInput.setAttribute("data-image_seq", create_image_index);
		newInput.classList.add("form-control");
		newDiv.appendChild(newLabel);
		newDiv.appendChild(newInput);
		divFileList.appendChild(newDiv);
	}
</script>