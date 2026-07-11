package com.microbase.profileservice.mapper;

import com.microbase.profileservice.dto.request.ProfileRequest;
import com.microbase.profileservice.dto.request.ProfileUpdateRequest;
import com.microbase.profileservice.dto.response.ProfileResponse;
import com.microbase.profileservice.model.Profile;
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
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Profile toEntity(ProfileRequest request);

    @Mapping(target = "formattedCreatedAt", source = "createdAt", qualifiedByName = "formatDateTime")
    @Mapping(target = "formattedUpdatedAt", source = "updatedAt", qualifiedByName = "formatDateTime")
    @Mapping(target = "preferredLocation", source = "location")
    @Mapping(target = "totalFriend", constant = "0")
    ProfileResponse toResponse(Profile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfileFromRequest(ProfileUpdateRequest request, @MappingTarget Profile target);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
