<%--
 북마크 그룹 정보를 편집할 때 사용하는 jsp 파일, 올바른 북마크 그룹 정보를 넘겨받았을 때만 페이지가 정상적으로 활성화된다.
--%>
<%@ page import="com.example.homeworkjsp.DBConnection" %>
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
            padding-left: 10px;
        }
        td.even {
            background-color:white;
        }
        td.odd {
            background-color:#f1f1f1;
        }
        td.pgstate0 {
            padding-left: 0px;
        }
        div {
            text-align:center;
        }
    </style>
</head>
<body>
<%
    String bookmarkGroupName = request.getParameter("name");
    Long bookmarkGroupOrder = null;

    boolean isDisable = true;
    if(bookmarkGroupName != null) {
        try{
            DBConnection dbConnection = new DBConnection();
            bookmarkGroupOrder = dbConnection.loadBookmarkGroupOrder(bookmarkGroupName);

            if(bookmarkGroupOrder != null) {
                isDisable = false;
            }
        } catch (NumberFormatException e) {

        }
    }
%>
<h1>북마크 그룹 수정</h1>
<p><a href="/">홈</a> | <a href="/history.jsp">위치 히스토리 목록</a> | <a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> | <a href="/bookmark_list.jsp">북마크 보기</a> | <a href="/bookmark_group.jsp">북마크 그룹 관리</a></p>

<table>
    <tr>
        <th scope="col">북마크 이름</th>
        <td class="even"><input type="text" name="name_bookmark" maxlength="20" value="<% out.print(isDisable ? "" : bookmarkGroupName); %>"<% out.print(isDisable ? " disabled" : ""); %> /></td>
    </tr>
    <tr>
        <th scope="col">순서</th>
        <td class="odd"><input type="text" name="order_bookmark" maxlength="3" value="<% out.print(isDisable ? "" : bookmarkGroupOrder); %>"<% out.print(isDisable ? " disabled" : ""); %> /></td>
    </tr>
    <tr>
        <td colspan=2 class="even pgstate0"><div><a href="javascript:history.go(-1);">돌아가기</a> | <button type="button" onclick="updateBookmark()" <% out.print(isDisable ? " disabled" : ""); %>>수정</button></div></td>
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

    var isWork = false;
    function updateBookmark() {
        var name_bookmark = document.getElementsByName("name_bookmark")[0].value;
        name_bookmark = name_bookmark.substring(0, 20);
        name_bookmark = name_bookmark.trim();

        var order_bookmark = document.getElementsByName("order_bookmark")[0].value;
        if(!isWork) {
            isWork = true;

            if(name_bookmark == "" || order_bookmark == "" || isNaN(order_bookmark)) {
                alert("올바른 값을 입력해주세요");
                isWork = false;
            } else {
                if(order_bookmark[0] < '0' || order_bookmark[9] > '9') {
                    alert("올바른 값을 입력해주세요");
                    isWork = false;
                } else {
                    var originalBookmarkName = "<% out.print(bookmarkGroupName == null ? "" : bookmarkGroupName); %>";
                    fetch("/updateDBPage.jsp", {
                        method: 'POST',
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({requestName : 'UPDATE BOOKMARKGROUP', originalBookmarkName : "" + originalBookmarkName,  bookmarkName : "" + name_bookmark, bookmarkOrder : "" + order_bookmark})
                    }).then((response) => response.text()).then(
                        (message) => {
                            console.log(message);
                            if(message == "Update Complete!") {
                                alert("현재 북마크 그룹 정보 수정이 완료되었습니다.");
                                location.replace("/bookmark_group_edit.jsp?name=" + name_bookmark);
                            } else if (message == "Name is Already In!") {
                                alert("이미 존재하는 북마크 그룹 이름이라 바꿀 수 없습니다. 다른 이름을 입력해주세요.");
                                isWork = false;
                            }else {
                                alert("수정 도중 문제가 발생하였습니다.");
                                location.reload(true);
                            }
                        }
                    );
                }
            }

        }
    }
</script>
</body>
</html>
