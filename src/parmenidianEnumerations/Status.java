package parmenidianEnumerations;

public enum Status {
	
	UNDEFINED(-1),CREATION(0), DELETION(1), UPDATE(2);
	
    private int value;
    
    private Status(int value) {
        this.value = value;
    }
    
    public int getValue(){
    	return value;
    }
    
}