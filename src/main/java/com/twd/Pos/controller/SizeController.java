package com.twd.Pos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.entity.Size;
import com.twd.Pos.service.SizeService;

@RestController
@RequestMapping("/admin")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @PostMapping("/createSize")
    public ResponseEntity<Size> createSize(@RequestBody Size size) {
        return ResponseEntity.ok(sizeService.createSize(size));
    }

    @GetMapping("/getAllSize")
    public ResponseEntity<List<Size>> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @GetMapping("/getFoodById/{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable Long id) {
        Optional<Size> size = sizeService.getSizeById(id);
        return size.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update-size/{id}")
    public ResponseEntity<Size> updateSize(@PathVariable Long id, @RequestBody Size sizeDetails) {
        return ResponseEntity.ok(sizeService.updateSize(id, sizeDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable Long id) {
        sizeService.deleteSize(id);
        return ResponseEntity.ok("Size deleted successfully.");
    }

}
