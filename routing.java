

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class routing {
	/*Starting with source node, keep each node as destination, run dijkstra,
	 *  get first hop value in shortest path. Now in Trie for source node, 
	 *  traverse with IP of every other node and insert hop values. 
	 *  Store the Trie in hash map with key as the source node you started with. 
	 *  Now search IP with longest prefix match with final destination node, get the first hop, 
	 * then refer the hashmap with key value as that hop value and get next hop. Repeat till you hit final destination. 
	 * Print prefix on your way.*/
	public static void main(String[] args) throws IOException {
		String srcId=args[2];
		String dstId=args[3];
		File file1 = new File(args[0]);
		File file2 = new File(args[1]);
		ArrayList<String> dijkstrResult = new ArrayList<String>();
		//run Dijkstra to get shortest path
		dijkstrResult = ssp.runDijkstra(file1, srcId,dstId);
		//print total weight
		System.out.println(dijkstrResult.get(dijkstrResult.size()-1));
		ArrayList<String> ipResult = BinaryConversion.convertIP(file2);		
		LinkedHashMap<String,Trie> nodeIp = new LinkedHashMap<String,Trie>();
		for(int j=dijkstrResult.size()-2;j>0;j--){
			String trieSource = dijkstrResult.get(j);
			//Create a new Trie for each node in shortest path except destination node
			Trie trie = new Trie();
			for (int i=0; i<ipResult.size(); i++)
			{ 	
				//Check first hop for each node as destination
				Integer val = i;
				//Skip when source == destination
				if(trieSource.equals(val.toString()))
					continue;
				ArrayList<String> dijkstrTrieResult = new ArrayList<String>();
				//Run dijkstra to get first hop for each destination
				dijkstrTrieResult =ssp.runDijkstra(file1, trieSource,val.toString());
				String insertKey = dijkstrTrieResult.get(dijkstrTrieResult.size()-3);
				int key = Integer.parseInt(insertKey);
				trie.insert(ipResult.get(i),key);				
			}

			nodeIp.put(trieSource, trie);
		}		

		for(Map.Entry<String,Trie> val : nodeIp.entrySet() )
		{   
			Trie trie1 = new Trie();
			trie1 = val.getValue();	
			TrieNode root = trie1.root;
			//condense IP using post order traversal
			trie1.postTraversal(root);                
			Integer i = trie1.search(ipResult.get(Integer.parseInt(dstId)));                

		}			



	}  
}
