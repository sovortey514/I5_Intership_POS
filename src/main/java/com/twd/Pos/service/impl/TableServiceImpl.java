package com.twd.Pos.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.Pos.entity.Tables;
import com.twd.Pos.repository.TableRepository;
import com.twd.Pos.service.TableService;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    @Override
    public Tables addTables(Tables tables) {
        return tableRepository.save(tables);
    }

    @Override
    public Tables updateTables(Long id, Tables updatedTable) {
        return tableRepository.findById(id).map(table -> {
            table.setStatus(updatedTable.getStatus());
            table.setType(updatedTable.getType());
            table.setLocation(updatedTable.getLocation());
            return tableRepository.save(table);
        }).orElseThrow(() -> new RuntimeException("Table not found"));
    }

    @Override
    public void deleteTables(Long id) {
        tableRepository.deleteById(id);
    }

    @Override
    public List<Tables> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public Optional<Tables> getTablesById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public Tables mergeTables(Long id1, Long id2) {
        // Retrieve tables from the repository
        Tables table1 = tableRepository.findById(id1).orElseThrow(() -> new RuntimeException("Table 1 not found"));
        Tables table2 = tableRepository.findById(id2).orElseThrow(() -> new RuntimeException("Table 2 not found"));

        // Ensure both tables are not already merged
        if (table1.getMergedTables() != null && !table1.getMergedTables().isEmpty()) {
            throw new RuntimeException("Table 1 is already merged.");
        }
        if (table2.getMergedTables() != null && !table2.getMergedTables().isEmpty()) {
            throw new RuntimeException("Table 2 is already merged.");
        }

        // Ensure both tables are in the same location
        if (!table1.getLocation().equals(table2.getLocation())) {
            throw new RuntimeException("Tables must be in the same location to merge.");
        }

        // Merge table properties
        String mergedName = table1.getName() + "-" + table2.getName();
        String mergedStatus = (table1.getStatus().equals("available") && table2.getStatus().equals("available"))
                ? "available"
                : "occupied";

        // Update table1 with merged details
        table1.setName(mergedName);
        table1.setStatus(mergedStatus);
        table1.setMergedTables(new ArrayList<>(Arrays.asList(id1, id2)));

        // Delete table2 from the database
        tableRepository.deleteById(id2);

        // Save and return the updated table1
        return tableRepository.save(table1);
    }

    @Override
    public Tables splitTables(Long id) {
        Tables mergedTable = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (mergedTable.getMergedTables() == null || mergedTable.getMergedTables().isEmpty()) {
            throw new RuntimeException("This table is not merged.");
        }

        // Restore original tables
        for (Long tableId : mergedTable.getMergedTables()) {
            Tables originalTable = new Tables("T-" + tableId, "available", "dine-in", mergedTable.getLocation());
            tableRepository.save(originalTable);
        }

        // Delete the merged table
        tableRepository.deleteById(id);

        return mergedTable;
    }

}
