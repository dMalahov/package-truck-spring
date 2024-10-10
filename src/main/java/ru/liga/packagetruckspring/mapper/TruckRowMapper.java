package ru.liga.packagetruckspring.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.liga.packagetruckspring.dto.TruckDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TruckRowMapper implements RowMapper<TruckDto> {

    @Override
    public TruckDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        TruckDto truckDto = new TruckDto();
        truckDto.setId(rs.getLong("id"));
        truckDto.setHeight(rs.getInt("height"));
        truckDto.setWidth(rs.getInt("width"));
        truckDto.setPackages(new ArrayList<>());
        return truckDto;
    }
}
