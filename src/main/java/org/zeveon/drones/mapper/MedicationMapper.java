package org.zeveon.drones.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zeveon.drones.config.GlobalMapperConfig;
import org.zeveon.drones.dto.MedicationDto;
import org.zeveon.drones.entity.Medication;

import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Mapper(config = GlobalMapperConfig.class)
public interface MedicationMapper {

    MedicationMapper INSTANCE = Mappers.getMapper(MedicationMapper.class);

    MedicationDto toDto(Medication message);

    @Mapping(target = "id", ignore = true)
    Medication toEntity(MedicationDto messageDto);

    List<MedicationDto> toDtoList(Collection<Medication> medications);

    List<Medication> toEntityList(Collection<MedicationDto> medicationDtos);
}
