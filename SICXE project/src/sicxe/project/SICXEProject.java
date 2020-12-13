
package sicxe.project;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
 class ReadFile {
    String x[][];
    int count=0;
    int hexa=0;
    Instr b=new Instr();
    public void setC(int count) {
        this.count = count;
    }
    public void setX(String[][] sicxe) {
        this.x = sicxe;    
    }
    public void Locator(String x[][]){
        for (int i=0; i<count;i++){ 
            try{
            if (i==0){
            hexa = Integer.parseInt(x[0][3], 16);
                        x[i][0] = x[i+1][0] = Integer.toHexString(hexa);
            }
            if (i>=1&&!x[i][2].contains("+")&&!x[i][2].contains("RESB")){ 
               hexa+=Integer.parseInt(b.getOPTAB(x[i][2]));
               x[i+1][0]=Integer.toHexString(hexa);
            }
            if (i>1&&x[i][2].contains("+")){
              hexa+=4;  
              x[i+1][0]=Integer.toHexString(hexa);
            }
            if (i>1&&x[i][2].contains("BYTE")){
                if(x[i][3].charAt(0)=='C')
                hexa+=x[i][3].length()-3; 
                else //start by x
                    hexa+=(x[i][3].length()-3)/2;
                
                x[i+1][0]=Integer.toHexString(hexa);
            }  
            if (i>1&&x[i][2].contains("RESW")){
                int j=Integer.parseInt(x[i][3])*3;
                hexa+=j;
                x[i+1][0]=Integer.toHexString(hexa);
            } 
            if (i>1&&x[i][2].contains("RESB")){
                int j=Integer.parseInt(x[i][3]);
                hexa+=j;  
                x[i+1][0]=Integer.toHexString(hexa);
            }    
        }catch (Exception e){}
        }
    } 
    public void SimpleTable(String x[][]){
        
            for (int i=0; i<count;i++){  
                if (x[i][1]!=null&&!x[i][1].equalsIgnoreCase("**") && i>0) {
                    System.out.println("" + x[i][0] + "  <->  " + x[i][1]);
                }
         }
    } 
    static String six(String x){
    if (x.length()==1)
        return "00000"+x;
    if (x.length()==2)
        return "0000"+x;
    if (x.length()==3)
        return "000"+x;
    else if (x.length()==4)
        return "00"+x;
    else if (x.length()==5)
        return "0"+x;
   else
        return x;
    }
    static String bits(String x){
    if (x.length()==1)
        return "0000000"+x;
    else if (x.length()==4)
        return "0000"+x;
    else if (x.length()==5)
        return "000"+x;
   else if (x.length()==6)
        return "00"+x;
   else if (x.length()==7)
        return "0"+x;
   else
        return x;
    }
    static String noDisp(String x){
     if (x.length()==13)
         return "0000000"+x;
     if (x.length()==15)
         return "00000"+x;
     else
         return x;
    }
     static String longDigits(String x){
    if (x==null)
        return "000000000000";
     if (x.length()==1)
        return "00000000000"+x;
    if (x.length()==2)
        return "0000000000"+x;
    if (x.length()==6)
         return "000000"+x;
     if (x.length()==5)
         return "0000000"+x;
     if (x.length()==4)
         return "00000000"+x;
     if (x.length()==3)
         return "000000000"+x;
     if (x.length()==12)
         return "0000"+x;     
     if (x.length()>16)
         return ""+x.substring(20);
     else
         return x;
    } 
    public void ObjectCode(String x[][]){
        for(int i=0;i<x.length;i++){
       //format 3
            if (b.getOPTAB(x[i][2]).equals("3")){
                String oop=b.NumOP(x[i][2]);          
                String opcodePart=Integer.toBinaryString(Integer.parseInt(oop,16));
                opcodePart=bits(opcodePart);
            x[i][4]=opcodePart;         
            // RSUB
                        if (x[i][2].equals("RSUB")){
                        String myop=x[i][4];
                        char p[]=myop.toCharArray();
                        x[i][4]=String.valueOf(p); 
                        x[i][4]+=longDigits(longDigits(x[i][3]));
                        String []sp=b.split(x[i][4]);  
                        int len= x[i][4].length()/4;
                        x[i][4]="";
                        for (int c=0 ; c<len;c++)
                        x[i][4]+=b.ConvertTo(sp[c]); 
                    }}
              //format 4
              String op;
            if (x[i][2].contains("+")){
                op=b.NumOP(x[i][2].substring(1));
                String opcodePart=Integer.toBinaryString(Integer.parseInt(op,16));
                opcodePart=bits(opcodePart);
                x[i][4]=opcodePart;}
        //Handing n & i
         try{   
         if (x[i][3].contains("#")){
                String myop=x[i][4];
                char p[]=myop.toCharArray();
                p[7]='1';
                x[i][4]=String.valueOf(p);
            }
            if (x[i][3].contains("@")){
                String myop=x[i][4];
                char p[]=myop.toCharArray();
                p[6]='1';
                x[i][4]=String.valueOf(p);
            }
            // format 4 &! immdate
            if (x[i][2].contains("+")&&!x[i][3].contains("#"))
            {
                String myop=x[i][4];
                char p[]=myop.toCharArray();
                p[6]='1';
                p[7]='1';
                x[i][4]=String.valueOf(p);
            }}
        catch(Exception y){} 
    }}
    //Indexed Registers
    public void Registers(String x[][]){
        for(int i=0;i<x.length;i++){
        if (!b.getOPTAB(x[i][2]).equals("2")){
        try{
            if (x[i][3].contains(",X")){
            x[i][4]+="1";       
            }
            else if (!x[i][2].contains("START")&&!x[i][2].contains("BASE")&&!x[i][2].contains("BYTE")&&!x[i][2].contains("RESW")&&!x[i][2].contains("RESB")&&!x[i][2].contains("END")) 
                x[i][4]+="0";
           
            if(x[i][2].contains("+")){
                String Disp=TargetAddress(x[i][3],x);
                String opcodePart=Integer.toBinaryString(Integer.parseInt(Disp,16));
                x[i][4]+="001"+noDisp(opcodePart);
                  x[i][4]=b.ConvertTo(x[i][4]); 
            } 
            }catch(Exception y){ }}
        try {
            if (x[i][2].contains("+")&&x[i][3].contains("#")){
                String opcodePart=Integer.toBinaryString(Integer.parseInt(x[i][3].substring(1)));
                x[i][4]+="001"+noDisp(opcodePart);
                //convert final object code
                x[i][4]=b.ConvertTo(x[i][4]);
            }
        } catch(Exception y){}
        } 
    }
    public void FormatThree(String x[][]) {
        String base= "33";
        
            for (int i= 0 ; i<x.length;i++){
                int disp=0;
                try {
                if (b.getOPTAB(x[i][2])=="3"){
        
                    if (!x[i][3].contains("#") && !x[i][3].contains("@")){
            
                        String myop=x[i][4];
                        char p[]=myop.toCharArray();
                        p[6]='1';
                        p[7]='1';
                        x[i][4]=String.valueOf(p);  
                    }
                 
                       if (x[i][3].contains("#") ||x[i][3].contains("@")){
                       
                        if (TargetAddress(x[i][3].substring(1),x)!=""){
                            disp= Integer.parseInt(TargetAddress(x[i][3].substring(1),x) ,16)-Integer.parseInt(x[i+1][0],16);
                            if (disp<=2047&& disp >=-2047){
                                x[i][4]+="010"+longDigits(Integer.toBinaryString(disp));
                              String []sp=b.split(x[i][4]);  
                              int len= x[i][4].length()/4;
                              x[i][4]="";
                              for (int c=0 ; c<len;c++)
                              x[i][4]+=b.ConvertTo(sp[c]); 
                                 }}
                        else{
                            x[i][4]+="000"+longDigits(Integer.toBinaryString(Integer.parseInt(x[i][3].substring(1))));
                            String []sp=b.split(x[i][4]);
                            int len= x[i][4].length()/4;
                            x[i][4]="";
                            for (int c=0 ; c<len;c++)
                                x[i][4]+=b.ConvertTo(sp[c]);          
                            } 
                    }  
                    if (!x[i][3].contains("#")){

                    disp= Integer.parseInt(TargetAddress(x[i][3],x) ,16)-Integer.parseInt(x[i+1][0],16);
    
                    // PC Relative
                        if (disp<2047&& disp >-2048){
                                
                                x[i][4]+="010"+longDigits(Integer.toBinaryString(disp));

                                String []sp=b.split(x[i][4]);  
                                int len= x[i][4].length()/4;
                                x[i][4]="";
                                for (int c=0 ; c<len;c++)
                                x[i][4]+=b.ConvertTo(sp[c]); 
                        }
                        //base Relative
                        else if (disp<=4095){ 

                            disp= Integer.parseInt(TargetAddress(x[i][3],x) ,16)-Integer.parseInt(base,16);
                            x[i][4]+="100"+longDigits(Integer.toBinaryString(disp));
                            String []sp=b.split(x[i][4]);  
                            int len= x[i][4].length()/4;
                            x[i][4]="";
                            for (int c=0 ; c<len;c++)
                            x[i][4]+=b.ConvertTo(sp[c]); 
                        }}}
        }catch (Exception o){}
        try {
            if (x[i][3].contains(",X")){
                    disp= Integer.parseInt(TargetAddress(x[i][3].replaceAll(",X",""),x) ,16)-Integer.parseInt(x[i+1][0],16);
                    // PC Relative
                        if (disp<2047&& disp >-2048){
                                
                                x[i][4]+="010"+longDigits(Integer.toBinaryString(disp));
                                String []sp=b.split(x[i][4]);  
                                int len= x[i][4].length()/4;
                                x[i][4]="";
                                for (int c=0 ; c<len;c++)
                                x[i][4]+=b.ConvertTo(sp[c]); 
                        }
                        //base Relative
                        else if (disp<=4095){
                            disp= Integer.parseInt(TargetAddress(x[i][3].replaceAll(",X",""),x) ,16)-Integer.parseInt(base,16);
                            x[i][4]+="100"+longDigits(Integer.toBinaryString(disp));
                            String []sp=b.split(x[i][4]);  
                            int len= x[i][4].length()/4;
                            x[i][4]="";
                            for (int c=0 ; c<len;c++)
                            x[i][4]+=b.ConvertTo(sp[c]); 
                        }
            }}catch (Exception o){}
        if (x[i][2].equals("BYTE")){
          if (x[i][3].charAt(0)=='X')
              x[i][4]=x[i][3].replaceAll("'","").replaceAll("X", "");
          else if (x[i][3].charAt(0)=='C'){
              String digits=x[i][3].replaceAll("'","").replaceAll("C", "");
              x[i][4]="";
                        char p[]=digits.toCharArray();
                       for (int u=0; u<p.length;u++){
                           x[i][4]+=String.valueOf((int)p[u]);}}}
        if (x[i][2].equals("WORD"))
        {
           x[i][4]=six(Integer.toHexString(Integer.parseInt(x[i][3],16))); 
        }
        if (x[i][2].contains("START") || x[i][2].contains("BASE") ||x[i][2].contains("RESW")||x[i][2].contains("RESB")||x[i][2].contains("END")) 
             x[i][4]="no opcode";
    }   
    }
     String TargetAddress(String inst, String x[][]){
             //format 4
       String u="";
             for (int i=0 ; i<x.length; i++){
                 if (inst.equals(x[i][1])){
                      u =x[i][0];}} 
            return u;        
    }
     void getX() {
         System.out.println("_____________________________________________SICXE CODE____________________________________________ ");
        Locator(x);
        ObjectCode(x);
        Registers(x);
        FormatThree(x);
        // printing data of table
        for (int e = 0; e < count; e++) {
            for (int s = 0; s < 5; s++) {
                System.out.print("" + x[e][s] + "\t");}
            System.out.println("\n");
        }
       System.out.println("_____________________________________________Symbol Table____________________________________________ ");
        SimpleTable(x); }
}
class HTErecord{
   String[][] sicxe;
    int count;
    int f=0;

