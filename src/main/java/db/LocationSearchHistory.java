package db;

// 위치 정보 검색 기록을 저장할 때 사용하는 클래스

public class LocationSearchHistory {
    private long id;
    private double lat;
    private double lnt;
    private String searchdate;

    public LocationSearchHistory(long id, double lat, double lnt, String searchdate) {
        this.id = id;
        this.lat = lat;
        this.lnt = lnt;
        this.searchdate = searchdate;
    }

    public long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLnt() {
        return lnt;
    }

    public String getSearchdate() {
        return searchdate;
    }
}
