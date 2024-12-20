package com.twd.SpringSecurityJWT_Pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.SpringSecurityJWT_Pos.entity.FileData;
import com.twd.SpringSecurityJWT_Pos.entity.User;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long>{

	Optional<FileData> findByName(String fileName);
    List<FileData> findByUser(User user);

}
