<%--
 특정 wifi의 세부 정보를 확인 할 때 사용하는 jsp 파일, 올바른 정보를 넘겨받았을 때만 페이지가 정상적으로 활성화된다.
--%>
<%@ page import="db.WifiInformation" %>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
<%@ page import="db.BookmarkGroup" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            width: 20%;
        }
        td {
            border-color:lightgray;
            font-weight: bold;
            width: 80%;
            padding-left:10px;
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
    int maxLengthLatLnt = 11;

    WifiInformation currentWifiInformation = null;

    String parameterId = request.getParameter("id");

    String paramLat = request.getParameter("lat");
    StringBuilder sbLat = new StringBuilder("");

    String paramLnt = request.getParameter("lnt");
    StringBuilder sbLnt = new StringBuilder("");

    List<BookmarkGroup> li_BookmarkGroup = new ArrayList<>();

    if(parameterId != null && paramLat != null && paramLnt != null) {
        try{
            long id = Long.parseLong(parameterId);

            double tmpLat = Double.parseDouble(paramLat);
            if(tmpLat < -90.0) {
                tmpLat = -90.0;
            } else if (tmpLat > 90.0) {
                tmpLat = 90.0;
            }
            sbLat.append(tmpLat);
            if(sbLat.length() > maxLengthLatLnt) sbLat.setLength(maxLengthLatLnt);

            double tmpLnt = Double.parseDouble(paramLnt);
            if(tmpLnt < -180.0) {
                tmpLnt = -180.0;
            } else if (tmpLnt > 180.0) {
                tmpLnt = 180.0;
            }
            sbLnt.append(tmpLnt);
            if(sbLnt.length() > maxLengthLatLnt) sbLnt.setLength(maxLengthLatLnt);

            DBConnection dbConnection = new DBConnection();
            currentWifiInformation = dbConnection.loadWifiInformationFromId(id, Double.parseDouble(sbLat.toString()), Double.parseDouble(sbLnt.toString()));

            dbConnection.loadBookmarkGroupList();
            li_BookmarkGroup = dbConnection.getBookmarkGroupList();
        } catch (NumberFormatException e) {

        }
    }
%>
<h1>와이파이 정보 구하기</h1>
<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>

<p>
    <select name="bookmark_group" onchange="selectName(this.value)" <% if(currentWifiInformation == null) { out.print("disabled"); } %>>
        <option value="">북마크 그룹 이름 선택</option>
        <%
            for(int i = 0; i < li_BookmarkGroup.size(); i++) {
                out.print("<option value=\"" + li_BookmarkGroup.get(i).getName() + "\">" + li_BookmarkGroup.get(i).getName() + "</option>");
            }
        %>
    </select>
    <button type="button" name="insertButton" onclick="insertBookmark()" disabled>북마크 추가하기</button>
</p>
<table>
    <tr>
        <th scope="col">거리(Km)</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(String.format("%.4f", currentWifiInformation.getLength())); } %></td>
    </tr>
    <tr>
        <th scope="col">관리번호</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getMgr_no()); } %></td>
    </tr>
    <tr>
        <th scope="col">자치구</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getWrdofc()); } %></td>
    </tr>
    <tr>
        <th scope="col">와이파이명</th>
        <td class="odd"><a href="javascript:location.reload(true)"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getMain_nm()); } %></a></td>
    </tr>
    <tr>
        <th scope="col">도로명주소</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getAddress1()); } %></td>
    </tr>
    <tr>
        <th scope="col">상세주소</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getAddress2()); } %></td>
    </tr>
    <tr>
        <th scope="col">설치위치(층)</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getInstl_floor()); } %></td>
    </tr>
    <tr>
        <th scope="col">설치유형</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getInstl_ty()); } %></td>
    </tr>
    <tr>
        <th scope="col">설치기관</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getInstl_mby()); } %></td>
    </tr>
    <tr>
        <th scope="col">서비스구분</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getSvc_se()); } %></td>
    </tr>
    <tr>
        <th scope="col">망종류</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getCmcwr()); } %></td>
    </tr>
    <tr>
        <th scope="col">설치년도</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getCnstc_year()); } %></td>
    </tr>
    <tr>
        <th scope="col">실내외구분</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getInout_door()); } %></td>
    </tr>
    <tr>
        <th scope="col">WIFI접속환경</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getRemars3()); } %></td>
    </tr>
    <tr>
        <th scope="col">X좌표</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getLat()); } %></td>
    </tr>
    <tr>
        <th scope="col">Y좌표</th>
        <td class="odd"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getLnt()); } %></td>
    </tr>
    <tr>
        <th scope="col">작업일자</th>
        <td class="even"><% if(currentWifiInformation != null) { out.print(currentWifiInformation.getWork_dttm()); } %></td>
    </tr>
</table>
<script type="text/javascript">
    window.onpageshow = function (e) {
        if (e.persisted) {
            location.reload(true);
        }
        else {
        }
    };

    function selectName(val) {
        var targetButton = document.getElementsByName("insertButton")[0];

        if(val == "") targetButton.disabled = true;
        else targetButton.disabled = false;
    }

    var isWork = false;
    function insertBookmark() {
        var bookmarkName = document.getElementsByName("bookmark_group")[0].value;

        if(bookmarkName != "" && !isWork) {
            isWork = true;
            var wifiId = "<% out.print(currentWifiInformation == null ? "" : currentWifiInformation.getId()); %>";
            fetch("/insertDBPage.jsp", {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({requestName : 'INSERT NEW BOOKMARK', wifiId : wifiId, bookmarkName : "" +bookmarkName})
            }).then((response) => response.text()).then(
                (message) => {
                    if(message == "Insert Complete!") {
                        alert("현재 와이파이 정보에 대한 북마크 생성이 완료되었습니다.");
                    } else if (message == "Already Insert!") {
                        alert("현재 와이파이 정보는 해당 북마크에 이미 들어있습니다.");
                    } else {
                        alert("북마크 생성 도중 문제가 발생하였습니다.");
                        location.reload(true);
                    }
                    isWork = false;
                }
            );
        } else if (bookmarkName == "" && !isWork) {
            isWork = true;
            alert("북마크 이름을 선택해주세요");
            isWork = false;
        }
    }
</script>
</body>
</html>
