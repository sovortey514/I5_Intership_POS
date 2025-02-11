package com.twd.Pos.service.impl;

import com.twd.Pos.entity.Size;
import com.twd.Pos.repository.SizeRepository;
import com.twd.Pos.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public Optional<Size> getSizeById(Long id) {
        return sizeRepository.findById(id);
    }

    @Override
    public Size createSize(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public Size updateSize(Long id, Size sizeDetails) {
        return sizeRepository.findById(id)
                .map(existingSize -> {
                    existingSize.setName(sizeDetails.getName());
                    existingSize.setDescription(sizeDetails.getDescription());
                    return sizeRepository.save(existingSize);
                }).orElseThrow(() -> new RuntimeException("Size not found with id: " + id));
    }

    @Override
    public void deleteSize(Long id) {
        if (sizeRepository.existsById(id)) {
            sizeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Size not found with id: " + id);
        }
    }
}
