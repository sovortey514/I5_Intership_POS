package com.twd.SpringSecurityJWT_Pos.dto.respones;

import com.twd.SpringSecurityJWT_Pos.dto.resquest.FileDataDTO;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String role;

    private List<FileDataDTO> files;
}
