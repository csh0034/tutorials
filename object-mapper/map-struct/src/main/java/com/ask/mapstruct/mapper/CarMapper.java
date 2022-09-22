package com.ask.mapstruct.mapper;

import com.ask.mapstruct.model.Car;
import com.ask.mapstruct.model.CarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {

  CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

  @Mapping(target = "id", source = "carId", ignore = true)
  @Mapping(target = "name", source = "carName")
  Car toEntity(CarDto dto);

  @Mapping(target = "carId", source = "id")
  @Mapping(target = "carName", source = "name")
  CarDto toDto(Car car);

}
