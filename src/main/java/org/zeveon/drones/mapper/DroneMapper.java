package org.zeveon.drones.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zeveon.drones.config.GlobalMapperConfig;
import org.zeveon.drones.dto.DroneDto;
import org.zeveon.drones.entity.Drone;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Mapper(config = GlobalMapperConfig.class)
public interface DroneMapper {

    DroneMapper INSTANCE = Mappers.getMapper(DroneMapper.class);

    DroneDto toDto(Drone drone);

    @Mapping(target = "id", ignore = true)
    Drone toEntity(DroneDto droneDto);

    List<DroneDto> toDtoList(Collection<Drone> drones);

    List<Drone> toEntityList(Collection<DroneDto> droneDtos);
}
