package db;

// 북마크 정보를 저장할 때 쓰는 클래스

public class Bookmark {
    private long id_bookmark_group;
    private String name_bookmark_group;
    private long id_wifi_information;
    private String name_wifi_information;
    private String insertdate;

    private double lat_wifi_information;
    private double lnt_wifi_information;

    public Bookmark(long id_bookmark_group, String name_bookmark_group, long id_wifi_information, String name_wifi_information, String insertdate, double lat_wifi_information, double lnt_wifi_information) {
        this.id_bookmark_group = id_bookmark_group;
        this.name_bookmark_group = name_bookmark_group;
        this.id_wifi_information = id_wifi_information;
        this.name_wifi_information = name_wifi_information;
        this.insertdate = insertdate;
        this.lat_wifi_information = lat_wifi_information;
        this.lnt_wifi_information = lnt_wifi_information;
    }

    public long getId_bookmark_group() {
        return id_bookmark_group;
    }

    public String getName_bookmark_group() {
        return name_bookmark_group;
    }

    public long getId_wifi_information() {
        return id_wifi_information;
    }

    public String getName_wifi_information() {
        return name_wifi_information;
    }

    public double getLat_wifi_information() {
        return lat_wifi_information;
    }

    public double getLnt_wifi_information() {
        return lnt_wifi_information;
    }

    public String getInsertdate() {
        return insertdate;
    }
}
