package ru.liga.packagetruckspring.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.liga.packagetruckspring.dto.StructureDto;


import java.sql.ResultSet;
import java.sql.SQLException;

public class StructureRowMapper implements RowMapper<StructureDto> {

    @Override
    public StructureDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        StructureDto structurePack = new StructureDto();
        structurePack.setId(rs.getLong("id"));
        structurePack.setName(rs.getString("name"));
        structurePack.setForm(rs.getString("form"));
        structurePack.setSymbol(rs.getString("symbol"));
        return structurePack;
    }
}
