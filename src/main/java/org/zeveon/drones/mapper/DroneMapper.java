package org.zeveon.drones.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zeveon.drones.config.GlobalMapperConfig;
import org.zeveon.drones.dto.DroneDto;
import org.zeveon.drones.entity.Drone;

/**
 * @author Stanislav Vafin
 */
@Mapper(config = GlobalMapperConfig.class)
public interface DroneMapper {

    DroneMapper INSTANCE = Mappers.getMapper(DroneMapper.class);

    DroneDto toDto(Drone message);

    @Mapping(target = "id", ignore = true)
    Drone toEntity(DroneDto messageDto);
}
