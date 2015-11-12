package com.sf;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Created by adityasofat on 09/11/2015.
 */
public class QueueHelper {

    public static DataSource dataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/hazel");
        dataSource.setUser("hazeluser");
        dataSource.setPassword("password");
        return dataSource;
    }
}
