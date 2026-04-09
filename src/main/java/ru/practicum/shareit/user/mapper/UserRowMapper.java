//package ru.practicum.shareit.user.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.user.model.User;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class UserRowMapper implements RowMapper<User> {
//
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//        User user = new User();
//
//        user.setUsername(rs.getString("username"));
//
//        // personalItemIds - в репозитории
//
//        return user;
//    }
//}
