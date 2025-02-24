package com.twd.Pos.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.entity.Tables;
import com.twd.Pos.service.TableService;

@RestController
@RequestMapping("/admin")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping("/getalltables")
    public List<Tables> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/gettablebyId/{id}")
    public Optional<Tables> getTableById(@PathVariable Long id) {
        return tableService.getTablesById(id);
    }

    @PostMapping("/createtable")
    public Tables createTable(@RequestBody Tables table) {
        return tableService.addTables(table);
    }

    @PutMapping("/upadtetable/{id}")
    public Tables updateTable(@PathVariable Long id, @RequestBody Tables table) {
        return tableService.updateTables(id, table);
    }

    
@DeleteMapping("/deletetable/{id}")
public ResponseEntity<String> deleteTable(@PathVariable Long id) {
    tableService.deleteTables(id);
    return ResponseEntity.ok("Table with ID " + id + " has been deleted successfully.");
}

@PostMapping("/merge")
public ResponseEntity<Tables> mergeTables(@RequestBody Map<String, Long> request) {
    Long id1 = request.get("id1");
    Long id2 = request.get("id2");

    Tables mergedTable = tableService.mergeTables(id1, id2);
    
    return ResponseEntity.ok(mergedTable);
}


    
@PostMapping("/split/{id}")
public ResponseEntity<String> splitTable(@PathVariable Long id) {
    tableService.splitTables(id);
    return ResponseEntity.ok("Table " + id + " has been successfully split.");
}
    
}
