<%@ page import = "java.io.BufferedReader" %><%@
        page import="java.io.InputStream" %><%@
        page import="java.io.InputStreamReader" %><%@
        page import="org.json.simple.parser.JSONParser" %><%@
        page import="org.json.simple.JSONObject" %><%@
        page import="org.json.simple.parser.ParseException" %><%@
        page import="com.example.homeworkjsp.DBConnection" %><%@
        page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%

    // DB에 UPDATE 작업을 하기 위해서 fetch를 통해 값을 넘겨받는 jsp파일
    // post방식을 이용해서 값을 넘겨받으면 작업이 진행되고 만약 그냥 접속했을 경우에는 백지 화면만 뜬다.
    // 전달 메세지에 문제가 발생할 수 있으므로 위의 page관련 코드는 꼭 저상태로 둔다! (<> 밖에서 엔터키 발생하면 안됨)
    // 깃허브 커밋 및 푸시 테스트용

    InputStream inputStr = request.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStr));

    StringBuilder sb = new StringBuilder("");
    String s = br.readLine();
    while(s != null) {
        sb.append(s);
        s = br.readLine();
    }

    if(sb.toString().compareTo("") != 0) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(sb.toString());
            String requestType = (String) jsonObject.get("requestName");

            if(requestType == null) {
                System.out.println("requestType is Null");
            } else if (requestType.compareTo("UPDATE BOOKMARKGROUP") == 0) {
                String originalBookmarkName = (String) jsonObject.get("originalBookmarkName");
                String bookmarkName = (String) jsonObject.get("bookmarkName");
                String bookmarkOrder = (String) jsonObject.get("bookmarkOrder");

                DBConnection dbConnection = new DBConnection();
                int completeState = dbConnection.updateBookmarkGroup(originalBookmarkName, bookmarkName, Long.parseLong(bookmarkOrder));

                switch (completeState) {
                    case 0:
                        out.print("Update Error!");
                        break;
                    case 1:
                        out.print("Name is Already In!");
                        break;
                    case 2:
                        out.print("Update Complete!");
                        break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
%>