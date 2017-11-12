package com.depromeet.todo.dto.page;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserForm implements Serializable {

    @Pattern(regexp = "^[A-Za-z0-9_]{5,45}")
    private String username;

    @NotEmpty
    @Size(min = 10, max = 50)
    private String password;
}
