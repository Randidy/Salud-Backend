package com.saludvital.mssaludvital.enums;

public enum DiaSemana {
    LUNES("lunes"),
    MARTES("martes"),
    MIERCOLES("miércoles"),
    JUEVES("jueves"),
    VIERNES("viernes"),
    SABADO("sábado"),
    DOMINGO("domingo");

    private final String displayName;

    DiaSemana(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DiaSemana fromString(String text) {
        if (text == null) throw new IllegalArgumentException("Día no puede ser nulo");

        String normalized = text.trim().toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        switch (normalized) {
            case "lunes":
            case "monday":
                return LUNES;
            case "martes":
            case "tuesday":
                return MARTES;
            case "miercoles":
            case "wednesday":
                return MIERCOLES;
            case "jueves":
            case "thursday":
                return JUEVES;
            case "viernes":
            case "friday":
                return VIERNES;
            case "sabado":
            case "saturday":
                return SABADO;
            case "domingo":
            case "sunday":
                return DOMINGO;
            default:
                throw new IllegalArgumentException("Día inválido: " + text);
        }
    }
}
