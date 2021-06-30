import java.util.*;
import java.io.*;
import java.lang.Math;  
class RWA
{
	//This function is used to create new graph which dosen't contains
	//the edge contributing towards prinary path
	public static int[][] protectedPathGraph(Stack trace,int a[][])
	{
		int newGraph[][]=new int[a.length][a[0].length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				newGraph[i][j]=a[i][j];
			}
		}
		for(int m = trace.size()-1; m >0; m--){
			int v1=(int)trace.get(m),v2=(int)trace.get(m-1);
			newGraph[v1][v2]=0;
		}
		//System.out.println(Arrays.deepToString(a));
		//System.out.println(Arrays.deepToString(newGraph));
		return(newGraph);
	}
	//This function is used to find wavelength for the path trace which follows the continuity constraints
	public static  int findContinuousWavelength(Stack trace,int edgePointer[][],boolean wavelength[][]){
		int smallestIndex=-1;
		System.out.print("wavelength used before applying wavelength continuity constraint");
		for(int m = trace.size()-1; m >0; m--){
			int v1=(int)trace.get(m),v2=(int)trace.get(m-1);
			int count=0;
			for(int n=0;n<wavelength[0].length;n++){
				//System.out.print(wavelength[edgePointer[v1][v2]-1][n]+" ");
				if(wavelength[edgePointer[v1][v2]-1][n]==false)
				{
					if(count==0)
					{
						System.out.print(" "+v1+"->"+v2+"= "+n);
						count++;
					}
					if(smallestIndex<=n){
						smallestIndex=n;
						break;
					}
				}
			} 
					
		}
		System.out.println();
		return(smallestIndex);
	}

	//This function generates the path trace consisting all the vertex in the path
	public static  Stack pathTrace(int source,int destination,int dis[][]){
		Stack<Integer> stack = new Stack<Integer>();
				if(destination!=source)
					stack.push(destination);

		for(int k=destination; ;)
				{
					if(dis[k][1]!=source){
						stack.push(dis[k][1]);
						k=dis[k][1];
					}
					else{
						stack.push(dis[k][1]);
						break;
					}
				}
			return(stack);
	}

	//This function implements the routing logic and act as a carrier function
	public static  void RwaImplementation(int a[][],int edgePointer[][], boolean wavelength[][])
	{
		//Looping through All the Requests
		//The total no. of request is n(n-1)
		List<Integer> primaryPathWavelengths=new ArrayList<Integer>();
		List<Integer> protectedPathWavelengths=new ArrayList<Integer>();
		int wavelengthPrimaryCount=0;
		int wavelengthProtectedCount=0;
		for(int i=0;i<a.length;i++)
		{
			int source=i;
			int dis[][]=shortestPath(a,source);
			for(int j=0;j<a.length;j++)
			{
				if(i!=j){
					//System.out.println("Shortest Path Between "+source+"-"+j+" is= "+ dis[j][0]+" ");
					//System.out.print("   Previous node= "+dis[j][1]+" ");
					System.out.println("For the request made between the nodes "+source+"-"+j+" ");
					Stack<Integer> primaryPathTrace =pathTrace(source,j,dis);
					//System.out.println(trace);
					System.out.print("Primary Path : ");
					for(int m = primaryPathTrace.size()-1; m >=0; m--)
        			{
            			System.out.print(primaryPathTrace.get(m));
						if(m>0)
							System.out.print("->");
       				}
					   System.out.println();
					int primaryPathwavelengthNumber=findContinuousWavelength(primaryPathTrace,edgePointer,wavelength);
					
					if(!primaryPathWavelengths.contains(primaryPathwavelengthNumber)){
						primaryPathWavelengths.add(primaryPathwavelengthNumber);
					}
					//System.out.println(primaryPathWavelengths);
					for(int m = primaryPathTrace.size()-1; m >0; m--){
						int v1=(int)primaryPathTrace.get(m),v2=(int)primaryPathTrace.get(m-1);
						wavelength[edgePointer[v1][v2]-1][primaryPathwavelengthNumber]=true;
						wavelengthPrimaryCount++; 
					}
					
					//System.out.println(Arrays.deepToString(wavelength));
					
					
					System.out.print("wavelength used after applying continuity constraint= "+primaryPathwavelengthNumber+" ");
					System.out.println();

					int newGraph[][]=protectedPathGraph(primaryPathTrace,a);
					//System.out.println(Arrays.deepToString(newGraph));
					int secondShortestPath[][]=shortestPath(newGraph,source);
					//System.out.println(Arrays.deepToString(secondShortestPath));
					Stack<Integer> protectedPathTrace =pathTrace(source,j,secondShortestPath);
					//System.out.println(protectedPathTrace);
					System.out.print("Protected Path : ");
					for(int m = protectedPathTrace.size()-1; m >=0; m--)
        			{
            			System.out.print(protectedPathTrace.get(m));
						if(m>0)
							System.out.print("->");
       				}
					    System.out.println();
					int protectedPathWavelengthNumber=findContinuousWavelength(protectedPathTrace,edgePointer,wavelength);
					
					if(!protectedPathWavelengths.contains(protectedPathWavelengthNumber)){
						protectedPathWavelengths.add(protectedPathWavelengthNumber);
					}
					//System.out.println(protectedPathWavelengths);
					for(int m = protectedPathTrace.size()-1; m >0; m--){
						int v1=(int)protectedPathTrace.get(m),v2=(int)protectedPathTrace.get(m-1);
						wavelength[edgePointer[v1][v2]-1][protectedPathWavelengthNumber]=true;
						wavelengthProtectedCount++;
					}
					
					//System.out.println(Arrays.deepToString(wavelength));
					
					
					System.out.print("Wavelength used after applying continuity constraint= "+protectedPathWavelengthNumber+" ");
					System.out.println();
					System.out.println();
				}
				  
			}
			
		}
		int primaryPathWavelengthCount=primaryPathWavelengths.size();
		int protectedPathWavelengthCount=protectedPathWavelengths.size();
		//System.out.println("Total Number of Wavelength used for primaryPath= "+primaryPathWavelengthCount);
		System.out.println("Total Number of Wavelength used for primaryPath= "+wavelengthPrimaryCount);
		//System.out.println("Total Number of Wavelength used for protectedPath= "+protectedPathWavelengthCount);
		System.out.println("Total Number of Wavelength used for protectedPath= "+wavelengthProtectedCount);
		int totalWavelengthCount=0;
		for(int i=0;i<wavelength[0].length;i++)
		{
			for(int j=0;j<wavelength.length;j++)
			{
				if(wavelength[j][i]){
					totalWavelengthCount++;
					break;
				}
			}
		}
		int wavelengthTotal=wavelengthPrimaryCount+wavelengthProtectedCount;
		/*for(int i=0;i<wavelength.length;i++)
		{
			for(int j=0;j<wavelength[0].length;j++)
			{
				if(wavelength[i][j])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}*/
		System.out.println("Total number of wavelength used for establishing and protecting the connections between every pair of nodes= "+wavelengthTotal);
	}
	//This function is used to generate Random number
	public static double generateRandomNumber(int max, int min)
	{
		return(Math.random() * (max - min + 1) + min );
	}
	public static int[][] shortestPath(int a[][], int source)
	{
		int n=a.length;
		int d[][]=new int[n][2];
		boolean v[]=new boolean[n];
		d[source][0]=0;
		d[source][1]=source;
		for(int i=0;i<n;i++)
		{
			if(i!=source){
				d[i][0]=Integer.MAX_VALUE;
			}
		}
		for(int i=0;i<n-1;i++)
		{
			int minv=minvertex(d,v);
			v[minv]=true;
			for(int j=0;j<n;j++)
			{
				if(a[minv][j]!=0 && !v[j] && d[minv][0]!=Integer.MAX_VALUE){
					int newd=d[minv][0]+a[minv][j];
					if(newd<d[j][0]){
						d[j][0]=newd;
						d[j][1]=minv;
					}
				}
			}

		}
		//System.out.println(Arrays.deepToString(d));
		return(d);
	}
	//This function is used to finding min vertex of the graph
	public static int minvertex(int d[][],boolean v[]) {
		int min=-1;
		for(int i=0;i<d.length;i++)
		{
			if(!v[i]&&(min==-1||d[min][0]>d[i][0]))
			{
				min=i;
			}
		}
		return(min);
	}
	//This is the carrier function
	public static void main(String[] args) throws Exception
	{
		FileReader fr=new FileReader("F:\\Final Year Project\\NSFnetwork.txt");    
        BufferedReader br=new BufferedReader(fr);
		String str;
		int v=0,e=0;
		while((str=br.readLine())!=null){
			//System.out.println(str);
			String input[]=str.split(" ");
				v=Integer.parseInt(input[0]);
				e=Integer.parseInt(input[1]);
			break;
		} 
		int a[][]=new int[v][v];
		boolean wavelength[][]=new boolean[e][200];
		int edgePointer[][]=new int[v][v];
		int i=0;
		while((str=br.readLine())!=null){
			//System.out.println(str);
			String input[]=str.split(" ");
			int v1=Integer.parseInt(input[0]);
			int v2=Integer.parseInt(input[1]);
			int w=Integer.parseInt(input[2]);
			a[v1][v2]=w;
			a[v2][v1]=w;
			edgePointer[v1][v2]=i+1;
			edgePointer[v2][v1]=i+1;
			i++;
		} 
		br.close();
		//System.out.println(Arrays.deepToString(a));
		RwaImplementation(a,edgePointer,wavelength);
	}
}