    public HTErecord(String[][] sicxe,int count){
        this.sicxe=sicxe;
        this.count=count;}
    
    public void getHTE(){ 
        int j;
        
        System.out.println("________________________________________HTE record______________________________________");
        System.out.println("H^"+sicxe[0][0]+"^"+sicxe[0][1]+"^"+"1077");
         Instr o =new Instr();
       String len=null;
               len=o.getlen(sicxe[0][0], sicxe[9][0]);
        System.out.print("T^"+sicxe[0][0]+"^"+len+"^");
       
    for( f=0 ;f<10;f++){
        if(sicxe[f][2].equalsIgnoreCase("RESW")||sicxe[f][2].equalsIgnoreCase("RESB")){
         System.out.println(" ");
         System.out.print("T^"+sicxe[1][0]+"^"+len+"^");
        }
        else if(sicxe[f][4].equalsIgnoreCase("no opcode")){
        continue;
        }
        else{
            System.out.print(sicxe[f][4]);
    }
    }
     len=o.getlen(sicxe[10][0], sicxe[19][0]);
    System.out.println(" ");
    System.out.print("T^"+sicxe[10][0]+"^"+len+"^");
    
    for( f=10 ;f<20;f++){
        if(sicxe[f][2].equalsIgnoreCase("RESW")||sicxe[f][2].equalsIgnoreCase("RESB")){
         continue;
        }
        else if(sicxe[f][4].equalsIgnoreCase("no opcode")){
        continue;
        }
        else{
            System.out.print(sicxe[f][4]);
    }}
      len=o.getlen(sicxe[20][0], sicxe[29][0]);
     System.out.println(" ");
    System.out.print("T^"+sicxe[20][0]+"^"+len+"^");
    for( f=20 ;f<30;f++){
        if(sicxe[f][2].equalsIgnoreCase("RESW")||sicxe[f][2].equalsIgnoreCase("RESB")){
         continue;
       }
//        else if(sicxe[f][4].equalsIgnoreCase("no opcode")){
//        continue;
//        }
//        else if(sicxe[f][4].equalsIgnoreCase("null")){
//            continue;
//        }
        else{
            System.out.print(sicxe[f][4]);
    }}
    len=o.getlen(sicxe[30][0], sicxe[39][0]);
    System.out.println(" ");
    System.out.print("T^"+sicxe[30][0]+"^"+len+"^");
    for( f=30 ;f<40;f++){
        if(sicxe[f][2].equalsIgnoreCase("RESW")||sicxe[f][2].equalsIgnoreCase("RESB")){
         continue;
       }
//        else if(sicxe[f][4].equalsIgnoreCase("null")){
//            continue;
//        }
         else{
            System.out.print(sicxe[f][4]);
    }}
     //len=o.getlen(sicxe[30][0], sicxe[39][0]);
    System.out.println(" ");
    System.out.print("T^"+sicxe[40][0]+"^"+len+"^");
    for( f=40 ;f<45;f++){
        if(sicxe[f][2].equalsIgnoreCase("RESW")||sicxe[f][2].equalsIgnoreCase("RESB")){
         continue;
       }
        
//        else if(sicxe[f][4].equalsIgnoreCase("null")){
//            continue;
//        }
         else{
            System.out.print(sicxe[f][4]);
    }}
    System.out.println(" ");
    System.out.println("E^"+sicxe[0][0]);
    }

}
 class Instr {
 private static final String[][] OPTAB = new String[68][3];
    public static void initialize () {
        OPTAB[0] = new String[] {"FIX", "1", "C4"};
        OPTAB[1] = new String[] {"FLOAT", "1", "C0"};
        OPTAB[2] = new String[] {"HIO", "1", "F4"};
        OPTAB[3] = new String[] {"NORM", "1", "C8"};
        OPTAB[4] = new String[] {"SIO", "1", "F0"};
        OPTAB[5] = new String[] {"TIO", "1", "F8"};
        OPTAB[6] = new String[] {"ADDR", "2", "90"};
        OPTAB[7] = new String[] {"CLEAR", "2", "B4"};
        OPTAB[8] = new String[] {"COMPR", "2", "A0"};
        OPTAB[9] = new  String[] {"DIVR", "2", "9C"};
        OPTAB[10] = new String[] {"MULR", "2", "98"};
        OPTAB[11] = new String[] {"RMO", "2", "AC"};
        OPTAB[12] = new String[] {"SHIFTL", "2", "A4"};
        OPTAB[13] = new String[] {"SHIFTR", "2", "A8"};
        OPTAB[14] = new String[] {"SUBR", "2", "94"};
        OPTAB[15] = new String[] {"SVC", "2", "B0"};
        OPTAB[16] = new String[] {"TIXR", "2", "B8"};
        OPTAB[17] = new String[] {"ADD", "3", "18"};
        OPTAB[18] = new String[] {"ADDF", "3", "58"};
        OPTAB[19] = new String[] {"AND", "3", "40"};
        OPTAB[20] = new String[] {"COMP", "3", "28"};
        OPTAB[21] = new String[] {"COMPF", "3", "88"};
        OPTAB[22] = new String[] {"DIV", "3", "24"};
        OPTAB[23] = new String[] {"DIVF", "3", "64"};
        OPTAB[24] = new String[] {"J", "3", "3C"};
        OPTAB[25] = new String[] {"JEQ", "3", "30"};
        OPTAB[26] = new String[] {"JGT", "3", "34"};
        OPTAB[27] = new String[] {"JLT", "3", "38"};
        OPTAB[28] = new String[] {"JSUB", "3", "48"};
        OPTAB[29] = new String[] {"LDA", "3", "00"};
        OPTAB[30] = new String[] {"LDB", "3", "68"};
        OPTAB[31] = new String[] {"LDCH", "3", "50"};
        OPTAB[32] = new String[] {"LDF", "3", "70"};
        OPTAB[33] = new String[] {"LDL", "3", "08"};
        OPTAB[34] = new String[] {"LDS", "3", "6C"};
        OPTAB[35] = new String[] {"LDT", "3", "74"};
        OPTAB[36] = new String[] {"LDX", "3", "04"};
        OPTAB[37] = new String[] {"LPS", "3", "D0"};
        OPTAB[38] = new String[] {"MUL", "3", "20"};
        OPTAB[39] = new String[] {"MULF", "3", "60"};
        OPTAB[40] = new String[] {"OR", "3", "44"};
        OPTAB[41] = new String[] {"RD", "3", "D8"};
        OPTAB[42] = new String[] {"RSUB", "3", "4C"};
        OPTAB[43] = new String[] {"SSK", "3", "EC"};
        OPTAB[44] = new String[] {"STA", "3", "0C"};
        OPTAB[45] = new String[] {"STB", "3", "78"};
        OPTAB[46] = new String[] {"STCH", "3", "54"};
        OPTAB[47] = new String[] {"STF", "3", "80"};
        OPTAB[48] = new String[] {"STI", "3", "D4"};
        OPTAB[49] = new String[] {"STL", "3", "14"};
        OPTAB[50] = new String[] {"STS", "3", "7C"};
        OPTAB[51] = new String[] {"STSW", "3", "E8"};
        OPTAB[52] = new String[] {"STT", "3", "84"};
        OPTAB[53] = new String[] {"STX", "3", "10"};
        OPTAB[54] = new String[] {"SUB", "3", "1C"};
        OPTAB[55] = new String[] {"SUBF","3", "5C"};
        OPTAB[56] = new String[] {"TD", "3", "E0"};
        OPTAB[57] = new String[] {"TIX", "3", "2C"};
        OPTAB[58] = new String[] {"WD", "3", "DC"};
        OPTAB[59] = new String[] {"SW", "0", "9"};
        OPTAB[60] = new String[] {"A", "", "0"};
        OPTAB[61] = new String[] {"L", "", "2"};
        OPTAB[62] = new String[] {"F", "", "6"};
        OPTAB[63] = new String[] {"S", "", "4"};
        OPTAB[64] = new String[] {"PC", "", "8"};
        OPTAB[65] = new String[] {"T", "", "5"};
        OPTAB[66] = new String[] {"B", "", "3"};
        OPTAB[67] = new String[] {"X", "", "1"};
    }
    public static String[][] getOPTAB() {
        return OPTAB;
    }
    public String getOPTAB(String s) {
            initialize();    
            int u=0;
            try {while (!(s.equals(OPTAB[u][0]))){          
                   u++;     
               }
            if (s.equals(OPTAB[u][0]))
                return OPTAB[u][1];
            }catch (Exception e){}
            return "0";}
     public String NumOP(String s) {
            initialize();    
            int u=0;
            try {while (!(s.equals(OPTAB[u][0]))){          
                   u++;     
               }
            if (s.equals(OPTAB[u][0]))
                return OPTAB[u][2];
            }catch (Exception e){ 
                   }
            return "0";
    }
    public String ConvertTo (String x){
        String hexa=x;
                 int decimal = Integer.parseInt(hexa,2);
                 String hexStr = Integer.toString(decimal,16);
                 x= hexStr; 
                 return x;
    }
    public String[] split(String x){
        String o []=x.split("(?<=\\G....)");       
        return o; 
}
 public String getlen(String s1,String s2){
    String len=null;
    
    len= Integer.toHexString(Integer.parseInt(s2,16)-Integer.parseInt(s1,16));
    
    
    return len;
}
 }
