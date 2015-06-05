package inventoryAllocator;

/*
 A Class to create an object of the inventory item containing the name and the count
 */

public class InventoryItem {
	
	private String itemName;
	private Long itemCount;
	private String itemState;
	
	public InventoryItem(String name, long count) {
		this.itemName = name;
		this.itemCount = count;
	}

	public String getItemState() {
		return itemState;
	}

	public void setItemState(String itemState) {
		this.itemState = itemState;
	}

	public String getItemName(){
		return this.itemName;
	}
	
	public Long getItemCount(){
		return this.itemCount;
	}
	
	public void setItemCount(Long count){
		this.itemCount = count;
	}
	
}
