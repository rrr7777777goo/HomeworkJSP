<%--
 북마크 그룹 목록을 출력하는 jsp 파일
--%>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
<%@ page import="java.util.*" %>
<%@ page import="db.BookmarkGroup" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>와이파이 정보 구하기</title>
  <style>
    table{ border-collapse : collapse;  /*이중선 제거*/
      margin-top:20px;
      width:100%;
    }
    th,td{
      height: 50px;
      border: 1px solid;

      vertical-align: top;	/* 위 */
      vertical-align: bottom;   /* 아래 */
      vertical-align: middle;   /* 가운데 */
    }
    th {
      color:white;
      background-color:#00AA6F;
      border-color:white;
      font-weight: bold;
      padding-left: 5px;
      padding-right: 5px;
    }
    td {
      border-color:lightgray;
      font-weight: bold;
      padding-left: 5px;
      padding-right: 5px;
    }
    td.even {
      background-color:white;
    }
    td.odd {
      background-color:#f1f1f1;
    }
    td.pgstate0 {
      text-align: center;
      padding-left:0px;
      padding-right: 0px;
    }
    div {
      text-align:center;
    }
  </style>
</head>
<body>
<%
  List<BookmarkGroup> li = new ArrayList<>();
  DBConnection dbConnection = new DBConnection();
  dbConnection.loadBookmarkGroupList();
  li = dbConnection.getBookmarkGroupList();
%>
<h1>북마크 그룹</h1>

<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>
<button type="button" onclick="location.href='bookmark_group_add.jsp'">북마크 그룹 이름 추가</button>
<table>
  <tr>
    <th scope="col">ID</th>
    <th scope="col">북마크 이름</th>
    <th scope="col">순서</th>
    <th scope="col">등록일자</th>
    <th scope="col">수정일자</th>
    <th scope="col">비고</th>
  </tr>
<%
  if(li.size() == 0) {
    out.println("<tr>");
    out.println("<td colspan=\"6\" class=\"pgstate0\"></td>");
    out.println("</tr>");
  } else {
    for(int i = 0; i < li.size(); i++) {
      out.println("<tr>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getId() + "</td>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getName() + "</td>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getOrder() + "</td>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInsertdate() + "</td>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getUpdatedate() + "</td>");
      out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + "<div><a href='/bookmark_group_edit.jsp?name=" + li.get(i).getName() + "'>수정</a> <a href='/bookmark_group_remove.jsp?name=" + li.get(i).getName() +"'>삭제</a></div></td>");
      out.println("</tr>");
    }
  }
%>
</table>
<script type="text/javascript">
  window.onpageshow = function (e) {
    if (e.persisted) {
      location.reload(true);
    }
    else {
    }
  };

</script>
</body>
</html>