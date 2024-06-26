<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ include file="/adminMall/include/header.jsp"%>

<c:if test="${empty sessionScope.admin_no}">
	<script>
// 		alert("로그인 후 이용하시기 바랍니다");
// 		location.href = "/javaMall/adminMall/category_manage/list";
	</script>
</c:if>

<div class="py-5">
	<!-- 상단 탭 영역 [시작] -->
	<%@ include file="/adminMall/include/category_manage_nav.jsp" %>
	<!-- 상단 탭 영역 [종료] -->
	
	<!-- form 영역 [시작] -->
	<div>
		<form action="/javaMall/adminMall/category_manage/category_reg/modify" method="POST">
			<input type="hidden" name="category_seq" value="${ rgCategoryInfo.getCategorySeq() }">
			<div class="mb-3">
				<label for="category_name" class="form-label">카테고리명</label>
	            <input type="text" class="form-control" id="category_name" name="category_name" maxlength="50" aria-describedby="categoryNameHelp" placeholder="${ rgCategoryInfo.getCategoryName() }" required>
	            <div id="categoryNameHelp" class="form-text">기존 카테고리명 : ${ rgCategoryInfo.getCategoryName() }</div>
          </div>
          <hr>
          <input type="submit" class="w-100 btn btn-primary btn-lg" value="수정">
		</form>
		<a class="w-100 btn btn-lg btn-outline-secondary" href="/adminMall/category_manage/list">이전</a>
	</div>
	<!-- form 영역 [종료] -->
</div>
<%@ include file="/adminMall/include/footer.jsp"%>