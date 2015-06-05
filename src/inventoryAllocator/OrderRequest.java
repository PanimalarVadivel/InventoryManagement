package inventoryAllocator;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
	public static void main(String args[]) throws ParserConfigurationException,
			SAXException, IOException {
		// Handling requests in the form of XML

		List<String> xmlOrderRequests = new ArrayList<String>();

		xmlOrderRequests
				.add("<order stream=\"1\" header =\"1\"><item><name>Apple</name><count>10</count></item><item><name>Carrot</name><count>30</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"2\"><item><name>Banana</name><count>5</count></item><item><name>Carrot</name><count>20</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"3\"><item><name>Donut</name><count>60</count></item><item><name>Egg</name><count>100</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"4\"><item><name>Apple</name><count>150</count></item><item><name>Banana</name><count>150</count></item>"
						+ "<item><name>Carrot</name><count>100</count></item><item><name>Donut</name><count>100</count></item>"
						+ "<item><name>Egg</name><count>200</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"5\"><item><name>Apple</name><count>150</count></item><item><name>Banana</name><count>150</count></item>"
						+ "<item><name>Carrot</name><count>100</count></item><item><name>Donut</name><count>100</count></item>"
						+ "<item><name>Egg</name><count>200</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"6\"><item><name>Donut</name><count>101</count></item><item><name>Egg</name><count>100</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"\"><item><name>Donut</name><count>100</count></item><item><name>Egg</name><count>100</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"8\"><item><name>Apple</name><count>140</count></item><item><name>Banana</name><count>145</count></item>"
						+ "<item><name>Carrot</name><count>50</count></item><item><name>Donut</name><count>40</count></item>"
						+ "<item><name>Egg</name><count>100</count></item></order>");
		xmlOrderRequests
				.add("<order stream =\"1\" header =\"\"><item><name>Donut</name><count>100</count></item><item><name>Egg</name><count>100</count></item></order>");
		LogMessages
				.printMessage("*******************************************************************************");
		InventoryManagement.printInventorySnapShot();
		LogMessages
				.printMessage("*******************************************************************************");
		for (int i = 0; i < xmlOrderRequests.size(); i++) {
			ProcessRequest.handleRequest(xmlOrderRequests.get(i).toString());
		}

	}
}

class ProcessRequest {
	private static List inventoryItemNames = InventoryManagement
			.getInventoryItemNames();
	private static List ordersList = new ArrayList();

