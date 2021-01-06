public class main {
    public static void main(String args[]) {
        Huffman h=new Huffman();
        h.Compress("C:\\Documents\\GitHub\\StandardHuffman\\test.txt", "compressed");
        h.Decompress("C:\\Documents\\GitHub\\StandardHuffman\\compressed.txt", "decompressed");
    }
}
