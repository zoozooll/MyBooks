package com.chinachip.tree;


public class SuperTree {
	
	public SuperNode root = null;
	
	public SuperTree()
	{
		
	}
	
	public SuperTree(SuperTree tree)
	{
		this.root = tree.root;
	}
	
	public boolean delTree()
	{
		root = null;
		return true;
	}
	
	public SuperNode getRoot()
	{
		return root;
	}
	
	public SuperNode setRoot(Node node)
	{
		root = node;
		if((node.prev == null)||(node.next == null))
			root.makeRoot();
		return root;
	}
	
	public SuperNode addLast(SuperNode root,SuperNode node)
	{
		if(root == null)
		{
			node.makeRoot();
			if(this.root != null)
				delTree();				
			this.root = node;
			return this.root;
		}
		
		root.addPrev(node);
		return node;
	}
	
	public SuperNode addNext(SuperNode curr,SuperNode node)
	{
		curr.addNext(node);
		return node;
	}
	

	public SuperNode addPrev(SuperNode curr,SuperNode node)
	{
		curr.addPrev(node);
		return node;
	}
	

	public SuperNode addChild(SuperNode curr,SuperNode node)
	{
		if(curr.child == null)
			node.makeRoot();
		
		curr.addChild(node);
		return node;
	}
	
	public SuperNode getNext(SuperNode node)
	{
		return node.next;
	}
	
	public SuperNode getPrev(SuperNode node)
	{
		return node.prev;
	}
	
	public SuperNode getChild(SuperNode node)
	{
		return node.child;
	}

	public SuperNode getParent(SuperNode node)
	{
		return node.parent;
	}
}
