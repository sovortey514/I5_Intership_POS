package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.Tables;
public interface TableService {
    List<Tables> getAllTables();
    Optional<Tables> getTablesById(Long id);
    Tables addTables(Tables tables);
    Tables updateTables(Long id, Tables tables);
    void deleteTables(Long id);
    Tables mergeTables(Long id1, Long id2);
    Tables splitTables(Long id);
}
