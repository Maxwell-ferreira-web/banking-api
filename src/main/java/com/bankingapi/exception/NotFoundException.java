package com.bankingapi.exception;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private String resourceType;
    private Object resourceId;
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s não encontrado com ID: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public Object getResourceId() {
        return resourceId;
    }
}
