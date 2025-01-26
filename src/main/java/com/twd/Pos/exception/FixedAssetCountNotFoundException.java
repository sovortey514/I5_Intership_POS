package com.twd.Pos.exception;

public class FixedAssetCountNotFoundException extends RuntimeException {
    public FixedAssetCountNotFoundException(Long id) {
        super("Fixed Asset Count not found with id: " + id);
    }
}