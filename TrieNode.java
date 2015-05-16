

class TrieNode 
{
/*Defines structure of each node in Trie*/
	char content; 
	boolean isEnd; 
	int count;  
	int vertex;
	TrieNode left;
	TrieNode right;

	/* Constructor */
	public TrieNode(char c)
	{
		isEnd = false;
		content = c;
		count = 0;
		left=null;
		right=null;
	}  
	public TrieNode subNode(char c)
	{
		if (left!=null)
			if(left.content == c)
				return left;
		if(right!=null)
			if(right.content==c)
				return right;
		return null;
	}
}