import java.util.*;

public class Chord {
	static int numberofentries ;
	static Map<Integer, Node> nodemap = new HashMap<Integer, Node>();
	static PriorityQueue<Integer> nodequeue = new PriorityQueue<Integer>();
	static Map<Integer, Node> joinednodes = new HashMap<Integer, Node>();

	
	public Chord(int num)
	{
		this.numberofentries= num;	
	}

	public void add(Node node)
	{	
		if(nodemap.isEmpty())
		{
			nodequeue.add(node.node_id);
			nodemap.put(node.node_id, node);
		
			System.out.println(" Added Node "+ node.node_id);
		}
		else
		{
			if(nodemap.containsKey(node.node_id))
			{
				System.out.println("ERROR: Node "+ node.node_id+" exists");
			}
			else
			{
				nodequeue.add(node.node_id);
				nodemap.put(node.node_id, node);
	
				System.out.println(" Added Node "+ node.node_id);
			}
		}
	}
	public static void join(Node nw, Node old)
	{	
		if(joinednodes.isEmpty())
		{
			//nw.predecessor=null;
			nw.successor=old.findsuccessor(nw.node_id, joinednodes);
			nw.fingertable.fingers.get(0).setSuccessor(nw.successor);


			nw.init_finger_table(old,joinednodes);
			//nw.update_others(joinednodes);

			joinednodes.put(nw.node_id, nw);
			joinednodes.put(old.node_id, old);
			//System.out.println(joinednodes.keySet());
			
		}
		else
		{
			if(joinednodes.containsKey(old.node_id))
			{
				nw.successor=old.findsuccessor(nw.node_id, joinednodes);
				nw.fingertable.fingers.get(0).setSuccessor(nw.successor);
				nw.init_finger_table(old, joinednodes);

				if(!joinednodes.containsKey(nw.node_id)) 
				{
					joinednodes.put(nw.node_id, nw);
				}
				//System.out.println(joinednodes.keySet());
			
			}
			else
			{
				for( int i=0 ; i< numberofentries;i++)
				{
					nw.fingertable.fingers.get(i).successor=nw;
				}
			}
		}
		
	}

	public static void leave(Node node)
	{
		node.successor=null;
		//node.predecessor=null;
		node.valid=false;
		nodemap.remove(node.node_id);
		joinednodes.remove(node.node_id);
		nodequeue.remove(node.node_id);
		System.out.println("Dropped Node "+ node.node_id);
	}


}


