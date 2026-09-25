package data;

import data.enums.UserRole;

import jakarta.persistence.AttributeConverter;

public class UserRoleConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole role) {
        return switch (role) {
            case admin -> "admin";
            case partner -> "partner";
            case supply_manager -> "supply_manager";
            case order_manager -> "order_manager";
            case client -> "client";
        };
    }

    @Override
    public UserRole convertToEntityAttribute(String role) {
        System.out.println(role);
        return switch (role) {
            case "admin" -> UserRole.admin;
            case "partner" -> UserRole.partner;
            case "supply_manager" -> UserRole.supply_manager;
            case "order_manager" -> UserRole.order_manager;
            case "client" -> UserRole.client;
            default -> throw new IllegalArgumentException("Unexpected database column value met");
        };
    }
}
