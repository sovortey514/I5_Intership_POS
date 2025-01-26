package com.twd.Pos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.Pos.entity.Category;
import com.twd.Pos.entity.Material;
import com.twd.Pos.repository.CategoryRepository;
import com.twd.Pos.repository.FixedAssetRepository;
import com.twd.Pos.service.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Material createFixedAsset(Material fixedAsset, Integer categoryId) {

        validateFixedAsset(fixedAsset);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            fixedAsset.setCategory(categoryOpt.get());
            return fixedAssetRepository.save(fixedAsset);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    private void validateFixedAsset(Material fixedAsset) {
        if (fixedAsset.getName() == null || fixedAsset.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        if (fixedAsset.getPrice() == null) {
            throw new RuntimeException("Price is required");
        }

        if (fixedAsset.getPurchaseDate() == null) {
            throw new RuntimeException("Purchase date is required");
        }
        if (fixedAsset.getQuantity() == null) {
            throw new RuntimeException("Quantity is required");
        }
    }

    //

    @Override
    public void deleteFixedAsset(Long id) {
        Optional<Material> asset = fixedAssetRepository.findById(id);

        if (!asset.isPresent()) {
            throw new RuntimeException("Fixed Asset not found with id: " + id); // Throwing exception if not found
        }

        fixedAssetRepository.deleteById(id);
    }

    @Override
    public List<Material> getAllFixedAssets() {
        return fixedAssetRepository.findAllWithCategory();
    }

    @Override
    public Optional<Material> getFixedAssetById(Long id) {

        return fixedAssetRepository.findById(id);
    }

    @Override
    public Material updateFixedAsset(Long id, Material fixedAsset) {

        return fixedAssetRepository.findById(id).map(existingAsset -> {
            existingAsset.setName(fixedAsset.getName());
            existingAsset.setPrice(fixedAsset.getPrice());
            existingAsset.setPurchaseDate(fixedAsset.getPurchaseDate());

            existingAsset.setQuantity(fixedAsset.getQuantity());
            existingAsset.setImage(fixedAsset.getImage());
            existingAsset.setCategory(fixedAsset.getCategory());
            return fixedAssetRepository.save(existingAsset);
        }).orElseThrow(() -> new RuntimeException("Fixed Asset not found"));
    }


}