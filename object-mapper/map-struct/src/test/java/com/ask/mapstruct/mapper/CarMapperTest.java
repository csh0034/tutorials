package com.ask.mapstruct.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.mapstruct.model.Car;
import com.ask.mapstruct.model.CarDto;
import org.junit.jupiter.api.Test;

class CarMapperTest {

  @Test
  void toEntity() {
    // given
    CarDto carDto = new CarDto();
    carDto.setCarName("carName...");

    // when
    Car car = CarMapper.INSTANCE.toEntity(carDto);

    // then
    assertThat(car.getName()).isEqualTo(car.getName());
  }

  @Test
  void toDto() {
    // given
    Car car = Car.builder()
        .name("carName...")
        .build();

    // when
    CarDto carDto = CarMapper.INSTANCE.toDto(car);

    // then
    assertThat(carDto.getCarName()).isEqualTo(car.getName());
  }

}
