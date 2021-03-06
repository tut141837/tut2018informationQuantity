package s4.B183325; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity,
}
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) {
	myFrequencer = new Frequencer();
	mySpace = space; myFrequencer.setSpace(space);
    }

    public double estimation(){
		double [] myIq = null;
    if(myTarget.length != 0) {
      myIq = new double[myTarget.length];
    }
		double iq1, iq2;
		boolean [] partition = new boolean[myTarget.length+1];
		int np;
		np = 1<<(myTarget.length-1);
		// System.out.println("np="+np+" length="+myTarget.length);
		double value = Double.MAX_VALUE; // value = mininimum of each "value1".
		/*mycode
		myFrequencer.setTarget(subBytes(myTarget, 0, 1));
		myIq[0] = iq(myFrequencer.frequency());
		for(int i = 1; i < myTarget.length; i++){
			myFrequencer.setTarget(subBytes(myTarget, i, i+1));
			iq1 = myIq[i-1] + iq(myFrequencer.frequency());
			myFrequencer.setTarget(subBytes(myTarget, 0, i+1));
			iq2 = iq(myFrequencer.frequency());
			if(iq1 < iq2){
				myIq[i] = iq1;
			}else{
				myIq[i] = iq2;
			}
		}
		for(int i = 0; i < myTarget.length; i++){
			System.out.println(myIq[i]);
		}
		return myIq[myTarget.length-1];
		mycode */

    /*mycode2*/
    myFrequencer.setTarget(subBytes(myTarget, 0, 1));
    myIq[0] = iq(myFrequencer.frequency());
    for(int i = 1; i < myTarget.length; i++){ // Roop of calculate myIq[i].
      myFrequencer.setTarget(subBytes(myTarget, 0, i+1));
      double min = iq(myFrequencer.frequency()); // Set min the infomation quantity of i characters.
      for(int j = 0; j < i; j++){ // Roop of calculate minimum value.
        iq1 = myIq[j];
        myFrequencer.setTarget(subBytes(myTarget, j+1, i+1));
        iq2 = iq(myFrequencer.frequency());
        if(iq1 + iq2 < min && iq1 + iq2 != Double.NEGATIVE_INFINITY){
          min = iq1 + iq2; // Set min the smaller one.
        }
      }
      myIq[i] = min;
    }
    return myIq[myTarget.length-1];
    /*mycode2*/

		/*umecode
		for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
			// binary representation of p forms partition.
			// for partition {"ab" "cde" "fg"}
			// a b c d e f g   : myTarget
			// T F T F F T F T : partition:
			partition[0] = true; // I know that this is not needed, but..
			for(int i=0; i<myTarget.length -1;i++) {
				//System.out.println(0 !=((1<<i) & p));
				partition[i+1] = (0 !=((1<<i) & p));
			}
			partition[myTarget.length] = true;

			// Compute Information Quantity for the partition, in "value1"
			// value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
			double value1 = (double) 0.0;
			int end = 0;;
			int start = end;
			while(start<myTarget.length) {
				// System.out.write(myTarget[end]);
				end++;;
			while(partition[end] == false) {
				// System.out.write(myTarget[end]);
				end++;
			}
			// System.out.print("("+start+","+end+")");
			myFrequencer.setTarget(subBytes(myTarget, start, end));
			value1 = value1 + iq(myFrequencer.frequency());
			start = end;
			}
			// System.out.println(" "+ value1);

			// Get the minimal value in "value"
			if(value1 < value) value = value1;
		}
		return value;
		umecode*/
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	//value = myObject.estimation();
	//System.out.println(">3210321001230123 "+value);
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("1".getBytes());
	value = myObject.estimation();
	System.out.println(">1 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
	myObject.setTarget("000".getBytes());
	value = myObject.estimation();
	System.out.println(">000 "+value);
	myObject.setTarget("101".getBytes());
	value = myObject.estimation();
	System.out.println(">101 "+value);
	myObject.setTarget("010".getBytes());
	value = myObject.estimation();
	System.out.println(">010 "+value);
	myObject.setTarget("012345".getBytes());
	value = myObject.estimation();
	System.out.println(">012345 "+value);
	myObject.setTarget("011".getBytes());
	value = myObject.estimation();
	System.out.println(">011 "+value);
	myObject.setTarget("0110".getBytes());
	value = myObject.estimation();
	System.out.println(">0110 "+value);
	myObject.setTarget("000000".getBytes());
	value = myObject.estimation();
	System.out.println(">000000 "+value);
    }
}
