package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.util.List;

public class SellItemsRequest {
    private final int id;
    private List<SellItemRequest> requests;

    public SellItemsRequest(int id) {
        this.id = id;
    }

    public void setRequests(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }

    int orderId() {
        return id;
    }
}
