public class main {
    public static void main(String args[]) {
        Huffman h=new Huffman();
        h.Compress("D:\\Java Repos\\StandardHuffman\\test.txt", "compressed");
        h.Decompress("D:\\Java Repos\\StandardHuffman\\compressed.txt", "decompressed");
    }
}
