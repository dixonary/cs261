package team16.cs261.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Bridges the gap between entities modelled as java objects and entities in the database.
 * Generates basic queries, e.g. selectAll, selectAll by id.
 * Assumptions:
 * - table name is entity class name
 * - first field declared in entity class is the id field
 */
public abstract class AbstractDao<ID, E> {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    private final Class<E> entityClass;
    private final String tableName;

    private String selectAll, selectAllLimit, selectCount, selectById, deleteFromIdIn;

    protected AbstractDao(Class<E> entityClass) {
        this.entityClass = entityClass;
        tableName = entityClass.getSimpleName();

        generateQueries();
    }

    public void generateQueries() {
        String idColumn = entityClass.getDeclaredFields()[0].getName();

        selectAll = "SELECT * FROM " + tableName;
        selectAllLimit = "SELECT * FROM " + tableName + " LIMIT ?, ?";
        selectCount = "SELECT COUNT(*) FROM " + tableName;
        selectById = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

        deleteFromIdIn = "DELETE FROM " + tableName + " WHERE " + idColumn + " IN ";
    }


    public void delete(List<Integer> ids) {
        if(ids.size()==0) return;


        jdbcTemplate.update(deleteFromIdIn + toList(ids));
    }

    public List<E> selectAll() {
        return jdbcTemplate.query(
                selectAll,
                new Object[0],
                new BeanPropertyRowMapper<>(entityClass)
        );
    }

    public Integer selectCountAll() {
        return jdbcTemplate.queryForObject(selectCount, Integer.class);
    }

    public List<E> selectAllLimit(int count) {
        return selectAllLimit(0, count);
    }

    public List<E> selectAllLimit(int offset, int length) {
        return jdbcTemplate.query(
                selectAllLimit,
                new Object[]{offset, length},
                new BeanPropertyRowMapper<>(entityClass)
        );
    }


    public E selectWhereId(ID id) {
        return jdbcTemplate.queryForObject(
                selectById,
                new Object[]{id},
                new BeanPropertyRowMapper<>(entityClass)
        );
    }

    public E selectWhere(Map<String, Object> conditions) {
        String sql = formatSelectWhere(tableName, conditions.keySet());

        Object[] values = conditions.values().toArray(new Object[conditions.values().size()]);

        return jdbcTemplate.queryForObject(
                sql, values,
                new BeanPropertyRowMapper<>(entityClass)
        );
    }

    public E selectWhereLimit(Map<String, Object> conditions, int offset, int length) {
        String sql = formatSelectWhereLimit(tableName, conditions.keySet(), offset, length);

        Object[] values = conditions.values().toArray(new Object[conditions.values().size()]);

        return jdbcTemplate.queryForObject(
                sql, values,
                new BeanPropertyRowMapper<>(entityClass)
        );
    }


    private static String formatSelect(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    private static String formatSelectWhere(String tableName, Iterable<String> fields) {
        StringBuilder query = new StringBuilder(formatSelect(tableName));

        query.append(" WHERE ");

        for (Iterator<String> iterator = fields.iterator(); iterator.hasNext(); ) {
            String f = iterator.next();
            query.append(f).append(" = ?");
            if (iterator.hasNext()) {
                query.append(" AND ");
            }
        }

        return query.toString();
    }

    private static String formatSelectWhereLimit(String tableName, Iterable<String> fields, int offset, int length) {
        return formatSelectWhere(tableName, fields) + " LIMIT " + offset + ", " + length;
    }

    public static String toList(List<Integer> integers) {
        StringBuilder builder = new StringBuilder("(");

        Iterator<Integer> ints = integers.iterator();
        while (ints.hasNext()) {
            builder.append(ints.next());
            if (ints.hasNext()) {
                builder.append(",");
            }
        }

        builder.append(")");

        return builder.toString();
    }

}
