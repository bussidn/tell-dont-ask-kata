package it.gabrieletondi.telldontaskkata.useCase;

class OrderApprovalOrRejectionRequest {
    private int orderId;
    private boolean approved;

    void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    int getOrderId() {
        return orderId;
    }

    void setApproved(boolean approved) {
        this.approved = approved;
    }

    boolean isApproved() {
        return approved;
    }
}
