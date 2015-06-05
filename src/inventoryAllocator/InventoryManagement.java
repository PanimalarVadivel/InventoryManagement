package inventoryAllocator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryManagement {
	
	
	private static List inventoryOriginal = new ArrayList<InventoryItem>(Arrays.asList(new InventoryItem("Apple", 150), new InventoryItem("Banana", 150),new InventoryItem("Carrot", 100),
			new InventoryItem("Donut", 100), new InventoryItem("Egg", 200)));
	
	private static List inventoryCurrent = new ArrayList<InventoryItem>(Arrays.asList(new InventoryItem("Apple", 150), new InventoryItem("Banana", 150),new InventoryItem("Carrot", 100),
			new InventoryItem("Donut", 100), new InventoryItem("Egg", 200)));
	
	
	public static List getInventoryItemNames(){
		List itemNames = new ArrayList();
		for (int i=0; i<inventoryCurrent.size(); i++){
			itemNames.add(((InventoryItem) inventoryOriginal.get(i)).getItemName());
		}
		return itemNames;
	}

	
	public static void printInventorySnapShot() throws IOException{
		LogMessages.printMessage("InventoryManagement :: printInventorySnapShot :: Items in Inventory ::" + inventoryCurrent.size());
		for (int i=0; i<inventoryCurrent.size(); i++){
			LogMessages.printMessage(((InventoryItem) inventoryCurrent.get(i)).getItemName() +" :: " + ((InventoryItem) inventoryCurrent.get(i)).getItemCount());
		}
	}
	
	public static boolean checkifItemIsValid(String name, Long count) throws IOException{
		LogMessages.printMessage("InventoryManagement :: checkifItemIsValid :: Name :: "+ name + " Count :: " + count);
		for (int i=0; i<inventoryOriginal.size(); i++){
			InventoryItem item = ((InventoryItem) inventoryOriginal.get(i));
			if (name.equals(item.getItemName())){
				//LogMessages.printMessage("Found a match for :: " + name +" against :: " + item.getItemName());
				if (count <=item.getItemCount() && count >0){
					//Item is valid if within the 0 and the item count from inventory
					//LogMessages.printMessage("Given count :: " + count + " Maximum Count :: " + item.getItemCount());
				return true;
				}else{
					LogMessages.printMessage("Given count for item :: "+ name +" is :: " + count + ". Maximum Count can only be :: " + item.getItemCount());
					return false;
				}
				
			}
		}
		return false;
	}
	
	public static Long getAvailabilityForItem (String name) throws IOException{
		//LogMessages.printMessage("InventoryManagement :: getAvailabilityForItem:: Checking availability for item "+ name+" in the inventory");
		for (int i=0; i<inventoryCurrent.size(); i++){
			InventoryItem item = ((InventoryItem) inventoryCurrent.get(i));
			if (name.equals(item.getItemName())){
				return item.getItemCount();
			}
		}
		return (long) 0;
	}

	public static void updateInventory (String name, Long count) throws IOException{
		LogMessages.printMessage("Updating Inventory for the item :: " + name + " with count :: " + count);
		for (int i=0; i<inventoryCurrent.size(); i++){
			InventoryItem item = ((InventoryItem) inventoryCurrent.get(i));
			if (name.equals(item.getItemName())){
				LogMessages.printMessage("Current availability in inventory for item :: " + item.getItemName() + " is :: " + item.getItemCount() );
				item.setItemCount(item.getItemCount()-count);
				LogMessages.printMessage("After updating, Current availability in inventory for item :: " + item.getItemName() + " is :: " + item.getItemCount() );
			}
		}
	}
	
	public static boolean isInventoryEmpty () throws IOException{
		LogMessages.printMessage("Checking if Inventory is empty");
		int itemUnavailable =0;
		for (int i=0; i<inventoryCurrent.size(); i++){
			InventoryItem item = ((InventoryItem) inventoryCurrent.get(i));
			if (item.getItemCount() == 0){
				//LogMessages.printMessage("Item unavailable :: " + item.getItemName());
				itemUnavailable ++;
			}		
		}
		if (itemUnavailable == inventoryCurrent.size()){
			LogMessages.printMessage("All items out of stock :: " + itemUnavailable);
			return true;
		}
		return false;
	}
		
}


