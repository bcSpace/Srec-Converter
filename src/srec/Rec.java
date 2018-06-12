package srec;

public class Rec {

	int addressStart;
	int addressLength;
	byte data[]; 
	
	Rec(int addressStart, int nextAddress, byte data[]) {
		this.addressStart = addressStart;
		this.addressLength = nextAddress - addressStart;
		
		if(addressStart == nextAddress) this.data = new byte[data.length];
		else this.data = new byte[addressLength];
		
		for(int i = 0; i < this.data.length; i++) 
			if(i < data.length) this.data[i] = data[i];
			else this.data[i] = 0x00;
	}
	
}
