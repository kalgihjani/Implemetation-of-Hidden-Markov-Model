/**
 * The following code gives the most likely state sequence for given output sequence and
 * for given model. It uses the Viterbi algorithm for this.
 * First It reads the training file which is model file.
 * It constructs pi vector, transition matrix and state-output probabilities matrix from this.
 * The code has comments when it creates each of this.
 * After constructing all matrixes, it reads output sequences.
 * For each output sequence it calculates the probabilities using Viterbi algorithm
 * At this time it stores previous maximum probable state in backtrack matrix.
 * Finally, using the back track matrix, it print outs the the most probable output sequence.
 */
/**
 * Input: model file to construct HMM model and test file containing output sequences
 * Output: Most likely state sequence for each output sequence given in the test file
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Hmm {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		     	if(args.length != 2)    //System should accept only 2 arguments:
		        {                       //training file name, test file name
		            System.out.println("You must provide exactly 2 arguments");
		            System.exit(1);
		        }
		        
		     	String train_file=args[0];       //taking command line arguments into variables
		        String test_file=args[1];
		        
		        FileReader fr=new FileReader(new File(train_file));     //file readers for training and testing files
		        FileReader fr_test=new FileReader(new File(test_file));

		        BufferedReader br=new BufferedReader(fr);
		        BufferedReader br_test=new BufferedReader(fr_test);
		        //reading the training file
		        int states=Integer.parseInt(br.readLine()); //first line gives how many states
		        String[] pi_temp=br.readLine().split(" ");	//storing initial probabilities
		        float[] pi=new float[states];
		        int i=0;
		        for(String s : pi_temp)	//converting String array to float array
		        {
		        	pi[i++]=Float.parseFloat(s);
		        }
		        String[] trans_temp=br.readLine().split(" "); //reading transition probabilities
		      //storing transition probabilities in trans[states][states] size array
		        float[][] trans=new float[states][states];
		        int k=0;
		        for(i=0;i<states;i++)	
		        {						
		        	for(int j=0;j<states;j++)
		        	{
		        		trans[i][j]=Float.parseFloat(trans_temp[k++]);
		        	}
		        }
		        int no_of_op=Integer.parseInt(br.readLine());
		        String opchar[]=br.readLine().split(" "); //storing output characters
		        
		        /**reading output probabilities of a character in each state and storing them
		         into float type op_prob[states][no_of_outputs] array **/
		        String[] op_prob_temp=br.readLine().split(" "); 
		        float[][] op_prob=new float[states][no_of_op];
		        
		        k=0;
		        for(i=0;i<states;i++)
		        {
		        	for(int j=0;j<no_of_op;j++)
		        	{
		        		op_prob[i][j]=Float.parseFloat(op_prob_temp[k++]);
		        	}
		        }
		       //un-comment the following for loop to print state to state transition matrix 
		       /** for(i=0;i<states;i++)
		        {
		        	for(int j=0;j<states;j++)
		        	{
		        		System.out.print(trans[i][j]);
		        	}
		        	System.out.println();
		        } **/
		        //un-comment the following for loop to print output character probability in each state
		        /**for(i=0;i<states;i++)
		        {
		        	for(int j=0;j<no_of_op;j++)
		        	{
		        		System.out.print(op_prob[i][j]);
		        	}
		        	System.out.println();
		        }**/
		   String s1=null;
		   
		   //while loop for each output sequence from test file
		   while((s1=br_test.readLine())!=null)	
		   {
		       String print_last=s1;
			   
		       String op_seq=s1.replaceAll("\\s",""); //removing spaces from output sequence
		       
		        int output=0;
		        /** the following for loop matches the output sequence character to the 
		         * elements of opchar[] to find out which is the current output character from the matrix
		         * so that from op_prob matrix, we can choose the correct column
		         */
		        for(i=0;i<opchar.length;i++)
		        {
		        	if(opchar[i].charAt(0)==op_seq.charAt(0))
		        	{
		        		output=i;
		        		break;
		        	}
		        }
		        
		        /**backtrack matrix will store the state number from which the current state
		        *has been reached. In short it will store the state number which will give 
		        *maximum probability for the current state
		        */
		        int[][] backtrack=new int[states][op_seq.length()];
		        float[] temp_holder=new float[states]; //stores current states probability
		        float[] cur_state_prob=new float[states]; //store previous states probability
		        int max=0;
		        float temp_prob=0;
		        /**the following for loop is for the first column where transition probabilities doesnt exist
		        *the first column also does not require backtracking
		        *so in backtracking matrix, I have simply stored state with maximum probability
		        *this can be useful when output sequence has only one character
		        */
		        for(i=0;i<states;i++)
		        {
		        	cur_state_prob[i]=pi[i]*op_prob[i][output];
		        	if(cur_state_prob[i]>=temp_prob)
		        	{
		        		temp_prob=cur_state_prob[i];
		        		max=i;
		        	}
		        }
		        
		        for(i=0;i<states;i++)
		        {
		        	backtrack[i][0]=max;
		        }
		        //for the columns 2 to end
		        int track=0;
		        /**
		         * for each output char, first we will find its position in opchar matrix
		         * then for each state, we will find probabilities using 
		         * previous probabilities*transition probabilities*output probabilities
		         * But, we will only store the state number which gives max in backtrack matrix
		         */
		        for(i=1;i<op_seq.length();i++)
		        {
		        	output=0;
		        	for(int w=0;w<opchar.length;w++)
			        {
			        	if(opchar[w].charAt(0)==op_seq.charAt(i))
			        	{
			        		output=w;
			        		break;
			        	}
			        }
		        	
		        	
		        	
		        	for(int j=0;j<states;j++)
		        	{
		        		//int max1=0;
		        		float local_max=0;
		        		for(k=0;k<states;k++)
		        		{
		        			float temp=cur_state_prob[k]*trans[k][j]*op_prob[j][output];
		        			if(temp>=local_max)
		        			{
		        				track=k;
		        				local_max=temp;
		        			}
		        		}
		        		temp_holder[j]=local_max;
		        		backtrack[j][i]=track;
		        	}
		        	/**
		        	 * after each column completed we will update current probabilities
		        	 */
		        	cur_state_prob=temp_holder.clone();
		        }
		        //un-comment the following for loop to print the final probabilities
		        /**for(float forprint:cur_state_prob)
		        {
		        	System.out.println(forprint);
		        }**/
		        
		        float final_max=0;
		        int final_state=0;
		        //finding the last state which has max probability
		        //so that we can start backtracking from that state
		        for(i=0;i<states;i++)
		        {
		        	if(cur_state_prob[i]>=final_max)
		        	{
		        		final_max=cur_state_prob[i];
		        		final_state=i;
		        	}
		        }
		        int cur_state=final_state;
		        //String s2=String.valueOf(cur_state);
		        StringBuilder print_op=new StringBuilder();
		        /** the following for loop starts from end of the output sequence and 
		         * runs till starting. Each time it adds the previous most probable state
		         * to the String print_op at the beginning of that string
		         */
		        for(i=op_seq.length()-1;i>=0;i--)
		        {
		        	if(i==0)
		        	{
		        		print_op.insert(0, "S"+String.valueOf(cur_state+1));
		        	}
		        	else
		        	{
		        	print_op.insert(0, "->S"+String.valueOf(cur_state+1));
		        	//System.out.println(cur_state);
		        	cur_state=backtrack[cur_state][i];
		        	}
		        }
		        
		       //printing the final output
		       System.out.println("For the sequence: "+print_last);
		       System.out.println("Most likely state sequence:");
		       System.out.println(print_op);
		        
		  }        
	
	br.close();
	br_test.close();
	
	}
}

