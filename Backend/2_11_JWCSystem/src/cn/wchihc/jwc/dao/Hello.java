package cn.wchihc.jwc.dao;


import cn.wchihc.jwc.dao.base.BaseDao;
import cn.wchihc.jwc.model.users.Student;

import java.util.ArrayList;

public class Hello {
    public static void main(String[] args) {
/*        Query query = new Query(
                "",
                "",
                "",
                "",
                "",
                1,
                10
        );
        BaseDao<Faculty> f = customSelect(
                " f.*," +
                        " (select count(*) from student s where f.id = s.f_id) as num_student, " +
                        " (select count(*) from teacher t where f.id = t.f_id) as num_teacher",
                Faculty.class,
                "f"
        ).where().like("name",query.getContent());
        int count = f.getTotalPageSize(10);
        System.out.println(count);

        f.byPage(1, 10)
                .executeCustomQuery(FacultyCustom.class)
                .forEach(System.out::println);*/

/*        List<ScoreReportCustom> query = customSelect("course.*, score", Course.class)
                .append("left")
                .join("elective", "course.id = elective.cs_id")
                .where(eq("st_id", 2))
                .executeCustomQuery(ScoreReportCustom.class);


        query.forEach(i -> {
            List<ClassStudentCustom> students = customSelect("rank() over (order by score desc ) as s_rank, name,sex,f_id,score,st_id as s_id", Elective.class)
                    .append("left").join("student s", "elective.st_id = s.id")
                    .where(eq("cs_id", i.getId())).executeCustomQuery(ClassStudentCustom.class);
            i.setSNum(students.size());
            for (ClassStudentCustom e : students) {
                if (e.getSId().equals(2)) {
                    i.setSRank(e.getSRank());
                    return;
                }
            }
        });*/
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("ccsd", "fdsfsf", "fs"));
        students.add(new Student("ewew", "fds332fsf", "fs"));
        students.add(new Student("fdsfssfsf", "fdsfssfsf", "fs"));
        students.add(new Student("fdsfddsf", "fdsfssfsf", "fs"));
        students.add(new Student("fdsffafsf", "fdsfssfsf", "fs"));
        students.add(new Student("fdsafdfsf", "fdsfssfsf", "fs"));
        students.add(new Student("fdsfasffsf", "fdsfssfsf", "fs"));
        long start = System.currentTimeMillis();
        BaseDao.batchInsert(Student.class, students).executeUpdate();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
      /*  add(user).executeUpdate();

        List<User> users =
                select(User.class).where("id=3")
                        .executeQuery();
//        boolean b = del(user).executeUpdate();
        users.forEach(System.out::println);*/

    }
}
