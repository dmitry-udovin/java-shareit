//package ru.practicum.shareit.request.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.request.model.ItemRequest;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class RequestRowMapper implements RowMapper<ItemRequest> {
//
//    @Override
//    public ItemRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
//        ItemRequest request = new ItemRequest();
//        request.setDescription(rs.getString("description"));
//        request.setCreated(rs.getTimestamp("created").toLocalDateTime());
//
//        request.setRequestorId(rs.getLong("requestor_id"));
//
//        // !!! items пустой — заполняем в репозитории
//
//        return request;
//    }
//}
