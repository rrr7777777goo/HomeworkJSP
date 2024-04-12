<%--
외부 api에서 db정보를 가져오게 해주는 jsp 파일, DBLoadFromJSONAPI 클래스를 이용한다.
외부에서 db정보를 가져오게 되면 기존에 있던 북마크 정보들은 모두 삭제된다.
--%>
<%@ page import="com.example.homeworkjsp.DBLoadFromJSONAPI" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        h1, div {
            text-align : center;
        }
    </style>
</head>
<body>
<%
    DBLoadFromJSONAPI dbLoadFromJSONAPI = new DBLoadFromJSONAPI();
    long cnt = dbLoadFromJSONAPI.getTotalCount();
%>
<h1><%=cnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
<div><a href="/">홈 으로 가기</a></div>
</body>
</html>
