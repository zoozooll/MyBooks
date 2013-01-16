package com.chinachip.tree;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BackList extends SuperTree {
	private String enc = "GBK";
	private Node cursor = null; //当前节点
	
	/**
	 * @param enc the enc to set
	 */
	public void setEnc(String enc) {
		this.enc = enc;
	}

	/**
	 * @return the enc
	 */
	public String getEnc() {
		return enc;
	}

	/**
	 * 
	 */
	public void doworkall() {
		workall(root);
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public ArrayList<Node> getNextNodeList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();

		nodes.add(node);
		Node next = (Node) node.next;

		do {
			if (next != node) {
				nodes.add(next);
			}
			next = (Node) next.next;
		} while (next != node);

		return nodes;
	}
	
	public ArrayList<Node> getCurrNodeList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		if(node.next == node){
			nodes.add(node);
			return nodes;
		}else{
			Node parentNode = (Node)node.parent;
			if (parentNode == null) {
				parentNode = (Node) root;
				nodes = getNextNodeList(parentNode);
				return nodes;
			}else {
				nodes = getChildNodeList(parentNode);
			}

		}
		
		return nodes;
	}


	/**
	 * 
	 * @param node
	 * @return
	 */
	public ArrayList<Node> getParentNodeList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node parentNode = (Node) node.parent;
		if (parentNode == null) {
			parentNode = (Node) root;
			nodes = getNextNodeList(parentNode);
			return nodes;
		}
		parentNode = (Node) parentNode.parent;
		if (parentNode == null) {
			parentNode = (Node) root;
			nodes = getNextNodeList(parentNode);
		} else {
			nodes = getChildNodeList(parentNode);
		}

		return nodes;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public ArrayList<Node> getChildNodeList(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		if (node.child == null) {
			return nodes;
		} else {
			return nodes = getNextNodeList((Node) node.child);
		}

	}
	
	public Node getPreNode(Node node)
	{
		if(isRoot(node)){
			return null;
		}
		Node returnNode = (Node)node.prev;
		if(node.parent == null)
		{
			return returnNode;
		}
		if((node.prev == node)||(node == node.parent.child))	
		{
			return (Node)node.parent;
		}
			
		returnNode = (Node)node.prev.child;
		
		while(returnNode != null)
		{
		   if(returnNode != (Node)returnNode.prev)
			   return (Node)returnNode.prev;
			   
		  Node tempNode = (Node)returnNode.child;
		  if(tempNode == null)
			  return returnNode;
		  returnNode = tempNode;
		}
		return (Node)node.prev;

	}
	
	public Node getPrevData(Node node){
		Node prevNode = getPreNode(node);
		if(prevNode == null)
			return null;
		while(prevNode.child != null){
			prevNode = getPreNode(prevNode);
		}
		return prevNode;		
		
	}

	public Node getNextNode(Node node){
		Node temp = node;
		if(temp.parent == null)
		{
			if(isRoot((Node)temp.next))
				return null;
			else
				return (Node)temp.next;
		}
		while((temp.next == temp)||(temp.next == temp.parent.child)){
			temp = (Node)temp.parent;
			if(temp.parent == null)
			{
				if(isRoot((Node)temp.next))
					return null;
				else
					break;
			}
			
			if(temp.prev != temp){
			{
				if(temp.next != temp.parent.child)
					break;
			}			
		}
		}

		return (Node)temp.next;
	}
	
	public Node getNextData(Node node)
	{
		Node nextNode = getNextNode(node);
		if(nextNode == null)
			return null;
		while(nextNode.parent != null){
			nextNode = getNextNode(nextNode);
		}
		return nextNode;
	}
	/**
	 * 
	 * @param node
	 * @return
	 */
	public SuperNode getChildNode(SuperNode node) {
		return node.child;
	}

	/**
	 * 
	 * @param node
	 */
	public void workall(SuperNode node) {
		Node root = (Node) node;
		Node curr = root;
		do {
			Node next = (Node) getNext(curr);
			if (curr.child != null)
				workall(curr.child);
			curr = next;

		} while (curr != root);
	}
	
	public int getHeadFile(Node node)
	{	
		Node root = (Node)node;
		return root.file;
	}
	
	public int getEndFile()
	{	
		Node root = (Node)getRoot();
		
		while(root.prev != root)
		{
			root = (Node)root.prev;
			if(root.child == null)
				return root.file;
			root = (Node)root.child;
			if(root.prev == root)
				break;
		}
		
		while(root.next == root)
		{
			if(root.child == null)
				break;
			
			root = (Node)root.child;
			
			if(root.next == root)
				continue;
			root = (Node)root.prev;
		}
		
		return root.file;
	}

	/**
	 * @param node
	 * @return true or false
	 * @author vrix.yan
	 */
	public boolean isRoot(Node node) {
		Node parentNode = (Node) node.parent;
		if (parentNode == null)
			return true;
		return false;
	}

	/**
	 * @param node
	 * @return node's title
	 * @throws UnsupportedEncodingException
	 * @author vrix.yan
	 */
	public String getTitle(Node node) throws UnsupportedEncodingException
	{
		return node.getTitle(enc);
	}
	
	/**
	 * @param  
	 * @return cursor's title
	 * @throws UnsupportedEncodingException
	 * @author vrix.yan
	 */
	public String getTitle() throws UnsupportedEncodingException
	{
		if( cursor!= null)
			return getTitle(cursor);
		return "";
	}
	
	/**
	 * 函  数：addRoot
	 * 功  能：增加一个Root节点 （ROOT只能有一个哦！）
	 * 参  数：title 标题； file: 文件索引
	 * 返  回：新节点引用
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public Node addRoot(byte[] title,int file)
	{
		cursor = (Node) setRoot(new Node(title,file));
		return cursor;
	}
	
	/**
	 * 函  数：addNext
	 * 功  能：给当前节点添加一个后节点,如果没有根节点新增加的就是根节点
	 * 参  数：title 标题； file: 文件索引
	 * 返  回：新节点引用
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public Node addNext(byte[] title,int file)
	{
		if(cursor == null)
			return addRoot(title,file);
		
		cursor = (Node) addNext(cursor,new Node(title,file));
		return cursor;
	}
	
	/**
	 * 函  数：addChild
	 * 功  能：向当前的节点添加一个子节点,如果没有根节点，新添加的就是根节点
	 * 参  数：title 标题； file: 文件索引
	 * 返  回：新节点引用
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public Node addChild(byte[] title,int file)
	{
		if(cursor == null)
			addRoot(title,file);
		
		cursor = (Node) addChild(cursor,new Node(title,file));
		return cursor;
	}
	
	/**
	 * 函  数：toChild
	 * 功  能：游标移动到子节点	
	 * 参  数：无
	 * 返  回：true cursor存在且有子节点，则成功；false 移动失败
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public boolean toChild()
	{
		if(cursor != null)
		{
			Node node = (Node) getChild(cursor);
			if(node != null)
			{
				cursor = node;
				return true;
			}
		}
		return false;
	}

	/**
	 * 函  数：toNext
	 * 功  能：游标移动到下一个节点	
	 * 参  数：无
	 * 返  回：true cursor存在且有下一个节点则成功；false 失败
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */	
	public boolean toNext()
	{
		if(cursor != null)
		{
			cursor = (Node) getNext(cursor);
			return true;
		}
		return false;
	}
	
	/**
	 * 函  数：toChild
	 * 功  能：游标移动到子节点
	 * 参  数：无
	 * 返  回：true cursor存在且有父节点则成功；false 失败
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public boolean toParent()
	{
		if(cursor != null)
		{
			Node node = (Node) getParent(cursor);
			if(node != null)
			{	
				cursor = node;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 函  数：isRoot
	 * 功  能：判断当前节点是否是根节点
	 * 参  数：无
	 * 返  回：true 成功；false 失败
	 * 作  者：vrix.yan 2011.03.19 09:02
	 */
	public boolean isRoot()
	{
		if(isRoot(cursor))
			return true;
		return false;
	}
	
	public void initTree() {
		BackList tree = new BackList();
		Node root = (Node) tree.setRoot(new Node());
		root.setTitle(new byte[] { 'A', '1' });

		Node node1 = (Node) tree.addLast(root, new Node());
		node1.setTitle(new byte[] { 'B', '2' });

		Node node2 = (Node) tree.addLast(root, new Node());
		node2.setTitle(new byte[] { 'C', '5' });

		Node node6 = (Node) tree.addLast(root, new Node());
		node6.setTitle(new byte[] { 'F', '6' });

		Node node3 = (Node) tree.addChild(node1, new Node());
		node3.setTitle(new byte[] { 'D', '3' });

		Node node4 = (Node) tree.addChild(node1, new Node());
		node4.setTitle(new byte[] { 'E', '4' });

		Node node7 = (Node) tree.addChild(node4, new Node());
		node7.setTitle(new byte[] { 'R', '9' });

		Node node8 = (Node) tree.addChild(node4, new Node());
		node8.setTitle(new byte[] { 'S', '9' });

		Node node10 = (Node) tree.addChild(node3, new Node());
		node10.setTitle(new byte[] { 'S', '9' });
	}
}
