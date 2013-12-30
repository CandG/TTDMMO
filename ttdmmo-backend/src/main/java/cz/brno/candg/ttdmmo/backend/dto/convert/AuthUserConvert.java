package cz.brno.candg.ttdmmo.backend.dto.convert;

import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.dto.AuthUserDto;

/**
 * Conversion between AuthUser DTO and entity back and forth.
 *
 * @author Zdenek Lastuvka
 */
public class AuthUserConvert {

    public static AuthUser fromDtoToEntity(AuthUserDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AuthUserConvert: fromDtoToEntity: null parameter!");
        }
        AuthUser authUser = new AuthUser();
        authUser.setUser_id(dto.getUser_id());
        authUser.setMoney(dto.getMoney());
        authUser.setName(dto.getName());
        return authUser;
    }

    public static AuthUserDto fromEntityToDto(AuthUser entity) {
        if (entity == null) {
            throw new IllegalArgumentException("AuthUserConvert: fromEntityToDto: null parameter!");
        }
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUser_id(entity.getUser_id());
        authUserDto.setMoney(entity.getMoney());
        authUserDto.setName(entity.getName());

        return authUserDto;
    }
}
