package com.sf.event.queue;

import com.hazelcast.core.QueueStore;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBStoreConfig implements QueueStore<String> {

    private final JdbcTemplate jdbcTemplate;
    private final String queueName;

    public DBStoreConfig(JdbcTemplate jdbcTemplate, String queueName) {
        this.jdbcTemplate = jdbcTemplate;
        this.queueName = queueName;
    }

    @Override
    public void store(Long key, String value) {
        SQL sql = new SQL().INSERT_INTO(queueName)
                .VALUES("id", String.valueOf(key))
                .VALUES("message", "'" + value + "'")
                .VALUES("status", "'PROCESSING'")
                .VALUES("createDateTime", "now()")
                .VALUES("updateDateTime", "now()");
        jdbcTemplate.update(sql.toString());
    }

    @Override
    public void storeAll(Map<Long, String> map) {
    }

    @Override
    public void delete(Long key) {
        SQL sql = new SQL().UPDATE(queueName)
                .SET("status = 'PROCESSED'")
                .SET("updateDateTime = now()")
                .WHERE("id = " + key);
        jdbcTemplate.update(sql.toString());
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
    }

    @Override
    public String load(Long key) {
        SQL sql = new SQL().SELECT("message").FROM(queueName).WHERE("id = " + key);
        return jdbcTemplate.queryForObject(sql.toString(), String.class);
    }

    @Override
    public Map<Long, String> loadAll(Collection<Long> keys) {
        return null;
    }

    @Override
    public Set<Long> loadAllKeys() {
        SQL sql = new SQL().SELECT("id").FROM(queueName).WHERE("status = 'PROCESSING'");
        return new HashSet<Long>(jdbcTemplate.queryForList(sql.toString(), Long.class));
    }
}
