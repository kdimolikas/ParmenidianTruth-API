

import java.util.EnumSet;

import junit.framework.TestCase;
import parmenidianEnumerations.Metric_Enums;

/**
 * Testing {@link parmenidianEnumerations.Metric_Enums}. 
 * @author MZ - IK
 * @since 2017-05-23
 *
 */

public class Metric_EnumsTester extends TestCase {
	
	public static void main(String[] args){
		
	EnumSet<Metric_Enums> set1 = EnumSet.of(Metric_Enums.VERTEX_DEGREE,Metric_Enums.VERTEX_OUT_DEGREE);
	EnumSet<Metric_Enums> set2 = EnumSet.complementOf(set1);
	
	System.out.println(set1);
	System.out.print(set2);

		
		
	}
	

}
