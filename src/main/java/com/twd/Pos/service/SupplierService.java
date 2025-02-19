package com.twd.Pos.service;

import java.util.List;

import com.twd.Pos.entity.Supplier;

public interface SupplierService {
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier supplier);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    void deleteSupplier(Long id);
}