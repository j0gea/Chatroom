import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JScrollPane;
import java.io.*;
import java.net.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class Chatroom extends JFrame{


    public Chatroom(){
        setSize(900,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Toppanel Top = new Toppanel();
        add(Top, BorderLayout.NORTH);


        Middlepanel Middle = new Middlepanel();
        add(Middle, BorderLayout.CENTER);


        Bottompanel Bottom = new Bottompanel();

        add(Bottom, BorderLayout.SOUTH);



        setVisible(true);

        Bottom.service();
    }

    public static void main(String[] args){
        new Chatroom();
    }




}import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JScrollPane;
import java.io.*;
import java.net.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class Chatroom extends JFrame{


    public Chatroom(){
        setSize(900,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Toppanel Top = new Toppanel();
        add(Top, BorderLayout.NORTH);


        Middlepanel Middle = new Middlepanel();
        add(Middle, BorderLayout.CENTER);


        Bottompanel Bottom = new Bottompanel();

        add(Bottom, BorderLayout.SOUTH);



        setVisible(true);

        Bottom.service();
    }

    public static void main(String[] args){
        new Chatroom();
    }




}








// ---------- 위쪽 ---------- //
class Toppanel extends JPanel{

    Toppanel(){
        setBackground(Color.gray);
        Top_right TR = new Top_right();
        setLayout(new BorderLayout());
        add(TR, BorderLayout.EAST);



    }

    class Top_right extends JPanel{
        Top_right(){
            setBackground(Color.gray);
            add(new JButton("쪽지보관함"));
            add(new JButton("회원정보수정"));
            add(new JButton("로그아웃"));
        }
    }
}





// ---------- 중간 ---------- //
class Middlepanel extends JPanel{

    Middlepanel(){
        setBackground(Color.white);
        setLayout(new BorderLayout());
        Middle_right MR = new Middle_right();
        add(MR,BorderLayout.EAST);
    }

    class Middle_right extends JPanel{

        Middle_right(){
            //-- 찾기 --//
            setLayout(new BorderLayout());


            JPanel Search = new JPanel();
            add(Search, BorderLayout.NORTH);



            //-----//
            setBackground(Color.blue);
            JTextField Search_T = new JTextField(10);
            Search.add(Search_T);

            JButton Search_B = new JButton("찾기");
            Search.add(Search_B);
            //-----//


            Middle_right_down MRD = new Middle_right_down();
            add(MRD, BorderLayout.CENTER);

        }

        class Middle_right_down extends JPanel{
            Middle_right_down(){
                setBackground(Color.YELLOW);
                add(new JLabel("대기방 접속자"));
            }
        }


    }
}





// ---------- 아래 ---------- //
class Bottompanel extends JPanel implements ActionListener,Runnable {


    private JTextArea output;
    private JTextField input;
    private JButton sendBtn;
    private Socket socket;
    private ObjectInputStream reader=null;
    private ObjectOutputStream writer=null;
    private String nickName;


    Bottompanel() {
        setLayout(new BorderLayout());
        add((new Bottom_right()), BorderLayout.EAST);
        add((new Bottom_left()), BorderLayout.WEST);

    }

    class Bottom_right extends JPanel {
        Bottom_right() {
            setLayout(new BorderLayout());

            add((new JLabel("친구")), BorderLayout.NORTH);
            add((new Online()), BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try{
                        //InfoDTO dto = new InfoDTO(nickName,Info.EXIT);
                        InfoDTO dto = new InfoDTO();
                        dto.setNickName(nickName);
                        dto.setCommand(Info.EXIT);
                        writer.writeObject(dto);  //            ʿ䰡
                        writer.flush();
                    }catch(IOException io){
                        io.printStackTrace();
                    }
                }
            });

        }

        class Online extends JPanel {
            Online() {
                setLayout(new BorderLayout());
                add((new JLabel("온라인")), BorderLayout.NORTH);

                JTextArea online_list = new JTextArea(7, 18);
                //online_list.setBackground(Color.BLUE);
                add(online_list, BorderLayout.CENTER);
            }

        }
    }

    class Bottom_left extends JPanel {
        Bottom_left() {
            setLayout(new BorderLayout());
            add((new Bottom_left_up()), BorderLayout.NORTH);
            add((new Bottom_left_mid()), BorderLayout.CENTER);
            add((new Bottom_left_down()), BorderLayout.SOUTH);

        }

        class Bottom_left_up extends JPanel {
            Bottom_left_up() {
                add(new JButton("방만들기"));
                add(new JLabel("방찾기"));
                add(new JTextField(15));
                add(new JButton("입력"));
                add(new JButton("대기방 보기"));
                add(new JButton("전체방 보기"));
            }
        }

        class Bottom_left_mid extends JPanel {
            Bottom_left_mid() {

                output = new JTextArea(7, 50);
                output.setEditable(false);
                JScrollPane chat_scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


                chat_scroll.setBounds(50, 50, 340, 330);
                add(chat_scroll);



            }

        }

        class Bottom_left_down extends JPanel {
            Bottom_left_down() {
                String[] chat_l = {"전체", "귓속말"};
                JList chat_list = new JList(chat_l);
                chat_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                add(chat_list);

                input = new JTextField(55);
                sendBtn = new JButton("입력");
                add(input);
                add(sendBtn);
            }
        }

    }


    public void service(){

        String serverIP= JOptionPane.showInputDialog(this,"서버IP를 입력하세요","192.168.0.9");  // ⺻                    ԷµǾ    
        if(serverIP==null || serverIP.length()==0){
            System.out.println("서버IP가 입력되지 않았습니다.");
            System.exit(0);
        }

        nickName= JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임" ,JOptionPane.INFORMATION_MESSAGE);
        if(nickName == null || nickName.length()==0){
            nickName="guest";
        }
        try{
            socket = new Socket(serverIP,9500);

            reader= new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("전송 준비 완료!");

        } catch(UnknownHostException e ){
            System.out.println("서버를 찾을 수 없음");
            e.printStackTrace();
            System.exit(0);
        } catch(IOException e){
            System.out.println("서버와 연결이 안되었다.");
            e.printStackTrace();
            System.exit(0);
        }
        try{

            InfoDTO dto = new InfoDTO();
            dto.setCommand(Info.JOIN);
            dto.setNickName(nickName);
            writer.writeObject(dto);
            writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }

        Thread t = new Thread(this);
        t.start();
        input.addActionListener(this);
        sendBtn.addActionListener(this);
    }




    //Runnable
    @Override
    public void run(){
        InfoDTO dto= null;
        while(true){
            try{
                dto = (InfoDTO) reader.readObject();
                if(dto.getCommand()==Info.EXIT){
                    reader.close();
                    writer.close();
                    socket.close();
                    System.exit(0);
                } else if(dto.getCommand()==Info.SEND){
                    output.append(dto.getMessage()+"\n");

                    int pos=output.getText().length();
                    output.setCaretPosition(pos);
                }
            }catch(IOException e){
                e.printStackTrace();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }


    //ActionPerformed
    @Override
    public void actionPerformed(ActionEvent e){
        try{
            String msg=input.getText();
            InfoDTO dto = new InfoDTO();

            if(msg.equals("exit")){
                dto.setCommand(Info.EXIT);
            } else {
                dto.setCommand(Info.SEND);
                dto.setMessage(msg);
                dto.setNickName(nickName);
            }
            writer.writeObject(dto);
            writer.flush();
            input.setText("");

        }catch(IOException io){
            io.printStackTrace();
        }
    }

}


   //-------------------------------------------------------//












// ---------- 위쪽 ---------- //
class Toppanel extends JPanel{

    Toppanel(){
        setBackground(Color.gray);
        Top_right TR = new Top_right();
        setLayout(new BorderLayout());
        add(TR, BorderLayout.EAST);



    }

    class Top_right extends JPanel{
        Top_right(){
            setBackground(Color.gray);
            add(new JButton("쪽지보관함"));
            add(new JButton("회원정보수정"));
            add(new JButton("로그아웃"));
        }
    }
}





// ---------- 중간 ---------- //
class Middlepanel extends JPanel{

    Middlepanel(){
        setBackground(Color.white);
        setLayout(new BorderLayout());
        Middle_right MR = new Middle_right();
        add(MR,BorderLayout.EAST);
    }

    class Middle_right extends JPanel{

        Middle_right(){
            //-- 찾기 --//
            setLayout(new BorderLayout());


            JPanel Search = new JPanel();
            add(Search, BorderLayout.NORTH);



            //-----//
            setBackground(Color.blue);
            JTextField Search_T = new JTextField(10);
            Search.add(Search_T);

            JButton Search_B = new JButton("찾기");
            Search.add(Search_B);
            //-----//


            Middle_right_down MRD = new Middle_right_down();
            add(MRD, BorderLayout.CENTER);

        }

        class Middle_right_down extends JPanel{
            Middle_right_down(){
                setBackground(Color.YELLOW);
                add(new JLabel("대기방 접속자"));
            }
        }


    }
}





// ---------- 아래 ---------- //
class Bottompanel extends JPanel implements ActionListener,Runnable {


    private JTextArea output;
    private JTextField input;
    private JButton sendBtn;
    private Socket socket;
    private ObjectInputStream reader=null;
    private ObjectOutputStream writer=null;
    private String nickName;


    Bottompanel() {
        setLayout(new BorderLayout());
        add((new Bottom_right()), BorderLayout.EAST);
        add((new Bottom_left()), BorderLayout.WEST);

    }

    class Bottom_right extends JPanel {
        Bottom_right() {
            setLayout(new BorderLayout());

            add((new JLabel("친구")), BorderLayout.NORTH);
            add((new Online()), BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try{
                        //InfoDTO dto = new InfoDTO(nickName,Info.EXIT);
                        InfoDTO dto = new InfoDTO();
                        dto.setNickName(nickName);
                        dto.setCommand(Info.EXIT);
                        writer.writeObject(dto);  //            ʿ䰡
                        writer.flush();
                    }catch(IOException io){
                        io.printStackTrace();
                    }
                }
            });

        }

        class Online extends JPanel {
            Online() {
                setLayout(new BorderLayout());
                add((new JLabel("온라인")), BorderLayout.NORTH);

                JTextArea online_list = new JTextArea(7, 18);
                //online_list.setBackground(Color.BLUE);
                add(online_list, BorderLayout.CENTER);
            }

        }
    }

    class Bottom_left extends JPanel {
        Bottom_left() {
            setLayout(new BorderLayout());
            add((new Bottom_left_up()), BorderLayout.NORTH);
            add((new Bottom_left_mid()), BorderLayout.CENTER);
            add((new Bottom_left_down()), BorderLayout.SOUTH);

        }

        class Bottom_left_up extends JPanel {
            Bottom_left_up() {
                add(new JButton("방만들기"));
                add(new JLabel("방찾기"));
                add(new JTextField(15));
                add(new JButton("입력"));
                add(new JButton("대기방 보기"));
                add(new JButton("전체방 보기"));
            }
        }

        class Bottom_left_mid extends JPanel {
            Bottom_left_mid() {

                output = new JTextArea(7, 50);
                output.setEditable(false);
                JScrollPane chat_scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


                chat_scroll.setBounds(50, 50, 340, 330);
                add(chat_scroll);



            }

        }

        class Bottom_left_down extends JPanel {
            Bottom_left_down() {
                String[] chat_l = {"전체", "귓속말"};
                JList chat_list = new JList(chat_l);
                chat_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                add(chat_list);

                input = new JTextField(55);
                sendBtn = new JButton("입력");
                add(input);
                add(sendBtn);
            }
        }

    }


    public void service(){

        nickName= JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임" ,JOptionPane.INFORMATION_MESSAGE);
        if(nickName == null || nickName.length()==0){
            nickName="host";
        }





        //---- 서버용 ----//

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            socket = serverSocket.accept();
            System.out.println("host : "+socket.getInetAddress()+" | 통신 연결 성공");
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader= new ObjectInputStream(socket.getInputStream());
            System.out.println("전송 준비 완료!");

        } catch(UnknownHostException e ){
            System.out.println("서버를 찾을 수 없음");
            e.printStackTrace();
            System.exit(0);
        } catch(IOException e){
            System.out.println("서버와 연결이 안되었다.");
            e.printStackTrace();
            System.exit(0);
        }
        try{

            InfoDTO dto = new InfoDTO();
            dto.setCommand(Info.JOIN);
            dto.setNickName(nickName);
            writer.writeObject(dto);
            writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }

        Thread t = new Thread(this);
        t.start();
        input.addActionListener(this);
        sendBtn.addActionListener(this);
    }




    //Runnable
    @Override
    public void run(){
        InfoDTO dto= null;
        while(true){
            try{
                dto = (InfoDTO) reader.readObject();
                if(dto.getCommand()==Info.EXIT){
                    reader.close();
                    writer.close();
                    socket.close();
                    System.exit(0);
                } else if(dto.getCommand()==Info.SEND){
                    output.append(dto.getMessage()+"\n");

                    int pos=output.getText().length();
                    output.setCaretPosition(pos);
                }
            }catch(IOException e){
                e.printStackTrace();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }


    //ActionPerformed
    @Override
    public void actionPerformed(ActionEvent e){
        try{
            String msg=input.getText();
            InfoDTO dto = new InfoDTO();

            if(msg.equals("exit")){
                dto.setCommand(Info.EXIT);
            } else {
                dto.setCommand(Info.SEND);
                dto.setMessage(msg);
                dto.setNickName(nickName);
            }
            writer.writeObject(dto);
            writer.flush();
            input.setText("");

        }catch(IOException io){
            io.printStackTrace();
        }
    }

}


   //-------------------------------------------------------//



