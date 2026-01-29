package com.spindox.authservice.mapper;

import com.spindox.authservice.dto.UserDto;
import com.spindox.authservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    public abstract UserDto ToDto(User user);
    public abstract User FromDto(UserDto userDto);

}
