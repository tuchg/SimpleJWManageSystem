package cn.wchihc.jwc.dao.base;

import cn.wchihc.jwc.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 简易链式动态SQL
 * 基于泛型,反射构建(入侵式)
 * 支持数据库下划线命名法->POJO驼峰命名法
 * @author wzc
 * @param <T>
 */
@SuppressWarnings("ALL")
public class BaseDao<T> {

    private StringBuffer sb = new StringBuffer();
    private T object;
    private Connection conn = DatabaseHelper.getConn();

    /**
     * 拒绝非规定方法实例化本类
     */

    private BaseDao() {
    }


    /**
     * 自定义查询
     *
     * @param clazz POJO
     * @return
     */
    public static <C> BaseDao<C> customSelect(String selectPart, Class<C> clazz) {
        BaseDao<C> baseDao = new BaseDao<>();
        StringBuffer sb = baseDao.sb;
        sb.append("select ").append(selectPart).append(" from ").append(clazz.getSimpleName().toLowerCase());
        try {
            //创建一个实例
            baseDao.object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    /**
     * 别名自定义查询
     *
     * @param clazz POJO
     * @return
     */
    public static <C> BaseDao<C> customSelect(String selectPart, Class<C> clazz, String alias) {
        return customSelect(selectPart, clazz).append(" ").append(alias).append(" ");
    }


    /**
     * 查
     *
     * @param object 需查询的
     * @return
     */
    public static BaseDao<Object> select(Object object) {
        BaseDao<Object> baseDao = new BaseDao<>();
        StringBuffer sb = baseDao.sb;
        baseDao.object = object;
        Class clazz = object.getClass();
        sb.append("select * from ").append(clazz.getSimpleName().toLowerCase()).append(" where ");
        try {
            Field[] fields = clazz.getDeclaredFields();


            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //解除私有域的限制
                field.setAccessible(true);
                //解除私有域的限制后 再进行where
                if (i == 0) continue;
                if (field.get(object) != null)
                    sb.append(" ").append(StringUtil.HumpToUnderline(field.getName())).append(" = '").append(field.get(object)).append("' and ");
            }
            //剔除末尾的 and
            int len = sb.length();
            sb.delete(len - 4, len);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return baseDao;
    }

    /**
     * 默认根据首位字段构造SQL条件
     * 删
     *
     * @param object
     * @return
     */
    public static BaseDao del(Object object) {
        BaseDao baseDao = new BaseDao();
        StringBuffer sb = baseDao.sb;
        baseDao.object = object;
        Class clazz = object.getClass();


        sb.append("delete from ").append(clazz.getSimpleName().toLowerCase()).append(" where ");

        try {
            //默认根据首位进行删除
            Field field = clazz.getDeclaredFields()[0];
//            name = 'value'
            //获取父级属性
            field.setAccessible(true);

            if (field.get(object) == null)
                field = clazz.getFields()[0];

            field.setAccessible(true);

            sb.append(" ").append(StringUtil.HumpToUnderline(field.getName())).append(" = '").append(field.get(object)).append("'");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    /**
     * 改
     *
     * @param object
     * @return
     */
    public static BaseDao update(Object object) {
        BaseDao baseDao = new BaseDao();
        StringBuffer sb = baseDao.sb;
        baseDao.object = object;
        Class clazz = object.getClass();


        sb.append("update ").append(clazz.getSimpleName().toLowerCase()).append(" set ");

        try {

            Field[] fields = clazz.getDeclaredFields();
            //将继承的属性取出
            ArrayList<Field> fields2 = new ArrayList<>(Arrays.asList(clazz.getFields()));
            fields2.addAll(Arrays.asList(fields));
            fields = fields2.toArray(new Field[0]);

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //解除私有域的限制
                field.setAccessible(true);
                //解除私有域的限制后 再进行where
                if (i == 0)
                    continue;

                if (field.get(object) != null)
                    sb.append(" ").append(StringUtil.HumpToUnderline(field.getName())).append(" = '").append(field.get(object)).append("',");

            }

            //剔除末尾的 ,
            baseDao.delRear();
            //指定ID
            sb.append(" where ").append(StringUtil.HumpToUnderline(fields[0].getName())).append(" = '").append(fields[0].get(object)).append("'");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return baseDao;
    }

    /**
     * 批量插入
     * 存在则插入,不存在则替换
     *
     * @param t
     * @param list
     * @param <C>
     * @return
     */
    public static <C> BaseDao<C> batchInsert(Class<C> clazz, List<C> list) {
        BaseDao<C> baseDao = new BaseDao<>();
        StringBuffer sb = baseDao.sb;
        sb.append(" replace into ").append(clazz.getSimpleName().toLowerCase());
        try {
            Field[] fields = clazz.getDeclaredFields();
            //拉入父级属性8afdddfg
            ArrayList<Field> fields2 = new ArrayList<>(Arrays.asList(clazz.getFields()));
            fields2.addAll(Arrays.asList(fields));
            fields = fields2.toArray(new Field[0]);

            sb.append(" (");
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                if (field.get(list.get(0)) != null)
                    sb.append(StringUtil.HumpToUnderline(field.getName())).append(",");
            }
            //剔除末尾的 ,
            baseDao.delRear();
            sb.append(") ");
            sb.append("values ");
            for (C obj : list) {
                ArrayList<Object> objects = new ArrayList<>();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    //解除私有域的限制
                    field.setAccessible(true);
                    Object o = field.get(obj);
                    if (o != null)
                        objects.add(o);
                }
                //将当前对象属性放至(,,,,)
                baseDao.values(objects.toArray());
                baseDao.append(",");
            }
            //删除多余的,
            baseDao.delRear();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return baseDao;
    }

    /**
     * 增
     *
     * @param object
     * @return
     */
    public static BaseDao insert(Object object) {

        BaseDao baseDao = new BaseDao();
        StringBuffer sb = baseDao.sb;
        baseDao.object = object;
        Class clazz = object.getClass();


        sb.append("insert into ").append(clazz.getSimpleName().toLowerCase());

        try {
            Field[] fields = clazz.getDeclaredFields();
            //拉入父级属性
            ArrayList<Field> fields2 = new ArrayList<>(Arrays.asList(clazz.getFields()));
            fields2.addAll(Arrays.asList(fields));
            fields = fields2.toArray(new Field[0]);

            sb.append(" (");
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                //忽略ID项
                if (i == 0) continue;
                if (field.get(object) != null)
                    sb.append(StringUtil.HumpToUnderline(field.getName())).append(",");
            }
            //剔除末尾的 ,
            baseDao.delRear();
            sb.append(") ");
            sb.append("values (");

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                //解除私有域的限制
                field.setAccessible(true);

                if (i == 0) continue;
                //修复 空指针异常
                if (field.get(object) != null)
                    sb.append(" '").append(field.get(object)).append("',");
            }
            baseDao.delRear();
            sb.append(") ");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    /**
     * 完全自定义SQL
     * 必须使用自定义查询
     *
     * @param sql
     * @return
     */
    public static BaseDao custom(String sql) {
        BaseDao baseDao = new BaseDao();
        baseDao.sb.append(sql);
        baseDao.object = null;
        return baseDao;
    }

    /**
     * =
     *
     * @param a a
     * @param b b
     * @return
     */
    public static String eq(String a, String b) {
        return " " + a + " = '" + b + "' ";
    }

    /**
     * =
     *
     * @param a
     * @param b
     * @return
     */
    public static String eq(String a, int b) {
        return " " + a + " = '" + b + "' ";
    }

    /**
     * 先构造SQL 而后将执行过程转接
     * 查
     *
     * @param clazz POJO
     * @return
     */
    public static <C> BaseDao<C> select(Class<C> clazz) {
        BaseDao<C> baseDao = new BaseDao<>();
        StringBuffer sb = baseDao.sb;
        sb.append("select * from ").append(clazz.getSimpleName().toLowerCase());
        try {
            //创建一个实例
            baseDao.object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    /**
     * 孤立计数器
     *
     * @param clazz
     * @param <C>
     * @return
     */
    public static <C> int executeCount(Class<C> clazz) {
        int num = 0;
        BaseDao<C> baseDao = new BaseDao<>();
        baseDao.sb.append("select count(*) from ").append(clazz.getSimpleName().toLowerCase());
        System.out.println("SQL：" + baseDao.sb.toString());

        try (
                Connection conn = baseDao.conn;
                PreparedStatement ps = conn.prepareStatement(baseDao.sb.toString())
        ) {

            ResultSet set = ps.executeQuery();
            while (set.next())
                num = set.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    /**
     * (1,2,3,4)
     *
     * @param values values
     * @return (1, 2, 3, 4)
     */
    public BaseDao<T> values(Object... values) {
        sb.append(" ( ");
        for (Object value : values) {
            sb.append(" '").append(value).append("',");
        }
        //删除多余的 ,
        delRear();
        sb.append(" ) ");
        return this;
    }

    /**
     * (1,2,3,4)
     *
     * @param values values
     * @return (1, 2, 3, 4)
     */
    public BaseDao<T> in(Object... values) {
        sb.append(" in ");
        return this.values(values);
    }

    /**
     * 排序
     *
     * @param condition 1 升序排序 -1 降序排序
     * @return
     */
    public BaseDao<T> orderBy(String col, int condition) {
        sb.append(" order by ").append(col).append(" ").append((condition == 1) ? "asc" : "desc").append(" ");
        return this;
    }

    /**
     * 计数器
     * 对当前已录入的SQL执行包装计数
     * 执行完后如果还有要求,需执行unCountWrap()
     *
     * @return 统计出的数目
     */
    public BaseDao<T> count() {
        sb.insert(0, "select count(*) as count from ( ");
        sb.append(" ) as x");
        return this;
    }

    /**
     * 执行count()后必须要执行的语句
     *
     * @return this
     */
    public BaseDao<T> unCountWrap() {
        sb.delete(0, 32);
        sb.delete(sb.length() - 7, sb.length());
        return this;
    }

    /**
     * 打印当前已录入的SQL
     *
     * @return this
     */
    public BaseDao<T> println() {
        System.out.println(sb.toString());
        return this;
    }

    /**
     * 多表联查
     *
     * @param table
     * @param condition
     * @return
     */
    public BaseDao<T> join(String table, String condition) {
        sb.append(" join ").append(table).append(" on ").append(condition);
        return this;
    }

    /**
     * 分组
     *
     * @param condition 分组条件
     * @return
     */
    public BaseDao<T> groupBy(String condition) {
        sb.append(" group by ").append(condition).append(" ");
        return this;
    }

    /**
     * 对已输入查询分页
     *
     * @param page     最小为一 -1为获取全部结果
     * @param pageSize 每页大小 -1为获取全部结果
     * @return this
     */
    public BaseDao<T> byPage(int page, int pageSize) {
        return this
                .limit((page - 1) * pageSize, pageSize);
    }

    /**
     * 获取总页数
     *
     * @param pageSize 每页的总数
     * @return 总页数
     */
    public int getTotalSize() {
        int count = this
                .count()
                .executeCount();
        //卸包计数器
        this.unCountWrap();
        return count;
    }

    /**
     * 获取总页数
     *
     * @param pageSize 每页的总数
     * @return 总页数
     */
    public int getTotalPageSize(int pageSize) {
        int count = this
                .count()
                .executeCount();
        //卸包计数器
        this.unCountWrap();
        //修复显示1页的bug
        if (count == 0)
            return 0;
        return (count + pageSize - 1) / pageSize;
    }

    /**
     * 删除尾部标点
     */
    public void delRear() {
        int len = sb.toString().length();
        //剔除末尾的 ,
        sb.delete(len - 1, len);
    }

    public StringBuffer getString() {
        return sb;
    }

    /**
     * 条件自定义追加
     *
     * @param sql sql语句
     * @return this
     */
    public BaseDao<T> append(String sql) {
        sb.append(" ").append(sql);
        return this;
    }

    /**
     * where子句
     *
     * @param sql sql语句
     * @return this
     */
    public BaseDao<T> where(String sql) {
        sb.append(" where ").append(sql);
        return this;
    }

    /**
     * where子句
     */
    public BaseDao<T> where() {
        sb.append(" where ");
        return this;
    }

    /**
     * limit子句
     * 可用于简单分页
     *
     * @param index  起点索引
     * @param offset 偏移量
     * @return this
     */
    public BaseDao<T> limit(int index, int offset) {
        sb.append(" limit ").append(index).append(",").append(offset);
        return this;
    }

    /**
     * and条件
     *
     * @param p1 条件一
     * @param p2 条件二
     * @return this
     */
    public BaseDao<T> and(String p1, String p2) {
        sb.append(" ").append(p1).append(" and ").append(p2);
        return this;
    }

    /**
     * and条件
     *
     * @return this
     */
    public BaseDao<T> and() {
        sb.append(" and ");
        return this;
    }

    /**
     * 模糊查询
     *
     * @param col     列名
     * @param content 内容
     * @return this
     */
    public BaseDao<T> like(String col, String content) {
        sb.append(" ").append(col).append(" like '%").append(content).append("%'");
        return this;
    }

    /**
     * 执行查询调用
     *
     * @return null 可能查询的表不存在
     */
    public List<T> executeQuery() {
        List<T> list = new ArrayList<>();
        //打印执行的SQL
        System.out.println("查询SQL： " + sb.toString());

        try (
                Connection conn = this.conn;
                PreparedStatement ps = conn.prepareStatement(sb.toString());
                ResultSet rs = ps.executeQuery()
        ) {
            Class<T> clazz = (Class<T>) object.getClass();
            while (rs.next()) {
                //数据类型映射获取 并放入返回结果
                list.add(resolveDataType(clazz, rs));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 执行查询调用
     *
     * @return null 可能查询的表不存在
     */
    public <C> List<C> executeCustomQuery(Class<C> clazz) {
        List<C> list = new ArrayList<>();
        //打印执行的SQL
        System.out.println("自定义查询SQL： " + sb.toString());

        try (
                Connection conn = this.conn;
                PreparedStatement ps = conn.prepareStatement(sb.toString());
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                /*//将对象属性与数据库获取数据对应
                for (Field field : clazz.getDeclaredFields()) {
                    //解除私有域的限制
                    field.setAccessible(true);


                    field.set(obj,rs.getObject(field.getName()));

                }*/

                //将对象加入返回结果
                list.add(resolveDataType(clazz, rs));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * 基本类型于数据库的映射解析
     *
     * @param cls
     * @param rs
     * @return
     * @throws NoSuchMethodException
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private <C> C resolveDataType(Class<C> cls, ResultSet rs) throws NoSuchMethodException, SQLException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //创建一个实例
        C obj = cls.newInstance();
        //获取结果集元数据(获取此 ResultSet 对象的列的编号、类型和属性。)
        ResultSetMetaData rd = rs.getMetaData();
        for (int i = 0; i < rd.getColumnCount(); i++) {
            //获取列名
            String columnName = rd.getColumnLabel(i + 1);
            //组合方法名
//            String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            String methodName = StringUtil.UnderlineToHump("set_" + columnName);

            //获取列类型
            int columnType = rd.getColumnType(i + 1);
            Method method;
            //不为空才进行解析,防止包装类将null解析为基本类型初始值
            if (rs.getObject(columnName) != null)
                switch (columnType) {
                    case Types.VARCHAR:
                    case Types.CHAR:
                        method = cls.getMethod(methodName, String.class);
                        if (method != null) {
                            method.invoke(obj, rs.getString(columnName));
                        }
                        break;

                    case Types.INTEGER:
                    case Types.SMALLINT:
                        method = cls.getMethod(methodName, Integer.class);
                        if (method != null) {
                            method.invoke(obj, rs.getInt(columnName));
                        }
                        break;
                    case Types.BIGINT:
                        method = cls.getMethod(methodName, Long.class);
                        if (method != null) {
                            method.invoke(obj, rs.getLong(columnName));
                        }
                        break;
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        method = cls.getMethod(methodName, Timestamp.class);
                        if (method != null) {

                            method.invoke(obj, rs.getTimestamp(columnName));
                        }
                        break;
                    case Types.DECIMAL:
                        method = cls.getMethod(methodName, BigDecimal.class);
                        if (method != null) {
                            method.invoke(obj, rs.getBigDecimal(columnName));
                        }
                        break;
                    case Types.REAL: //单精度浮点
                    case Types.DOUBLE:
                    case Types.NUMERIC:
                        method = cls.getMethod(methodName, Double.class);
                        if (method != null) {

                            method.invoke(obj, rs.getDouble(columnName));
                        }
                        break;
                    case Types.BIT:
                        method = cls.getMethod(methodName, Boolean.class);
                        if (method != null) {
                            method.invoke(obj, rs.getBoolean(columnName));
                        }
                        break;
                    default:
                        break;
                }
        }

        return obj;
    }

    /**
     * 执行 del() update() insert()调用
     *
     * @return true 执行成功 false 执行失败
     */
    public boolean executeUpdate() {
        boolean flag = false;
        System.out.println("SQL：" + sb.toString());

        try (
                Connection conn = this.conn;
                PreparedStatement ps = conn.prepareStatement(sb.toString())
        ) {

            //如果执行成功则返回
            if (ps.executeUpdate() > 0)
                flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 执行 count 调用
     * 调用完该函数后需 手动关闭连接!!~!!!
     *
     * @return 查询数
     */
    public int executeCount() {
        int num = 0;
        System.out.println("计数SQL：" + sb.toString());
        Connection conn = this.conn;
        try (
                PreparedStatement ps = conn.prepareStatement(sb.toString())
        ) {

            ResultSet set = ps.executeQuery();
            while (set.next())
                num = set.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    /**
     * 关闭数据库连接
     */
    public void closeConn() {
        try {
            this.conn.close();
            System.out.println("连接" + conn + "已关闭");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
