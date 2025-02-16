package com.gianmarques.supporticket.mapper.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClientUpdateDto {

    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 12)
    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 12)
    private String newPassword;

    @NotNull
    @Size(min = 8, max = 12)
    private String confirmPassword;

    public ClientUpdateDto() {
    }

    public ClientUpdateDto(String email, String oldPassword, String newPassword, String confirmPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @NotNull @Size(min = 8, max = 12) String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotNull @Size(min = 8, max = 12) String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotNull @Size(min = 8, max = 12) String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotNull @Size(min = 8, max = 12) String newPassword) {
        this.newPassword = newPassword;
    }

    public @NotNull @Size(min = 8, max = 12) String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotNull @Size(min = 8, max = 12) String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