	public static void handleRequest(String reqString)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(reqString));

		Document doc = docBuilder.parse(is);

		doc.getDocumentElement().normalize();
		LogMessages
				.printMessage("*******************************************************************************");
		LogMessages.printMessage("handleRequest :: Checking on the request :: "
				+ reqString);

		if (!InventoryManagement.isInventoryEmpty()) {
			LogMessages
					.printMessage("handleRequest :: Inventory is not empty! Processing the received Order Request");
			ordersList.add(processXML.handleInputXML(doc));
			LogMessages
					.printMessage("*******************************************************************************");
			InventoryManagement.printInventorySnapShot();
			LogMessages
					.printMessage("*******************************************************************************");
		} else {
			LogMessages.printMessage("WARNING! :: Inventory is EMPTY!");
			LogMessages.printMessage("Total Number of order handled so far :: "
					+ ordersList.size());
			for (int i = 0; i < ordersList.size(); i++) {
				printOrderDetails((Orders) ordersList.get(i));
			}
			LogMessages
					.printMessage("*******************************************************************************");

		}

	}

	private static void printOrderDetails(Orders ordObj) throws IOException {
		// Process only valid orders
		if (ordObj.getOrderState().equals("VALID")) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(getAllorderItemDetails(ordObj));
			stringBuffer.append(getProcessedOrdItmDetails(ordObj));
			stringBuffer.append(getBackorderedOrdItmDetails(ordObj));
			LogMessages.printMessage(stringBuffer.toString());
		} else {
			// Skip invalid orders
			LogMessages.printMessage("Skipping invalid order :: "
					+ ordObj.getOrderId());
		}
	}

	private static String getBackorderedOrdItmDetails(Orders ordObj) {
		// Retrieve items having 'backordered' status
		StringBuffer stringBuffer = new StringBuffer();
		for (int invItm = 0; invItm < inventoryItemNames.size(); invItm++) {
			boolean foundMatch = false;
			for (int ordItm = 0; ordItm < ordObj.getItems().size(); ordItm++) {
				InventoryItem item = (InventoryItem) ordObj.getItems().get(
						ordItm);
				if (inventoryItemNames.get(invItm).toString()
						.equals(item.getItemName())
						& item.getItemState().equals("BackOrdered")) {
					foundMatch = true;
					stringBuffer.append(item.getItemCount());
				}
			}
			if (!foundMatch) {
				stringBuffer.append("0");
			}
			if (invItm < inventoryItemNames.size() - 1) {
				stringBuffer.append(",");
			} else {
				stringBuffer.append("\n");
			}

		}
		return stringBuffer.toString();
	}

	private static String getProcessedOrdItmDetails(Orders ordObj) {
		// Retrieve items having 'processed' status
		StringBuffer stringBuffer = new StringBuffer();
		for (int invItm = 0; invItm < inventoryItemNames.size(); invItm++) {
			boolean foundMatch = false;
			for (int ordItm = 0; ordItm < ordObj.getItems().size(); ordItm++) {
				InventoryItem item = (InventoryItem) ordObj.getItems().get(
						ordItm);
				if (inventoryItemNames.get(invItm).toString()
						.equals(item.getItemName())
						& item.getItemState().equals("Processed")) {
					foundMatch = true;
					stringBuffer.append(item.getItemCount());
				}
			}
			if (!foundMatch) {
				stringBuffer.append("0");
			}
			if (invItm < inventoryItemNames.size() - 1) {
				stringBuffer.append(",");
			} else {
				stringBuffer.append("::");
			}
		}
		return stringBuffer.toString();

	}

	private static String getAllorderItemDetails(Orders ordObj) {
		// Retrieve all items for the order 
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Details for the order :: " + ordObj.getOrderId()
				+ " ::");
		for (int invItm = 0; invItm < inventoryItemNames.size(); invItm++) {
			boolean foundMatch = false;
			for (int ordItm = 0; ordItm < ordObj.getItems().size(); ordItm++) {
				InventoryItem item = (InventoryItem) ordObj.getItems().get(
						ordItm);
				if (inventoryItemNames.get(invItm).toString()
						.equals(item.getItemName())) {
					foundMatch = true;
					stringBuffer.append(item.getItemCount());
				}
			}
			if (!foundMatch) {
				stringBuffer.append("0");
			}
			if (invItm < inventoryItemNames.size() - 1) {
				stringBuffer.append(",");
			} else {
				stringBuffer.append("::");
			}
		}
		return stringBuffer.toString();
	}

}

class processXML {
	public static Orders handleInputXML(Document doc) throws IOException {
		String streamId;
		String header;
		InventoryItem itemInfo = null;
		Orders orderObj = null;
		List items = null;

		Node orderNode = doc.getDocumentElement();
		NodeList itemList = orderNode.getChildNodes();

		// Retrieve Order Details
		streamId = orderNode.getAttributes().getNamedItem("stream")
				.getNodeValue();
		header = orderNode.getAttributes().getNamedItem("header")
				.getNodeValue();
		items = handleItemList(itemList);

		// Set up Order Details
		orderObj = new Orders(streamId, header, items);
		processOrder(orderObj);
		return orderObj;
	}

	private static void processOrder(Orders orderObj) throws IOException {
		// Validating Order details
		boolean isOrderValid = isOrderValid(orderObj);
		if (isOrderValid) {
			// Process only of order is valid
			LogMessages
					.printMessage("Order Request :: processOrder :: Order is Valid. Processing further.");
			orderObj.setOrderState("VALID");
			processOrderItems(orderObj.items);
		} else {
			LogMessages
					.printMessage("Order Request :: processOrder :: Order is InValid. Not Processing further.");
			orderObj.setOrderState("INVALID");
		}

		// Need to do a duplicate order check as well.
	}

