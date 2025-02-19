package com.twd.Pos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.Pos.entity.Supplier;
import com.twd.Pos.repository.SupplierRepository;
import com.twd.Pos.service.SupplierService;

@Service
public class SupplierServiceImpl implements SupplierService{

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplier) {
        return supplierRepository.findById(id).map(existingSupplier -> {
            existingSupplier.setContactName(supplier.getContactName());
            existingSupplier.setPhone(supplier.getPhone());
            existingSupplier.setEmail(supplier.getEmail());
            existingSupplier.setAddress(supplier.getAddress());
            existingSupplier.setCountry(supplier.getCountry());
            return supplierRepository.save(existingSupplier);
        }).orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
    }
    
}
