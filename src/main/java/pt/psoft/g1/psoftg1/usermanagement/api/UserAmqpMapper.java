package pt.psoft.g1.psoftg1.usermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.usermanagement.dto.UserDTOAmqp;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserAmqpMapper extends MapperInterface {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "fullName")
    @Mapping(target = "role", source = "role")

    public abstract CreateUserRequest toCreateUserRequest(UserDTOAmqp userDTOAmqp);
    @Mapping(target = "id", expression = "java(user.getId() != null ? user.getId().toString() : null)")
    public abstract UserDTOAmqp toUserDTOAMQP(User user);

    public abstract List<UserDTOAmqp> toUserDTOAMQP(List<User> userList);
}
