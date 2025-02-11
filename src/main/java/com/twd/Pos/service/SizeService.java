package com.twd.Pos.service;

import java.util.List;
import java.util.Optional;

import com.twd.Pos.entity.Size;

public interface SizeService {
    

    List<Size> getAllSizes();

    Optional<Size> getSizeById(Long id);

    Size createSize(Size size);

    Size updateSize(Long id, Size sizeDetails);

    void deleteSize(Long id);
}
