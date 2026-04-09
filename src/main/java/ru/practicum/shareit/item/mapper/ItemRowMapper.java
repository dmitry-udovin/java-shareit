//package ru.practicum.shareit.item.mapper;
//
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.model.RentStatus;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class ItemRowMapper implements RowMapper<Item> {
//
//    @Override
//    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Item item = new Item();
//
//        item.setName(rs.getString("item_name"));
//        item.setDescription(rs.getString("item_desc"));
//        item.setOwnerId(rs.getLong("owner_id"));
//        item.setCountRents(rs.getInt("count_rents"));
//
//        item.setStatus(RentStatus.valueOf(rs.getString("rent_status")));
//
//        return item;
//    }
//}
