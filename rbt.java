/**************************************
*
*      Andre Leite - 2015250489
*      Reference to ITA book, 365
*
*
**************************************/





import java.io.IOException;
import java.util.*;

class Node{

    public String word = "";
    public int[] lines;
    public Node parent, left, right;
    public int color;
    private int cardinalL;

    public Node(String w,int line){
        this.word = w;
        this.color = 0;
        this.lines = new int[1];
        this.parent = this.right = this.left = null;
        this.lines[0] = line;
        cardinalL = 1;
    }

    public void addNewLine(int value){
        int out = 0;
        for(int i = 0; i < cardinalL && out != 1; i++){
            if(this.lines[i] == value){
                out = 1;
            }
        }
        if(out != 1){
            int[] novo = new int[this.lines.length+1];
            for(int j = 0; j < this.lines.length; j++){
                novo[j] = this.lines[j];
            }
            novo[this.lines.length] = value;
            this.lines = novo;
            this.cardinalL++;
        }
    }

}

class RedNBlack {

    Node root;
    Node Tnil;

    public RedNBlack() {
        Tnil = new Node("",-1); // TNIL é uma sentinela que permite saber onde é o fim da árvore
        Tnil.color = 0; // TNIL é sempre preto e representa um nó NULL
        Tnil.left = null;
        Tnil.right = null;
        root = Tnil; // inicia-se a root
    }

    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void insert(String word, int line) {

        Node node = new Node(word,line);
        node.parent = null;
        node.left = Tnil;
        node.right = Tnil;
        node.color = 1;

        Node y = null;
        Node x = this.root;

        while (x != Tnil) { // procura de um pai adequado, para o novo nó
            y = x;
            if (node.word.compareTo(x.word) < 0) {
                x = x.left;
            }else if(node.word.compareTo(x.word) > 0) {
                x = x.right;
            }else{
                break;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.word.compareTo(y.word) < 0) {
            y.left = node;
        } else if (node.word.compareTo(y.word) > 0){
            y.right = node;
        }else{
            y.addNewLine(line);
            return;
        }

        if (node.parent == null){
            node.color = 0; // 0 -> Preto 1-> Vermelho
            return;
        }

        if (node.parent.parent == null) {
            return;
        }
        insertFixup(node);
    }

    private void insertFixup(Node k){
        Node u;
        while (k.parent.color == 1) { // enquanto o pai é vermelho implica uma quebra na regra que
            if (k.parent == k.parent.parent.right) { // se o pai estiver à direita
                u = k.parent.parent.left;
                if (u.color == 1) { // verficar se o "tio" do nó k, é vermelho. Caso seja, recoloração bottom up
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else { // se o tio for um nó preto
                    if (k == k.parent.left) { // e se k estiver à esquerda realiza-se uma rotação à direita para que este nó passe a ser pai de k
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent); // finalizar sempre com rotação à esquerda para equilibrar esta sub-arvore
                }
            } else { // caso complementar ao anteriormente referido
                u = k.parent.parent.right;
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

    public String findNode(Node node, String token, String lines, String flag){

        int i = 0;
        boolean out = false;
        String specifics = "";

        if(node == Tnil || node == null){
            if(flag.equals("lines")){
                return "-1\n";
            }else{
                return "NAO ENCONTRADA.\n";
            }
        }

        if(token.compareTo(node.word) < 0){
            specifics = findNode(node.left,token,lines,flag);
        }else if(token.compareTo(node.word) > 0){
            specifics = findNode(node.right,token,lines,flag);
        }else{
            for(; i < node.lines.length-1; i++){
                specifics += node.lines[i]+" ";
                if(String.valueOf(node.lines[i]).equals(lines)){
                    out = true;
                }
            }
            specifics += node.lines[i]+"\n";
            if(String.valueOf(node.lines[i]).equals(lines)){
                out = true;
            }

            if(flag.equals("assoc")){
                if(out){
                    return "ENCONTRADA.\n";
                }else{
                    return "NAO ENCONTRADA.\n";
                }
            }

            if( i != 0 ){
                return specifics;
            }else{
                return "-1\n";
            }

        }
        return specifics;
    }

    void printTree(Node node, String token)
    {
        if(node == Tnil || node == null){
            return;
        }else{
            System.out.println("Node: "+node.word+" len: "+node.lines.length);
        }
        if(token.compareTo(node.word) < 0){
            printTree(node.left,token);
        }else if(token.compareTo(node.word) > 0){
            printTree(node.right,token);
        }else{
            return;
        }
    }

}


public class Main{

    static String readLn (int maxLg) {
        byte lin[] = new byte[maxLg];
        int lg = 0, car = -1;
        String line = "";
        try {
            while (lg < maxLg) {
                car = System.in.read();
                if ((car < 0) || (car == '\n')) break;
                lin[lg++] += car;
            }
        } catch (IOException e) {
            return (null);
        }
        if ((car < 0) && (lg == 0)) return (null);

        return (new String(lin, 0, lg));
    }

    public static void main(String[] args) {

        String regex = "";
        int end = 1;
        int lines = -1;
        StringTokenizer st;
        RedNBlack redBlack = new RedNBlack();
        int assoc = 0;
        ArrayList<String> reply = new ArrayList<String>();
        ArrayList<String[]> tokenSaver = new ArrayList<String[]>();

        regex = readLn(1024);
        if(regex!=null){
            st = new StringTokenizer(regex," ");
            if(st.nextToken().equals("TEXTO") && regex != "") {
                while (end != 0) {
                    lines++;
                    regex = readLn(1024);
                    if(regex!=null){
                        st = new StringTokenizer(regex, "\\,\\.\\;\\~\"\\(\\) ");
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken();
                            if(token.equals("FIM")){
                                end = 0;
                                break;
                            }else{
                                redBlack.insert(token,lines);
                                end = 1;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("GUARDADO.");
        int post = 0, switcher;
        st = null;

        while(post == 0 && regex != ""){

            regex = readLn(1024);
            if(regex!=null){
                String header = "";
                try{
                    st = new StringTokenizer(regex," ");
                    header = st.nextToken();
                }catch (NoSuchElementException e){
                    System.out.printf("");
                }
                String[] tokenS = new String[2];
                switcher = 0;

                if(header.equals("TCHAU")){
                    for(int  i = 0; i < reply.size(); i++){
                        System.out.printf(reply.get(i));
                    }
                    post = 1;
                }else if(header.equals("LINHAS")){
                    assoc = 0;
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        reply.add(redBlack.findNode(redBlack.root,token,"0","lines"));
                    }
                }else if(header.equals("ASSOC")){
                    assoc = 1;
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        tokenS[switcher] = token;
                        switcher++;
                    }
                }
                if(assoc == 1){
                    reply.add(redBlack.findNode(redBlack.root,tokenS[0],tokenS[1],"assoc"));
                    assoc = 0;
                }

            }

        }

    }

}
