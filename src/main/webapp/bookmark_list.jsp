<%--
 북마크 목록을 출력하는 jsp 파일
--%>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
<%@ page import="java.util.*" %>
<%@ page import="db.Bookmark" %>
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
        }
        td {
            border-color:lightgray;
            font-weight: bold;
        }
        td.pgstate0 {
            text-align: center;
        }
        td.even {
            background-color:white;
        }
        td.odd {
            background-color:#f1f1f1;
        }
        div {
            text-align:center;
        }
    </style>
</head>
<body>
<%
    List<Bookmark> li = new ArrayList<>();
    DBConnection dbConnection = new DBConnection();
    dbConnection.loadBookmarkList();
    li = dbConnection.getBookmarkListList();
%>
<h1>북마크 목록</h1>

<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>
<table>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">북마크 이름</th>
        <th scope="col">와이파이명</th>
        <th scope="col">등록일자</th>
        <th scope="col">비고</th>
    </tr>
    <%
        if(li.size() == 0) {
            out.println("<tr>");
            out.println("<td colspan=\"5\" class=\"pgstate0\"></td>");
            out.println("</tr>");
        } else {
            for(int i = 0; i < li.size(); i++) {
                out.println("<tr>");
                out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getId_bookmark_group() + "</td>");
                out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getName_bookmark_group() + "</td>");
                out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + "<a href=/detail.jsp?id=" + li.get(i).getId_wifi_information() + "&lat=" + li.get(i).getLat_wifi_information() + "&lnt=" + li.get(i).getLnt_wifi_information() +">" + li.get(i).getName_wifi_information() + "</a></td>");
                out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInsertdate() + "</td>");
                out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + "<div><a href='/bookmark_delete.jsp?id=" + li.get(i).getId_wifi_information() + "&name=" + li.get(i).getName_bookmark_group() +"'>삭제</a></td>");
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