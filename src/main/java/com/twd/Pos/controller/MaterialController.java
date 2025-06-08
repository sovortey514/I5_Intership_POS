package com.twd.Pos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.dto.FixedAssetRequest;
import com.twd.Pos.dto.ReqRes;
import com.twd.Pos.entity.Category;
import com.twd.Pos.entity.Material;
import com.twd.Pos.repository.CategoryRepository;
import com.twd.Pos.repository.FixedAssetRepository;
import com.twd.Pos.service.MaterialService;

@RestController
@RequestMapping("/auth")
public class MaterialController {
    
    @Autowired
    private MaterialService fixedAssetService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FixedAssetRepository fixedAssetRepository;
    
    @PostMapping("/createMaterial")
    public ResponseEntity<ReqRes> createFixedAsset(@RequestBody FixedAssetRequest fixedAssetRequest) {
        ReqRes resp = new ReqRes();
        try {
            Material fixedAssetToSave = new Material();
            fixedAssetToSave.setName(fixedAssetRequest.getName());
            fixedAssetToSave.setPrice(fixedAssetRequest.getPrice());
            fixedAssetToSave.setPurchaseDate(fixedAssetRequest.getPurchaseDate());
            fixedAssetToSave.setQuantity(fixedAssetRequest.getQuantity());
            fixedAssetToSave.setRemarks(fixedAssetRequest.getRemark());
            fixedAssetToSave.setImage(fixedAssetRequest.getImage());
            Integer categoryId = fixedAssetRequest.getCategoryId();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                fixedAssetToSave.setCategory(categoryOpt.get());
            } else {
                throw new RuntimeException("Category not found");
            }
            Material savedFixedAsset = fixedAssetService.createFixedAsset(fixedAssetToSave, categoryId);
            if (savedFixedAsset != null && savedFixedAsset.getId() != null) {
                resp.setFixedAsset(savedFixedAsset);
                resp.setMessage("Fixed Asset Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (RuntimeException e) {
            resp.setStatusCode(400); 
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500); 
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
}


    @DeleteMapping("/deleteFixedAsset/{id}")
    public ResponseEntity<ReqRes> deleteFixedAsset(@PathVariable Long id){
        ReqRes resp = new ReqRes();
        try {
            fixedAssetService.deleteFixedAsset(id);
            resp.setMessage("Fixed Asset Deleted Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getAllFixedAssets")
    public ResponseEntity<ReqRes> getAllFixedAssets() {
    ReqRes resp = new ReqRes();
    try {
        List<Material> fixedAssets = fixedAssetService.getAllFixedAssets();
        resp.setFixedAssets(fixedAssets); 
        resp.setMessage("Fixed Assets Retrieved Successfully");
        resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }  

    @GetMapping("/getFixedAssetById/{id}")
    public ResponseEntity<ReqRes> getFixedAssetById(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Material> fixedAsset = fixedAssetService.getFixedAssetById(id);
            if (fixedAsset.isPresent()) {
                resp.setFixedAsset(fixedAsset.get());
                resp.setMessage("Fixed Asset Retrieved Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Fixed Asset Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReqRes> updateFixedAsset(@PathVariable Long id, @RequestBody FixedAssetRequest fixedAssetRequest) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Material> existingFixedAssetOpt = fixedAssetRepository.findById(id);
            if (!existingFixedAssetOpt.isPresent()) {
                resp.setStatusCode(404);
                resp.setMessage("Fixed Asset not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }
            Material existingFixedAsset = existingFixedAssetOpt.get();

            // Update the fields that are present in the request
            if (fixedAssetRequest.getName() != null) {
                existingFixedAsset.setName(fixedAssetRequest.getName());
            }
            if (fixedAssetRequest.getPrice() != null) {
                existingFixedAsset.setPrice(fixedAssetRequest.getPrice());
            }
            if (fixedAssetRequest.getPurchaseDate() != null) {
                existingFixedAsset.setPurchaseDate(fixedAssetRequest.getPurchaseDate());
            }
            if (fixedAssetRequest.getQuantity() != null) {
                existingFixedAsset.setQuantity(fixedAssetRequest.getQuantity());
            }
            if (fixedAssetRequest.getImage() != null) {
                existingFixedAsset.setImage(fixedAssetRequest.getImage());
            }
            Integer categoryId = fixedAssetRequest.getCategoryId();
            if (categoryId != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
                if (categoryOpt.isPresent()) {
                    existingFixedAsset.setCategory(categoryOpt.get());
                } else {
                    throw new RuntimeException("Category not found");
                }
            }

            Material updatedFixedAsset = fixedAssetRepository.save(existingFixedAsset);

            // Prepare response
            resp.setFixedAsset(updatedFixedAsset);
            resp.setMessage("Fixed Asset Updated Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}