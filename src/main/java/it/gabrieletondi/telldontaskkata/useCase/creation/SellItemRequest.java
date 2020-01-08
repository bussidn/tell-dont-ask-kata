package it.gabrieletondi.telldontaskkata.useCase.creation;

public class SellItemRequest {
    private int quantity;
    private String productName;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    int getQuantity() {
        return quantity;
    }

    String getProductName() {
        return productName;
    }
}
