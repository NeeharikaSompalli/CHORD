import java.util.*;

public class Node {
	
	int node_id;
	Node predecessor, successor;
	Fingertable fingertable;
	boolean valid;
	int numberofentries;
	Map< Integer, Node> mapref;
	
	public Node(int id, int numberofentries, Map<Integer, Node> map)
	{
		this.node_id=id;
		this.predecessor=null;
		this.successor=this;
		this.numberofentries=numberofentries;
		this.fingertable=new Fingertable(this, numberofentries);
		this.valid =true;
		this.initialize_fingertable(this, numberofentries);
		this.mapref=map;
	}

	public Node getsuccessor()
	{
		return this.successor;
	}
	public void setsuccessor(Node n)
	{
		 this.successor=n;
	}
	public Node getpredecessor()
	{
		return this.predecessor;
	}
	public void setpredecessor(Node n)
	{
		this.predecessor=n;
	}

	public Node findsuccessor(int id, Map<Integer, Node> joinednodes)
	{
		Node n_dash=find_predecessor( id, joinednodes);
		return n_dash.successor;
	}

	public void fixfingertable(Map<Integer, Node> nodemap, int numberofentries) {

		List<Integer> all = new ArrayList<Integer>();
		all.addAll(nodemap.keySet());
		all.remove(new Integer(this.node_id));
		Collections.sort(all);
		for (int i = 0; i < numberofentries; i++) {
			int id = this.fingertable.fingers.get(i).start;
			flag: for (int key : all) {
				if (key >= id) {
					this.fingertable.fingers.get(i).successor = nodemap.get(key);
					this.fingertable.fingers.get(i).setSuccessor(nodemap.get(key));
					break flag;
				}
				else
				{
					this.fingertable.fingers.get(i).setSuccessor(nodemap.get(all.get(0)));
				}
			}

		}

	}

	private Node find_predecessor( int id, Map<Integer, Node> joinednodes) {
		Node n_dash = this;
		int n_dash_id = n_dash.node_id;
		int n_dash_suc_id= n_dash.successor.node_id;
		while(!ininterval_open_closed(id, n_dash_id,n_dash_suc_id))
		{
			n_dash =n_dash.closest_precedingfinger(id, joinednodes);
			n_dash_id=n_dash.node_id;
			n_dash_suc_id=n_dash.successor.node_id;
		}
		return n_dash;
	}
	
	
	private Node closest_precedingfinger(int id, Map<Integer, Node> joinednodes) {
		int  value=0;
		List<Integer> sortedkeys= new ArrayList<Integer>(joinednodes.size());
		sortedkeys.addAll(joinednodes.keySet());
		Collections.sort(sortedkeys);
		for(int i =numberofentries-1 ;i>=0;i--)
		{	
			value= this.fingertable.fingers.get(i).successor.node_id;
			if(openinterval(value, this.node_id, id)) return joinednodes.get(value);
			
			}
		return this;
			
		}
	
	private boolean ininterval_open_closed(int tar, int left_id, int right_id)
		{
		if(left_id >= right_id)
		{
			if( left_id < tar || right_id >=tar)
				return true;
		}
		else if( left_id< tar && right_id>=tar)
		{
			return true;
		}
			return false;
	}
	private boolean interval_closed_open(int tar, int left_id, int right_id)
	{
		if(left_id >= right_id)
		{
			if( left_id <= tar || right_id >tar)
				return true;
		}
		else if( left_id<= tar && right_id>tar)
		{
			return true;
		}
		return false;
	}


	private boolean openinterval(int tar, int left_node, int right_node) {
		int id1= left_node;
		int id2= right_node;
		if(id1 >= id2)
		{
			if( id1 < tar || id2 >tar)
				return true;
		}
		else if( id1< tar && id2 >tar)
		{
			return true;
		}
			return false;
	}

	public void printfingertable( )
	{
		System.out.println("Inside Finger table- print table");
		this.printtable();
	}

	public boolean checkvalid()
    {
		return this.valid;
	}

    public void stabilize() {
		Node successor = this.successor;
		if(!mapref.containsKey(successor.node_id))
		{
			for(Finger i : this.fingertable.fingers)
			{
			if (i.successor !=this.successor)
			{
				successor = i.getSuccessor();
				break;
			}
			}
			this.setsuccessor(successor);
		}
        Node newadd =this.successor.predecessor;
        if(newadd !=null && newadd.checkvalid())
        {
            if(newadd.openinterval(newadd.node_id,this.node_id, this.successor.node_id)) {
                this.successor=newadd;
            }
        }
        this.successor.notify(this);

    }


    public void notify(Node node)
	{	Node predecessor = this.predecessor;
		if(!mapref.containsKey(predecessor.node_id))
		{
			this.setpredecessor(null);
		}
		if(this.predecessor== null || this.predecessor.checkvalid()==true || node.openinterval(node.node_id,this.predecessor.node_id, this.node_id)) {
            this.predecessor = node;
        }
		
	}

    public void printtable()
    {
        System.out.println(" Start | End | Successor");

        for (int i=0;i<this.fingertable.fingers.size();i++)
        {
            Finger f = this.fingertable.fingers.get(i);
            System.out.println(f.start+ "    |   "+ f.end+ "    |   "+ f.getSuccessor().node_id);
        }
    }

    public void initialize_fingertable(Node node, int numofentries)
    {
        this.fingertable.fingers = new ArrayList<Finger>();
        int id = node.node_id;
        int size =(int)Math.pow(2,numofentries);
        for(int i=0;i<numofentries;i++)
        {
            int start = (id+ (int)Math.pow(2, i))% size;
            int end = (id+(int)Math.pow(2, i+1))% size;
            Node successor = node;
            this.fingertable.fingers.add(new Finger(start, successor, end));
        }

    }

    public void init_finger_table(Node n, Map<Integer, Node> joinednodes)
	{
		//this.successor=n.findsuccessor(n.node_id, joinednodes);
		this.successor.predecessor=this;
		this.predecessor=this.successor.predecessor;


		for(int i=0; i< this.numberofentries-1;i++)
		{
			if(interval_closed_open(fingertable.fingers.get(i+1).start,this.node_id, this.fingertable.fingers.get(i).successor.node_id))
			{
				//this.fingertable.fingers.get(i+1).setSuccessor(n.findsuccessor(this.fingertable.fingers.get(i+1).start, joinednodes));
				this.fingertable.fingers.get(i+1).setSuccessor(this.fingertable.fingers.get(i).successor);
			}
			else
			{
				this.fingertable.fingers.get(i+1).setSuccessor(n.findsuccessor(this.fingertable.fingers.get(i+1).start, joinednodes));
			}
		}
	}
	public void update_others(Map<Integer, Node> joinednodes)
	{
		for( int i =0; i< this.numberofentries;i++)
		{
			Node p = find_predecessor(this.node_id - ((int)Math.pow(2, i)), joinednodes);
			p.update_finger_table(this.node_id, i, joinednodes);

		}
		}

		public void update_finger_table(int s, int i, Map<Integer, Node> joinednodes)
		{
			if( interval_closed_open(s, this.node_id, this.fingertable.fingers.get(i).successor.node_id) )
			{
				this.fingertable.fingers.get(i).setSuccessor(joinednodes.get(s));
				Node p = this.predecessor;
				p.update_finger_table(s, i, joinednodes);
			}
	}

}
