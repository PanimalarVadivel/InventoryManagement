package inventoryAllocator;

import java.util.List;

public class Orders {

	public static Long orderCount=(long) 0;
	public List items;
	private String streamId;
	private String header;
	private String orderState;
	private Long orderId;
	
	public Orders(String streamId, String header, List items) {
		super();
		this.orderId = ++orderCount;
		this.streamId = streamId;
		this.header = header;
		this.items = items;
	}

	public List getItems() {
		return items;
	}

	public Long getOrderId() {
		return orderId;
	}
	
	public String getStreamId() {
		return streamId;
	}
	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}	
	
}
