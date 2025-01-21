package com.twd.SpringSecurityJWT.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.CategoryRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetService;

@Service
public class FixedAssetServiceImpl implements FixedAssetService {

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public FixedAsset createFixedAsset(FixedAsset fixedAsset, Integer categoryId) {

        validateFixedAsset(fixedAsset);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            fixedAsset.setCategory(categoryOpt.get());
            return fixedAssetRepository.save(fixedAsset);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    private void validateFixedAsset(FixedAsset fixedAsset) {
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
        Optional<FixedAsset> asset = fixedAssetRepository.findById(id);

        if (!asset.isPresent()) {
            throw new RuntimeException("Fixed Asset not found with id: " + id); // Throwing exception if not found
        }

        fixedAssetRepository.deleteById(id);
    }

    @Override
    public List<FixedAsset> getAllFixedAssets() {
        return fixedAssetRepository.findAllWithCategory();
    }

    @Override
    public Optional<FixedAsset> getFixedAssetById(Long id) {

        return fixedAssetRepository.findById(id);
    }

    @Override
    public FixedAsset updateFixedAsset(Long id, FixedAsset fixedAsset) {

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