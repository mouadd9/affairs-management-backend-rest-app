package com.example.AffairsManagementApp.Exceptions;

public class RoleAlreadyAssignedToThisUser extends Exception{
    public RoleAlreadyAssignedToThisUser (String message) {
        super(message);
    }
}
