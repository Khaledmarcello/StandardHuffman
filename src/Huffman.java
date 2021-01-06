import javax.swing.*;
import java.io.*;
import java.security.Provider;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Huffman {

        Vector<Probability> prob = new Vector <Probability>() ;
        public String ReadFromFile (String Path) throws IOException{
            String content = null;
            File file = new File(Path);
            FileReader reader = null;
            try {
                reader = new FileReader(file);
                char[] chars = new char[(int) file.length()];
                reader.read(chars);
                content = new String(chars);
                reader.close();
            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return content;
        }
        public void WrtieToFile(String content,String name){
            BufferedWriter writer = null;
            String fileName = name+".txt" ;
            File logFile = new File(fileName);
            try{
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(content);
        } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            writer.close();
        } catch (Exception e) {}

            }
        }
        public void Sort(Vector<Probability>vec){
            for (int i=0 ; i<vec.size() ; i++)
            {
                for (int j=0 ; j<vec.size() ; j++)
                {
                    if (vec.elementAt(i).count < vec.elementAt(j).count)
                    {
                        Probability temp = vec.elementAt(i);
                        vec.setElementAt(vec.elementAt(j), i);
                        vec.setElementAt(temp, j);
                    }
                }
            }
        }
        public Probability SearchProb(Vector<Probability>vec, String content){
            for (int i=0 ; i<vec.size() ; i++)
            {
                if (vec.elementAt(i).text.equals(content))
                    return vec.get(i) ;
            }
            return null ;
        }

        public void CalcProbability(String content){
        for (int i=0 ; i<content.length() ; i++)
        {
            boolean found = false ;
            for (int j=0 ; j<prob.size() ; j++)
            {
                if (prob.get(j).text.charAt(0) == content.charAt(i))
                {
                    found = true ;
                    prob.elementAt(j).count ++ ;
                    break;
                }
            }
            if (!found)
            {
                String c = "" + content.charAt(i) ;
                int count = 1 ;
                String code = "" ;
                Probability p = new Probability(c, count, code) ;
                prob.add(p) ;
            }
        }
        }
        public Vector<Probability>SetCodes(){

            Vector <Probability> vec = prob ;
            Vector <Probability> result = new Vector<Probability>();
            Stack<String> stk = new Stack <String>() ;
            int num = vec.size() ;

            while (num > 2)
            {
                int sum = 0 ;
                String temp = "" ;
                for (int i=0 ; i<vec.size() ; i++)
                {
                    for (int j=0 ; j<vec.size() ; j++)
                    {
                        if (vec.elementAt(i).count > vec.elementAt(j).count)
                        {
                            Probability temp1 = vec.elementAt(i);
                            vec.setElementAt(vec.elementAt(j), i);
                            vec.setElementAt(temp1, j);
                        }
                    }
                }

                for (int i=0 ; i<2 ; i++)
                {
                    stk.add(vec.elementAt(vec.size()-1).text) ;
                    temp+=vec.elementAt(vec.size()-1).text;
                    sum += vec.elementAt(vec.size()-1).count ;
                    vec.removeElementAt(vec.size()-1);
                }

                // Create Probability
                Probability p = new Probability(temp , sum , "") ;
                vec.addElement(p);
                num -- ;

            }

            stk.push(vec.elementAt(1).text) ;
            stk.push(vec.elementAt(0).text) ;

            boolean firstTime = true;
            while (!stk.isEmpty())
            {
                Probability temp0 , temp1 ;

                if (firstTime)
                {
                    firstTime = false ;
                    temp0 = new Probability(stk.pop() , 0 , "0");
                    temp1 = new Probability(stk.pop() , 0 , "1");
                }
                else
                {
                    temp0 = new Probability(stk.pop() , 0 , "") ;
                    temp1 = new Probability(stk.pop() , 0 , "") ;

                    String text = temp1.text + temp0.text ;
                    Probability searchRes = SearchProb (result , text) ;

                    // Update Codes
                    temp0.code = searchRes.code + "0" ;
                    temp1.code = searchRes.code + "1" ;
                    result.remove(searchRes) ;

                }
                result.addElement(temp0);
                result.addElement(temp1);
            }
            return result ;
        }
        public void Compress(String path ,String name){
            String text = new String();
            String compressed = new String ("") ;
            try {
                text = ReadFromFile(path);
            } catch (IOException e) {

            }

            CalcProbability(text);
            Vector <Probability> result = SetCodes();

           //count of all unique chars
            compressed += result.size() + " " ;

            //code of every unique char
            for (int i=0 ; i<result.size() ; i++)
            {
                compressed += result.get(i).text + " " + result.get(i).code + " " ;
            }
            for (int i=0 ; i<text.length() ; i++)
            {
                String code = SearchProb(result, text.charAt(i)+"").code ;
                compressed += code + "";
            }
            WrtieToFile(compressed, name);
        }

        public void Decompress (String path , String name) {
            String text = new String();
            String decompressed = new String ("") ;
            Vector<Probability> dictionary = new Vector<Probability>() ;

            try {
                text = ReadFromFile(path);
            } catch (IOException e) {

            }

            Scanner input = new Scanner(text) ;
            int numChars = input.nextInt() ;

            for (int i=0 ; i<numChars ; i++)
            {
                String c = input.next() ;
                String code = input.next() ;
                Probability p = new Probability( c+"" , 0 , code ) ;
                dictionary.addElement(p);
            }

            String compressed = input.next() ;
            String curr = "" ;
            boolean found;
            char c = 0;

            for (int i=0 ; i<compressed.length() ; i++)
            {
                found = false ;
                curr += compressed.charAt(i) ;
                int j ;
                for (j=0 ; j<dictionary.size() ; j++)
                {
                    if (dictionary.get(j).code.equals(curr))
                    {
                        c = dictionary.get(j).text.charAt(0) ;
                        found = true ;
                        curr = "" ;
                        break ;
                    }
                }

                if (!found)
                {
                    continue;
                }
                else
                {
                    decompressed += c ;
                }
            }
            WrtieToFile(decompressed, name);
        }

}
