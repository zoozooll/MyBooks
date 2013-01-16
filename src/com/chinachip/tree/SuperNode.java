package com.chinachip.tree;


public class SuperNode {
	
	public SuperNode next = null;
	public SuperNode prev = null;
	public SuperNode child = null;
	public SuperNode parent = null;
	
	public void makeRoot()
	{
		next = this;
		prev = this;
	}
	
	public SuperNode addNext(SuperNode node)
	{
		node.next = this.next;
		node.prev = this;
		node.parent = this.parent;
		this.next.prev = node;
		this.next = node;
		return node;
	}
	
	public SuperNode addPrev(SuperNode node)
	{
		node.next = this;
		node.prev = this.prev;
		node.parent = this.parent;
		this.prev.next = node;
		this.prev = node;		
		return node;
	}
	
	public SuperNode addChild(SuperNode node)
	{
		node.parent = this;
		if(child == null)
		{
			if((node.prev == null)||(node.next == null))
			 	node.makeRoot();
			child = node;
		}
		else
		{
			child.addPrev(node);
		}
		return node;
	}
}
