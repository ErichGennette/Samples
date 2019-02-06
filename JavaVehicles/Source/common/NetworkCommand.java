package common;

import java.io.Serializable;

public class NetworkCommand implements Serializable {
	
	private static final long serialVersionUID = 2640699340897179385L;
	
	public enum netCommands {
		No_Command, Reset , Change_Movement , Add_Vehicle , Remove_Vehicle
    }

	private netCommands comm;


	 public NetworkCommand(netCommands comm) {
		this.comm = comm;
	}  
	
	 public NetworkCommand(NetworkCommand n) {
		this.comm = n.comm;
	}  
	
	public netCommands getComm() {
		return comm;
	}
	
	public String toString() {
		return comm.name();
	}
	
}