	private static void processOrderItems(List<InventoryItem> items)
			throws IOException {
		/* Find if item is in inventory and process if the item is in stock. If not, backorder the item*/
		for (int i = 0; i < items.size(); i++) {
			String itemName = ((InventoryItem) items.get(i)).getItemName();
			Long itemCount = ((InventoryItem) items.get(i)).getItemCount();
			Long availableCount = InventoryManagement
					.getAvailabilityForItem(itemName);
			// Process an item only if available in inventory
			if (availableCount >= itemCount) {
				LogMessages
						.printMessage("OrderRequest :: processOrderItems :: Processed OrderItem :: "
								+ itemName
								+ " Available Count :: "
								+ availableCount + " itemCount :: " + itemCount);
				((InventoryItem) items.get(i)).setItemState("Processed");
				InventoryManagement.updateInventory(itemName, itemCount);
			} else {
				// back order items that are not in inventory
				LogMessages
						.printMessage("OrderRequest :: processOrderItems :: BackOrdered OrderItem :: "
								+ itemName
								+ " Available Count :: "
								+ availableCount + " itemCount :: " + itemCount);
				((InventoryItem) items.get(i)).setItemState("BackOrdered");
			}

		}

	}

	private static boolean isOrderValid(Orders orderObj) throws IOException {
		LogMessages
				.printMessage("OrderRequest :: isOrderValid :: Checking if the order is valid :: Order ID :: "
						+ orderObj.getOrderId());
		boolean isValid = true;
		// Order cannot have null or empty Stream Id or Header ID
		LogMessages.printMessage("Validating Order Header and Stream IDs");
		// Order cannot have null or empty Stream Id
		if (orderObj.getStreamId().toString() != null
				&& !(orderObj.getStreamId().toString().equals(""))) {
			// Order cannot have null or empty Header Id
			if (orderObj.getHeader().toString() != null
					&& !(orderObj.getHeader().toString().equals(""))) {
				LogMessages
						.printMessage("Valid Stream Id and Header ID for order :: "
								+ orderObj.getOrderId());
				if (orderObj.items.size() > 0) {
					List items = orderObj.items;
					for (int i = 0; i < items.size(); i++) {
						/* checking if order items belong to the inventory and
						 the count is valid*/
						String itemName = ((InventoryItem) items.get(i))
								.getItemName();
						Long itemCount = ((InventoryItem) items.get(i))
								.getItemCount();
						boolean isValidItem = InventoryManagement
								.checkifItemIsValid(itemName, itemCount);
						if (!isValidItem) {
							LogMessages
									.printMessage("OrderRequest :: isOrderValid :: Item name or count is/are not valid");
							isValid = false;
						}

					}
				} else {
					// Order cannot have empty order items
					LogMessages
							.printMessage("OrderRequest :: isOrderValid :: Number of items is zero");
					isValid = false;
				}
			} else {
				LogMessages.printMessage("Null or Empty Header ID");
				isValid = false;
			}
		} else {
			LogMessages.printMessage("Null or Empty Stream ID");
			isValid = false;
		}

		return isValid;
	}

	private static List handleItemList(NodeList itemList) throws IOException {
		// Handle item Node from the request XML
		List items = new ArrayList();
		for (int i = 0; i < itemList.getLength(); i++) {
			InventoryItem itemInfo = null;
			Node order = itemList.item(i);
			if (order.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) order;
				String name = handleTags("name", element);
				Long count = Long.parseLong(handleTags("count", element));
				// Create an item object for the name and count
				itemInfo = new InventoryItem(name, count);
				LogMessages
						.printMessage("OrderRequest :: handleItemList :: Items Details :: "
								+ i
								+ " :: name :: "
								+ itemInfo.getItemName()
								+ " :: count :: " + itemInfo.getItemCount());
			}
			// Add item to the order Item List
			items.add(itemInfo);
		}
		return items;
	}

	private static String handleTags(String tagName, Element element)
			throws IOException {
		// Retrieve the value for the given tag
		StringBuffer returnValue = new StringBuffer();
		for (int i = 0; i < element.getElementsByTagName(tagName).getLength(); i++) {
			NodeList nodeList = element.getElementsByTagName(tagName).item(i)
					.getChildNodes();
			Node node = nodeList.item(0);
			returnValue.append(node.getNodeValue());
		}
		return returnValue.toString();
	}

}