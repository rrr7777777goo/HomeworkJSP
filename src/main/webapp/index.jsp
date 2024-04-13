<%--
위치 정보를 기반으로 근처 WIPI 정보를 최대 20개까지 볼 수 있도록 해주는 페이지를 구현하는 jsp파일
--%>
<%@ page import="db.WifiInformation" %>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        table{
            border-collapse : collapse;  /*이중선 제거*/
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
            padding-left: 5px;
            padding-right: 5px;
        }
        td.pgstate0 {
            text-align: center;
            padding-left: 0px;
            padding-right: 0px;
        }
        td.even {
            background-color:white;
        }
        td.odd {
            background-color:#f1f1f1;
        }
    </style>
</head>
<body>
<%
    int pageState = 0; // 0 : 탐색 전 상태 / 1 : 잘못된 값으로 검색하는 상태 / 2 : 올바른 값으로 검색하는 상태
    int maxLengthLatLnt = 11;

    String paramLat = request.getParameter("lat");
    StringBuilder sbLat = new StringBuilder("");

    String paramLnt = request.getParameter("lnt");
    StringBuilder sbLnt = new StringBuilder("");

    if(paramLat == null || paramLnt == null) {
        pageState = 0;
    } else {
        boolean isWrong_LatLntInput = false;
        try {
            double tmpLat = Double.parseDouble(paramLat);
            if(tmpLat < -90.0) {
                tmpLat = -90.0;
            } else if (tmpLat > 90.0) {
                tmpLat = 90.0;
            }
            sbLat.append(tmpLat);
            if(sbLat.length() > maxLengthLatLnt) sbLat.setLength(maxLengthLatLnt);
        } catch (NumberFormatException e) {
            isWrong_LatLntInput = true;
        }
        try {
            double tmpLnt = Double.parseDouble(paramLnt);
            if(tmpLnt < -180.0) {
                tmpLnt = -180.0;
            } else if (tmpLnt > 180.0) {
                tmpLnt = 180.0;
            }
            sbLnt.append(tmpLnt);
            if(sbLnt.length() > maxLengthLatLnt) sbLnt.setLength(maxLengthLatLnt);
        } catch (NumberFormatException e) {
            isWrong_LatLntInput = true;
        }

        if(isWrong_LatLntInput) pageState = 1;
        else pageState = 2;
    }

    List<WifiInformation> li = new ArrayList<>();
    if(pageState == 2) {
        DBConnection dbConnection = new DBConnection();
        dbConnection.loadWifiInformationList(Double.parseDouble(sbLat.toString()), Double.parseDouble(sbLnt.toString()));
        li = dbConnection.getWifiInformationList();
    }

%>
<h1>와이파이 정보 구하기</h1>

<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>

<p>
    <form action="/" method="get">
    LAT: <input type="text" value="<%=sbLat%>" name="lat" maxlength="<%=maxLengthLatLnt%>"> ,
    LNT : <input type="text" value="<%=sbLnt%>" name="lnt" maxlength="<%=maxLengthLatLnt%>">
    <button type="button" onclick="getMyLocation()">내 위치 가져오기</button>
    <button>근처 WIPI 정보 보기</button>
    </form>
</p>

<table>
    <tr>
        <th>거리(Km)</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치(층)</th>
        <th>설치유형</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>망종류</th>
        <th>설치년도</th>
        <th>실내외구분</th>
        <th>WIFI접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>
    <%
        if(pageState == 0) {
            out.println("<tr>");
            out.println("<td colspan=\"17\" class=\"pgstate0\">위치 정보를 입력한 후에 조회해 주세요.</td>");
            out.println("</tr>");
        } else if (pageState == 1) {
            out.println("<tr>");
            out.println("<td colspan=\"17\" class=\"pgstate0\">유효하지 않은 값이 확인되었습니다. 위치 정보값을 다시 입력해 주세요.</td>");
            out.println("</tr>");
        } else if (pageState == 2) {
            if(li.size() == 0) {
                out.println("<tr>");
                out.println("<td colspan=\"17\" class=\"pgstate0\">현재 어떤 와이파이도 탐색되지 않았습니다.</td>");
                out.println("</tr>");
            } else {
                for(int i = 0; i < li.size(); i++) {
                    out.println("<tr>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + String.format("%.4f", li.get(i).getLength()) + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getMgr_no() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getWrdofc() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + "<a href=/detail.jsp?id=" + li.get(i).getId() + "&lat=" +sbLat + "&lnt=" + sbLnt + ">" + li.get(i).getMain_nm() + "</a></td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getAddress1() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getAddress2() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInstl_floor() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInstl_ty() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInstl_mby() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getSvc_se() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getCmcwr() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getCnstc_year() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getInout_door() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getRemars3() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getLat() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getLnt() + "</td>");
                    out.println("<td class=" + (i % 2 == 0 ? "even" : "odd") + ">" + li.get(i).getWork_dttm() + "</td>");
                    out.println("</tr>");
                }
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

    <%-- 현재 위치 좌표를 받아오는 javascript함수 --%>
    function getMyLocation() {
        fetch("http://ip-api.com/json")
            .then((response) => response.json())
            .then((json) => {
                var inputLat = document.getElementsByName("lat")[0];
                var inputLnt = document.getElementsByName("lnt")[0];
                inputLat.value = json.lat;
                inputLnt.value = json.lon;
            });
    }
</script>
</body>
</html>