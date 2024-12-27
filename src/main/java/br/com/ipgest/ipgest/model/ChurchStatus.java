package br.com.ipgest.ipgest.model;

public enum ChurchStatus {

    CONGREGACAO("Congregação"),
    IGREJA("Igreja"),
    PONTO_DE_PREGACAO("Ponto de Pregação"),
    INATIVA("Inativa");

    private final String description;

    ChurchStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
