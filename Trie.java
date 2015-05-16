

class Trie
{
	public TrieNode root;

	public Trie()
	{
		root = new TrieNode(' '); 
	}

	public void insert(String ip,int vertex)
	{
		/*Forms a Trie using characters in ip address and inserts key i.e. vertex in the leaf node*/
		if (search(ip) !=-1) 
			return;        
		TrieNode current = root; 
		for (char ch : ip.toCharArray() )
		{
			TrieNode child = current.subNode(ch);
			if (child != null)
				current = child;
			else 
			{
				if(ch=='1')
				{
					current.left=new TrieNode(ch);
					current=current.left;
				}
				else
				{
					current.right=new TrieNode(ch);
					current=current.right;
				}

			}
			current.count++;
		}
		current.isEnd = true;
		current.vertex=vertex;
	}


	public Integer search(String ip)
	{
		/*Searches for key value at end of a ip in Trie*/
		char[] result = new char[32];
		int i = 0;
		TrieNode current = root;  
		for (char ch : ip.toCharArray() )
		{
			if (current.subNode(ch) == null)
				//return -1;
			break;
			else
			{   
				result[i++] = ch;
				current = current.subNode(ch);
			}
		}      
		if (current.isEnd == true) 
		{  for( char ch : result)
		{   //Print compressed IP 
			if((ch=='1')||(ch=='0'))
			System.out.print(ch);
		}
		//Add a space after each IP
		System.out.print(" ");
		return current.vertex;
		}
		return -1;
	}


	public void postTraversal(TrieNode node)
	{
		/*Do a post order traversal on Trie. Compresses the trie in following conditions
		If node.left.vertex == node.right.vertex
	    If node.right == null && node.left != null
	    If node.left == null && node.right != null
		*/
		if ( node == null)
			return;
		postTraversal(node.left);
		postTraversal(node.right);
		if((node.left != null && node.left.isEnd == true) || (node.right != null) && node.right.isEnd == true )
		{
			if( node.left != null && node.right != null && node.left.vertex == node.right.vertex)
			{
				node.isEnd = true;
				node.vertex = node.left.vertex;
				node.left = null;
				node.right = null;
				return;

			}    		
			else if(node.right == null && node.left != null)
			{
				node.isEnd = true;
				node.vertex = node.left.vertex;
				node.left = null;
				return;
			}
			else if(node.left == null && node.right != null)
			{
				node.isEnd = true;
				node.vertex = node.right.vertex;
				node.right = null;
				return;
			}
			else
				return;
		}
	}
}

