package db;

// 와이파이 정보를 저장할 때 사용하는 클래스

public class WifiInformation {
    private double length;
    private long id;
    private String mgr_no;
    private String wrdofc;
    private String main_nm;
    private String address1;
    private String address2;
    private String instl_floor;
    private String instl_ty;
    private String instl_mby;
    private String svc_se;
    private String cmcwr;
    private String cnstc_year;
    private String inout_door;
    private String remars3;
    private double lat;
    private double lnt;
    private String work_dttm;


    public WifiInformation(long id, String mgr_no, String wrdofc, String main_nm, String address1, String address2, String instl_floor, String instl_ty, String instl_mby, String svc_se, String cmcwr, String cnstc_year, String inout_door, String remars3, double lat, double lnt, String work_dttm) {
        length = 0.0;
        this.id = id;
        this.mgr_no = mgr_no;
        this.wrdofc = wrdofc;
        this.main_nm = main_nm;
        this.address1 = address1;
        this.address2 = address2;
        this.instl_floor = instl_floor;
        this.instl_ty = instl_ty;
        this.instl_mby = instl_mby;
        this.svc_se = svc_se;
        this.cmcwr = cmcwr;
        this.cnstc_year = cnstc_year;
        this.inout_door = inout_door;
        this.remars3 = remars3;
        this.lat = lat;
        this.lnt = lnt;
        this.work_dttm = work_dttm;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double lat, double lnt) {
        this.length = Math.sqrt(Math.pow((this.lat - lat), 2) + Math.pow((this.lnt - lnt), 2));
    }

    public long getId() {
        return id;
    }

    public String getMgr_no() {
        return mgr_no;
    }

    public String getWrdofc() {
        return wrdofc;
    }

    public String getMain_nm() {
        return main_nm;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getInstl_floor() {
        return instl_floor;
    }

    public String getInstl_ty() {
        return instl_ty;
    }

    public String getInstl_mby() {
        return instl_mby;
    }

    public String getSvc_se() {
        return svc_se;
    }

    public String getCmcwr() {
        return cmcwr;
    }

    public String getCnstc_year() {
        return cnstc_year;
    }

    public String getInout_door() {
        return inout_door;
    }

    public String getRemars3() {
        return remars3;
    }

    public double getLat() {
        return lat;
    }

    public double getLnt() {
        return lnt;
    }

    public String getWork_dttm() {
        return work_dttm;
    }
}
