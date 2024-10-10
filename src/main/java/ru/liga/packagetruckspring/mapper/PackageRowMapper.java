package ru.liga.packagetruckspring.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.liga.packagetruckspring.dto.PackageDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageRowMapper implements RowMapper<PackageDto> {

    private final StringArrayConverter stringArrayConverter = new StringArrayConverter();

    @Override
    public PackageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        PackageDto packageDto = new PackageDto();
        packageDto.setId(rs.getLong("id"));
        packageDto.setHeight(rs.getInt("height"));
        packageDto.setWidthTop(rs.getInt("widthTop"));
        packageDto.setWidthBottom(rs.getInt("widthBottom"));
        packageDto.setPack(stringArrayConverter.convertToEntityAttribute(rs.getString("pack")));
        return packageDto;
    }
}
