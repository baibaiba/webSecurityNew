package demo.securitystarter.dto;

public class ClientDetail {
    private String clientId;
    private String clientName;
    private String clientKey;
    private String description;

    public ClientDetail(String clientId, String clientName, String clientKey, String description) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientKey = clientKey;
        this.description = description;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
