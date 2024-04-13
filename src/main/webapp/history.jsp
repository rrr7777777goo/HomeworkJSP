<%--
올바른 형태의 위치 정보 검색기록을 조회하는 jsp 파일, 최대 20개까지 출력이 가능하며 위치 정보 삭제도 가능하다.
--%>
<%@ page import="db.LocationSearchHistory" %>
<%@ page import="java.util.*" %>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        table {
            border-collapse : collapse;  /*이중선 제거*/
            margin-top : 20px;
            width : 100%;
        }
        th,td {
            height : 50px;
            border : 1px solid;

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
            width: 80%;
            padding-left:5px;
            padding-right:5px;
        }
        td.pgstate0 {
            text-align: center;
            padding-left:0px;
            padding-right:0px;
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
    List<LocationSearchHistory> li = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        dbConnection.loadLocationSearchHistoryList();
        li = dbConnection.getLocationSearchHistoryList();
%>
<h1>와이파이 정보 구하기</h1>

<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>

<table>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">X좌표</th>
        <th scope="col">Y좌표</th>
        <th scope="col">조회일자</th>
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
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getId() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getLat() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getLnt() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getSearchdate() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + "<div><button type=\"button\" onclick=\"deleteHistory(" + li.get(i).getId() + ")\">삭제</button></div></td>");
                    out.println("</tr>");
                }
            }
    %>
</table>
<script type="text/javascript">
    function deleteHistory(idValue) {
            fetch("/deleteDBPage.jsp", {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({requestName : 'DELETE LOCAL SEARCH HISTORY', id : "" + idValue})
            }).then((response) => response.text()).then(
                (message) => {
                    location.reload(true);
                }
            );
    }
</script>
</body>
</html>