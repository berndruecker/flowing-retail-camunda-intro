package io.flowing.retail.monolith.entitystate;

import java.util.UUID;

@SuppressWarnings("unused")
public class Order {

  protected String id = UUID.randomUUID().toString();

  
  public static enum GoodsDeliveryStatus {
    NOTHING_DONE, GOODS_RESERVED, GOODS_PICKED
  }

  private boolean paymentReceived = false;
  private GoodsDeliveryStatus deliveryStatus = GoodsDeliveryStatus.NOTHING_DONE;
  private boolean shipped = false;
  
  
}
