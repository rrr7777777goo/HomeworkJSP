package db;

// 북마크 그룹 정보를 저장할 때 쓰는 클래스

public class BookmarkGroup {
    private String name;
    private long id;
    private long order;
    private String insertdate;
    private String updatedate;


    public BookmarkGroup(String name, long id, long order, String insertdate, String updatedate) {
        this.name = name;
        this.id = id;
        this.order = order;
        this.insertdate = insertdate;
        this.updatedate = updatedate;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public long getOrder() {
        return order;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }
}