public class SICXEProject { 
    static int i=0;
    public static void main(String[] args) throws Exception{
        Scanner s1 = null;
        Scanner input=null;
        int line=0;
        int count=0;
        try {
            File file = new File("inSICXE.txt");
            s1 = new Scanner(file);
            input=new Scanner(file);     
            while (input.hasNextLine()){
                input.nextLine();
                count++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("error File is Not Found");
        }
        String sicxe[][] =new String [count][5];
        try {
        while (s1.hasNextLine()) {    
                String m=s1.nextLine();
                String a[]=m.trim().split("\\s+");
                line=a.length;
        if (line ==3){
         sicxe[i][1]=a[0];   
         sicxe[i][2]=a[1];
         sicxe[i][3]=a[2];
        }
        else if (line==2)
        {
         sicxe[i][1]="**";   
         sicxe[i][2]=a[0];
         sicxe[i][3]=a[1]; 
        }  
        else if (line ==1){
             sicxe[i][2]=a[0];
        }  
        i++;
        }}
        catch (Exception e){System.out.println("Error Exception ");}
        ReadFile object= new ReadFile();
        object.setX(sicxe);
        object.setC(count);
        object.getX();
        
        HTErecord obj1= new HTErecord(sicxe,count);
        obj1.getHTE();

 }}
    
    

