package com.github.immortaleeb.hearts.write.domain;

class ValidationResult {

    private final String error;

    private ValidationResult(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean hasError() {
        return !isSuccess();
    }

    public String error() {
        if (isSuccess()) {
            throw new IllegalStateException("ValidationResult does not have an error");
        }

        return error;
    }

    public static ValidationResult success() {
        return new ValidationResult(null);
    }

    public static ValidationResult failed(String error) {
        return new ValidationResult(error);
    }

}
