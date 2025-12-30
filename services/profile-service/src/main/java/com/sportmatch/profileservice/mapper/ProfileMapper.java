package com.sportmatch.profileservice.mapper;

import com.sportmatch.profileservice.dto.request.ProfileRequest;
import com.sportmatch.profileservice.dto.response.ProfileResponse;
import com.sportmatch.profileservice.entity.Profile;
import org.mapstruct.*;

import java.time.LocalDateTime;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {CustomMapperUtil.class}
)
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Profile toEntity(ProfileRequest request);

    @Mapping(target = "formattedCreatedAt", source = "createdAt", qualifiedByName = "formatDateTime")
    @Mapping(target = "formattedUpdatedAt", source = "updatedAt", qualifiedByName = "formatDateTime")
    ProfileResponse toResponse(Profile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfileFromEntity(Profile source, @MappingTarget Profile target);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}