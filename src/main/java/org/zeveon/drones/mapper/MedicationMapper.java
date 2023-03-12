package org.zeveon.drones.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zeveon.drones.config.GlobalMapperConfig;
import org.zeveon.drones.dto.MedicationDto;
import org.zeveon.drones.entity.Medication;
import org.zeveon.drones.service.ImageService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Mapper(config = GlobalMapperConfig.class)
public interface MedicationMapper {

    MedicationMapper INSTANCE = Mappers.getMapper(MedicationMapper.class);

    @Mapping(target = "droneId", source = "drone.id")
    MedicationDto toDto(Medication medication);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", expression = "java(imageService.save(medicationDto.getImage()))")
    @Mapping(target = "imageContentType", expression = "java(imageService.getContentType(medicationDto.getImage()))")
    Medication toEntity(MedicationDto medicationDto, @Context ImageService imageService) throws IOException;

    List<MedicationDto> toDtoList(Collection<Medication> medications);

    List<Medication> toEntityList(Collection<MedicationDto> medicationDtos, @Context ImageService imageService) throws IOException;
}
