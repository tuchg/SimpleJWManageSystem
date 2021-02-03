package cn.wchihc.jwc.model.custom;

public class FacultyCustom {

    /**
     * id : 1
     * name :
     * numStudent :
     * numTeacher :
     */

    private Integer id;
    private String name;
    private Long numStudent;
    private Long numTeacher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumStudent() {
        return numStudent;
    }

    public void setNumStudent(Long numStudent) {
        this.numStudent = numStudent;
    }

    public Long getNumTeacher() {
        return numTeacher;
    }

    public void setNumTeacher(Long numTeacher) {
        this.numTeacher = numTeacher;
    }

    @Override
    public String toString() {
        return "FacultyCustom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numStudent=" + numStudent +
                ", numTeacher=" + numTeacher +
                '}';
    }
}
