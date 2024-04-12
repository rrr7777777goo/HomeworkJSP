package com.example.homeworkjsp;

import db.BookmarkGroup;
import db.Bookmark;
import db.LocationSearchHistory;
import db.WifiInformation;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

// jsp파일에서 sqlite db와 서로 통신할 때 사용하는 클래스 파일

public class DBConnection {
    private List<WifiInformation> wifiInformationList;
    private List<LocationSearchHistory> locationSearchHistoryList;
    private List<BookmarkGroup> bookmarkGroupList;
    private List<Bookmark> bookmarkList;
    private StringBuilder dbURL;

    public DBConnection() {
        wifiInformationList = new ArrayList<>();
        locationSearchHistoryList = new ArrayList<>();
        bookmarkGroupList = new ArrayList<>();
        bookmarkList = new ArrayList<>();
        dbURL = new StringBuilder("jdbc:sqlite:C:\\dev\\sqlite3\\wifiDB.db?foreign_keys=1");
    }

    public void loadWifiInformationList(double lat, double lnt) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select *\n" +
                    "from wifi_information\n" +
                    "order by sqrt(power(? - lat, 2) + power(? - lnt, 2))\n" +
                    "limit 20;");
            preparedStatement.setObject(1, lat);
            preparedStatement.setObject(2, lnt);
            rs = preparedStatement.executeQuery();
            preparedStatement.clearParameters();

            while(rs.next()) {
                WifiInformation tmp = new WifiInformation(
                        rs.getLong("id"),
                        rs.getString("mgr_no"),
                        rs.getString("wrdofc"),
                        rs.getString("main_nm"),
                        rs.getString("address1"),
                        rs.getString("address2"),
                        rs.getString("instl_floor"),
                        rs.getString("instl_ty"),
                        rs.getString("instl_mby"),
                        rs.getString("svc_se"),
                        rs.getString("cmcwr"),
                        rs.getString("cnstc_year"),
                        rs.getString("inout_door"),
                        rs.getString("remars3"),
                        rs.getDouble("lat"),
                        rs.getDouble("lnt"),
                        rs.getString("work_dttm")
                );
                tmp.setLength(lat, lnt);
                wifiInformationList.add(tmp);
            }
            preparedStatement = connection.prepareStatement("select ifnull(max(id), 0) as maxID from locationsearch_history;");
            rs = preparedStatement.executeQuery();
            rs.next();
            long newId = rs.getLong("maxID") + 1;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dateStr = (String) simpleDateFormat.format(new Date());

            preparedStatement = connection.prepareStatement("insert into locationsearch_history\n" +
                    "(id, lat, lnt, searchdate)\n" +
                    "VALUES(?, ?, ?, ?);");
            preparedStatement.setObject(1, newId);
            preparedStatement.setObject(2, lat);
            preparedStatement.setObject(3, lnt);
            preparedStatement.setObject(4, dateStr);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public WifiInformation loadWifiInformationFromId(long id, double lat, double lnt) {
        WifiInformation returnValue = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select *\n" +
                            "from wifi_information\n" +
                            "where id = ?;");
            preparedStatement.setObject(1, id);

            rs = preparedStatement.executeQuery();
            preparedStatement.clearParameters();

            while(rs.next()) {
                returnValue = new WifiInformation(
                        rs.getLong("id"),
                        rs.getString("mgr_no"),
                        rs.getString("wrdofc"),
                        rs.getString("main_nm"),
                        rs.getString("address1"),
                        rs.getString("address2"),
                        rs.getString("instl_floor"),
                        rs.getString("instl_ty"),
                        rs.getString("instl_mby"),
                        rs.getString("svc_se"),
                        rs.getString("cmcwr"),
                        rs.getString("cnstc_year"),
                        rs.getString("inout_door"),
                        rs.getString("remars3"),
                        rs.getDouble("lat"),
                        rs.getDouble("lnt"),
                        rs.getString("work_dttm")
                );
                returnValue.setLength(lat, lnt);
                break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return returnValue;
    }

    // SELECT 관련 함수들 ------------
    public void loadLocationSearchHistoryList() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select *\n" +
                            "from locationsearch_history\n" +
                            "order by id desc\n" +
                            "limit 20;");
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                locationSearchHistoryList.add(
                        new LocationSearchHistory(
                                rs.getLong("id"),
                                rs.getDouble("lat"),
                                rs.getDouble("lnt"),
                                rs.getString("searchdate")
                        )
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadBookmarkList() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select bgroup.id as bookmark_id, li.name_bookmark_group as bookmark_name, li.id_wifi_information as wifi_info_id, winfo.main_nm as wifi_info_name, li.insertdate as insertdate, winfo.lat as wifi_info_lat, winfo.lnt as wifi_info_lnt \n" +
                            "from wifi_information winfo, bookmark_group bgroup, bookmark_list li \n" +
                            "where name_bookmark_group = bgroup.name and winfo.id = id_wifi_information\n" +
                            "order by bgroup.`order`, bgroup.id, insertdate;");
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                bookmarkList.add(
                        new Bookmark(
                                rs.getLong("bookmark_id"),
                                rs.getString("bookmark_name"),
                                rs.getLong("wifi_info_id"),
                                rs.getString("wifi_info_name"),
                                rs.getString("insertdate"),
                                rs.getDouble("wifi_info_lat"),
                                rs.getDouble("wifi_info_lnt")
                        )
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadBookmarkGroupList() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select * from bookmark_group order by `order`, id, insertdate");
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                bookmarkGroupList.add(
                        new BookmarkGroup(
                                rs.getString("name"),
                                rs.getLong("id"),
                                rs.getLong("order"),
                                rs.getString("insertdate"),
                                rs.getString("updatedate")
                        )
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Long loadBookmarkGroupOrder(String name) {
        Long returnValue = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement(
                    "select `order` from bookmark_group where name = ?");
            preparedStatement.setObject(1, name);
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                returnValue = rs.getLong("order");
                break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return returnValue;
    }

    public Bookmark loadBookmark(long wifiInformationId, String bookmarkGroupName) {
        Bookmark returnValue = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement("select bgroup.id as bookmark_id, li.name_bookmark_group as bookmark_name, li.id_wifi_information as wifi_info_id, winfo.main_nm as wifi_info_name, li.insertdate as insertdate, winfo.lat as wifi_info_lat, winfo.lnt as wifi_info_lnt\n" +
                    "from wifi_information winfo, bookmark_group bgroup, bookmark_list li \n" +
                    "where li.id_wifi_information = ? and li.name_bookmark_group = ? and name_bookmark_group = bgroup.name and winfo.id = id_wifi_information;");
            preparedStatement.setObject(1, wifiInformationId);
            preparedStatement.setObject(2, bookmarkGroupName);

            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                returnValue = new Bookmark(
                        rs.getLong("bookmark_id"),
                        rs.getString("bookmark_name"),
                        rs.getLong("wifi_info_id"),
                        rs.getString("wifi_info_name"),
                        rs.getString("insertdate"),
                        rs.getDouble("wifi_info_lat"),
                        rs.getDouble("wifi_info_lnt")
                );
                break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return returnValue;
    }
    // ------------ SELECT 관련 함수들

    // INSERT 관련 함수들 ------------
    public int insertBookmark(long wifiId, String bookmarkName) {
        int completeValue = 0; // 0 : 외래키 오류 발생 / 1 : 이미 북마크에 존재 / 2 : insert 완료
        if(bookmarkName == null) {
            return completeValue;
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String dateStr = (String) simpleDateFormat.format(new Date());

            preparedStatement = connection.prepareStatement("select * " +
                    "from bookmark_list\n" +
                    "where name_bookmark_group = ? and id_wifi_information = ?;");
            preparedStatement.setObject(1, bookmarkName);
            preparedStatement.setObject(2, wifiId);
            rs = preparedStatement.executeQuery();
            preparedStatement.clearParameters();

            boolean isAlreadyIn = false;

            while(rs.next()) {
                isAlreadyIn = true;
                break;
            }

            if(isAlreadyIn) {
                completeValue = 1;
            } else {
                preparedStatement = connection.prepareStatement("insert into bookmark_list(name_bookmark_group, id_wifi_information, insertdate)\n" +
                        "VALUES(?, ?, ?);");
                preparedStatement.setObject(1, bookmarkName);
                preparedStatement.setObject(2, wifiId);
                preparedStatement.setObject(3, dateStr);
                preparedStatement.executeUpdate();
                preparedStatement.clearParameters();
                completeValue = 2;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return completeValue;
    }

    public int insertBookmarkGroup(String bookmarkName, long bookmarkOrder) {
        int completeValue = 0; // 0 : 외래키 오류 발생 / 1 : 추가하려고 하는 이름의 북마크가 이미 존재 / 2 : 업데이트 완료
        if(bookmarkName == null) {
            return completeValue;
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());

            boolean isAlreadyIn = false;

                preparedStatement = connection.prepareStatement("select * \n" +
                        "from bookmark_group\n" +
                        "where name = ? ;");
                preparedStatement.setObject(1, bookmarkName);
                rs = preparedStatement.executeQuery();
                preparedStatement.clearParameters();

                while(rs.next()) {
                    isAlreadyIn = true;
                    break;
                }

            if(isAlreadyIn) {
                completeValue = 1;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String dateStr = (String) simpleDateFormat.format(new Date());

                preparedStatement = connection.prepareStatement("insert into bookmark_group(name, id, `order`, insertdate, updatedate)\n" +
                        "values(?, (select ifnull(max(id), 0)+1 from bookmark_group), ?, ?, ?);");
                preparedStatement.setObject(1, bookmarkName);
                preparedStatement.setObject(2, bookmarkOrder);
                preparedStatement.setObject(3, dateStr);
                preparedStatement.setObject(4, "");
                preparedStatement.executeUpdate();
                preparedStatement.clearParameters();
                completeValue = 2;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return completeValue;
    }
    // ------------ INSERT 관련 함수들

    // UPDATE 관련 함수들 ------------
    public int updateBookmarkGroup(String originalBookmarkName, String bookmarkName, long bookmarkOrder) {
        int completeValue = 0; // 0 : 외래키 오류 발생 / 1 : 바꾸려고 하는 이름의 북마크가 이미 존재 / 2 : 업데이트 완료

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());

            boolean isAlreadyIn = false;

            if(originalBookmarkName.compareTo(bookmarkName) != 0) {
                preparedStatement = connection.prepareStatement("select * " +
                        "from bookmark_group\n" +
                        "where name = ? ;");
                preparedStatement.setObject(1, bookmarkName);
                rs = preparedStatement.executeQuery();
                preparedStatement.clearParameters();

                while(rs.next()) {
                    isAlreadyIn = true;
                    break;
                }
            }

            if(isAlreadyIn) {
                completeValue = 1;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String dateStr = (String) simpleDateFormat.format(new Date());

                preparedStatement = connection.prepareStatement("update bookmark_group\n" +
                        "set name = ?, `order` = ?, updatedate = ? \n" +
                        "where name = ?;");
                preparedStatement.setObject(1, bookmarkName);
                preparedStatement.setObject(2, bookmarkOrder);
                preparedStatement.setObject(3, dateStr);
                preparedStatement.setObject(4, originalBookmarkName);
                preparedStatement.executeUpdate();
                preparedStatement.clearParameters();
                completeValue = 2;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return completeValue;
    }
    // ------------ UPDATE 관련 함수들

    // DELETE 관련 함수들 ------------
    public void deleteLocationSearchHistory(long id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());
            preparedStatement = connection.prepareStatement("delete from locationsearch_history where id = ?;");
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int deleteBookmarkGroup(String bookmarkName) {
        int completeValue = 0; // 0 : 외래키 오류 발생 / 1 : 업데이트 완료

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());

            preparedStatement = connection.prepareStatement("delete \n" +
                    "from bookmark_group \n" +
                    "where name = ?;");
            preparedStatement.setObject(1, bookmarkName);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
            completeValue = 1;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return completeValue;
    }

    public int deleteBookmark(long wifiId, String bookmarkName) {
        int completeValue = 0; // 0 : 외래키 오류 발생 / 1 : 업데이트 완료

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbURL.toString());

            preparedStatement = connection.prepareStatement("delete \n" +
                    "from bookmark_list \n" +
                    "where name_bookmark_group = ? and id_wifi_information = ?;");
            preparedStatement.setObject(1, bookmarkName);
            preparedStatement.setObject(2, wifiId);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
            completeValue = 1;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
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
                if(connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return completeValue;
    }
    // ------------ DELETE 관련 함수들

    public List<WifiInformation> getWifiInformationList() {
        return wifiInformationList;
    }
    public List<LocationSearchHistory> getLocationSearchHistoryList() { return locationSearchHistoryList; }
    public List<Bookmark> getBookmarkListList() { return bookmarkList; }
    public List<BookmarkGroup> getBookmarkGroupList() { return bookmarkGroupList; }

}
