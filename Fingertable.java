import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fingertable {
	List<Finger> fingers;
	
	public Fingertable(Node node, int numofentries) {
		fingers = new ArrayList<Finger>();
		int id = node.node_id;
		int size =(int)Math.pow(2,numofentries);
		for(int i=0;i<numofentries;i++)
		{
			int start = (id+ (int)Math.pow(2, i))% size;
			int end = (id+(int)Math.pow(2, i+1))% size;
			Node successor = node;
			fingers.add(new Finger(start, successor, end));
		}
	}

	public int[] getfingers()
	{	int[] fingersucs= new int[this.fingers.size()];
		int k=0;
		for( Finger i : this.fingers)
		{

			fingersucs[k]=i.successor.node_id;
			k++;
		}
		return fingersucs;
	}
}

class Finger {
	int start;
	Node successor;
	int end;


	Finger(int start, Node successor, int end) {
		this.start = start;
		this.successor = successor;
		this.end = end;
	}

	public Node getSuccessor() {
		return successor;
	}

	public void setSuccessor(Node suc) {
		this.successor = suc;
	}
}

