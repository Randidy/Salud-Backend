package com.saludvital.mssaludvital.enums;

public enum Especialidad {
    CARDIOLOGIA("Cardiología"),
    DERMATOLOGIA("Dermatología"),
    GASTROENTEROLOGIA("Gastroenterología"),
    GINECOLOGIA("Ginecología"),
    NEUROLOGIA("Neurología"),
    OFTALMOLOGIA("Oftalmología"),
    PEDIATRIA("Pediatría"),
    PSIQUIATRIA("Psiquiatría"),
    TRAUMATOLOGIA("Traumatología"),
    UROLOGIA("Urología"),
    MEDICINA_GENERAL("Medicina General");

	private final String displayName;

    Especialidad(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Especialidad fromString(String text) {
        if (text == null) throw new IllegalArgumentException("Especialidad no puede ser nula");
        for (Especialidad e : Especialidad.values()) {
            if (e.displayName.equalsIgnoreCase(text) || e.name().equalsIgnoreCase(text)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Especialidad inválida: " + text);
    }}