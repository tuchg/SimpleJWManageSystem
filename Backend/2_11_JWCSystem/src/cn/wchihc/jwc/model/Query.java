package cn.wchihc.jwc.model;

/**
 * 通用查询字串
 */
public class Query {

    /**
     * content :
     * sort :
     * faculty :
     * type:
     * title :
     * page : 1
     * limit : 20
     */
    private int id = 1;
    private String content;
    //搜索的类别 默认正序
    private int sort = 1;
    //类别
    private String type;
    //院系
    private String faculty;
    //职称
    private String title;
    //状态
    private String status;
    //第几页
    private int page = 1;
    //每页多少
    private int limit = 10;

    public Query() {
    }

    public Query(String content, int sort, String type, String faculty, String title, int page, int limit) {
        this.content = content;
        this.sort = sort;
        this.type = type;
        this.faculty = faculty;
        this.title = title;
        this.page = page;
        this.limit = limit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
