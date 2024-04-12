package com.example.homeworkjsp;

import db.WifiInformation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// jsp파일에서 서울시 공공 와이파이 정보를 넘겨받기 위해서 사용하는 클래스 파일

public class DBLoadFromJSONAPI {
    List<WifiInformation> wifiInformationList = new ArrayList<>();

    private long totalCount = 0;

    public DBLoadFromJSONAPI() {
        try {
            String key = "4b62464e6d6a67683931694f777966"; // 인증키

            StringBuilder domain = new StringBuilder("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/1/1"); // 접속할 url 주소값 저장용

            URL url = new URL(domain.toString()); // url 객체 생성

            JSONParser jsonParser = new JSONParser();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")); // json 값 읽어오기

            JSONObject jsonObject = (JSONObject) jsonParser.parse(br.readLine());

            JSONObject tbPublicWifiInfo = (JSONObject)jsonObject.get("TbPublicWifiInfo");

            JSONArray jsonArray = null;

            totalCount = (Long) tbPublicWifiInfo.get("list_total_count");

            long startIndex = 1;
            long endIndex = startIndex + 999;

            long wifiID = 1;
            while(startIndex <= totalCount) {
                domain.setLength(0);
                domain.append("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/" + startIndex + "/" + endIndex);
                url = new URL(domain.toString());

                br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                jsonObject = (JSONObject) jsonParser.parse(br.readLine());
                tbPublicWifiInfo = (JSONObject)jsonObject.get("TbPublicWifiInfo");
                jsonArray = (JSONArray)tbPublicWifiInfo.get("row");

                if(jsonArray != null) {
                    for(int i = 0; i < jsonArray.size(); i++) {
                        JSONObject tmpJSON = (JSONObject) jsonArray.get(i);
                        String mgr_no = (String) tmpJSON.get("X_SWIFI_MGR_NO");
                        String wrdofc = (String) tmpJSON.get("X_SWIFI_WRDOFC");
                        String main_nm = (String) tmpJSON.get("X_SWIFI_MAIN_NM");
                        String address1 = (String) tmpJSON.get("X_SWIFI_ADRES1");
                        String address2 = (String) tmpJSON.get("X_SWIFI_ADRES2");
                        String instl_floor = (String) tmpJSON.get("X_SWIFI_INSTL_FLOOR");
                        String instl_ty = (String) tmpJSON.get("X_SWIFI_INSTL_TY");
                        String instl_mby = (String) tmpJSON.get("X_SWIFI_INSTL_MBY");
                        String svc_se = (String) tmpJSON.get("X_SWIFI_SVC_SE");
                        String cmcwr = (String) tmpJSON.get("X_SWIFI_CMCWR");
                        String cnstc_year = (String) tmpJSON.get("X_SWIFI_CNSTC_YEAR");
                        String inout_door = (String) tmpJSON.get("X_SWIFI_INOUT_DOOR");
                        String remars3 = (String) tmpJSON.get("X_SWIFI_REMARS3");
                        Double lat = Double.parseDouble((String) tmpJSON.get("LAT"));
                        Double lnt = Double.parseDouble((String) tmpJSON.get("LNT"));
                        String work_dttm = (String) tmpJSON.get("WORK_DTTM");

                        wifiInformationList.add(new WifiInformation(wifiID++, mgr_no, wrdofc, main_nm, address1, address2, instl_floor, instl_ty, instl_mby, svc_se, cmcwr, cnstc_year, inout_door, remars3, lat, lnt, work_dttm));
                    }
                }

                startIndex = endIndex + 1;
                endIndex = startIndex + 999;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        String dbURL = "jdbc:sqlite:C:\\dev\\sqlite3\\wifiDB.db?foreign_keys=1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL);
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement("pragma foreign_keys = 1");
            preparedStatement.execute();
            connection.commit();

            preparedStatement = connection.prepareStatement("delete from wifi_information;");
            int deleteResult = preparedStatement.executeUpdate();
            connection.commit();


            preparedStatement = connection.prepareStatement("insert into wifi_information" +
                    "(id, mgr_no, wrdofc, main_nm, address1, address2, instl_floor, instl_ty, instl_mby, svc_se, cmcwr, cnstc_year, inout_door, remars3, lat, lnt, work_dttm)" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            int informationCount = 1;
            for(WifiInformation info : wifiInformationList) {
                preparedStatement.setObject(1, info.getId());
                preparedStatement.setObject(2, info.getMgr_no());
                preparedStatement.setObject(3, info.getWrdofc());
                preparedStatement.setObject(4, info.getMain_nm());
                preparedStatement.setObject(5, info.getAddress1());
                preparedStatement.setObject(6, info.getAddress2());
                preparedStatement.setObject(7, info.getInstl_floor());
                preparedStatement.setObject(8, info.getInstl_ty());
                preparedStatement.setObject(9, info.getInstl_mby());
                preparedStatement.setObject(10, info.getSvc_se());
                preparedStatement.setObject(11, info.getCmcwr());
                preparedStatement.setObject(12, info.getCnstc_year());
                preparedStatement.setObject(13, info.getInout_door());
                preparedStatement.setObject(14, info.getRemars3());
                preparedStatement.setObject(15, info.getLat());
                preparedStatement.setObject(16, info.getLnt());
                preparedStatement.setObject(17, info.getWork_dttm());

                preparedStatement.addBatch();

                preparedStatement.clearParameters();

                if(informationCount % 5000 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    connection.commit();
                }
                ++informationCount;
            }

            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            try {
                if(rs != null && !rs.isClosed()) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(preparedStatement != null && !preparedStatement.isClosed()) preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if(connection != null) {
                    connection.setAutoCommit(true);
                    if(!connection.isClosed()) connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getTotalCount() {
        return totalCount;
    }
}
