import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JScrollPane;
import javax.swing.event.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Chatroom extends JFrame {

    static final int portNum = 9500;

    static public int getPortnum(){
        return portNum;
    }
    static final String serverIp = "192.168.0.5";

    static public String getServerIp(){
        return serverIp;
    }
    JTextArea output;
    JTextField input;
    JButton sendBtn;
    Socket socket;
    ObjectInputStream reader = null;
    ObjectOutputStream writer = null;
    String nickName;

    public DefaultListModel listModel;


    //--------------------------------------UI START--------------------------------------------------//
    public Chatroom() {
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Toppanel Top = new Toppanel();
        add(Top, BorderLayout.NORTH);


        Middlepanel Middle = new Middlepanel();
        add(Middle, BorderLayout.CENTER);


        Bottompanel Bottom = new Bottompanel();

        add(Bottom, BorderLayout.SOUTH);


        setVisible(true);

        Bottom.service();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    //InfoDTO dto = new InfoDTO(nickName,Info.EXIT);
                    InfoDTO dto = new InfoDTO();
                    dto.setNickName(nickName);
                    dto.setCommand(Info.EXIT);
                    writer.writeObject(dto);
                    writer.flush();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) {
        new Chatroom();
    }


    // ----------------------------------------- Top --------------------------------------- //
    class Toppanel extends JPanel {

        Toppanel() {
            setBackground(Color.gray);
            Top_right TR = new Top_right();
            setLayout(new BorderLayout());
            add(TR, BorderLayout.EAST);


        }

        class Top_right extends JPanel {
            Top_right() {
                setBackground(Color.gray);
                add(new JButton("쪽지보관함"));
                JButton memberInfo = new JButton("회원정보수정");

                memberInfo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new mypage(null);
                    }
                });

                add(memberInfo);
                add(new JButton("로그아웃"));
            }
        }
    }


    // ----------------------------------------- Middle --------------------------------------- //
    class Middlepanel extends JPanel {

        Middlepanel() {
            setBackground(Color.white);
            setLayout(new BorderLayout());
            Middle_right MR = new Middle_right();
            add(MR, BorderLayout.EAST);
        }

        class Middle_right extends JPanel {

            Middle_right() {
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

            class Middle_right_down extends JPanel {
                Middle_right_down() {
                    setBackground(Color.YELLOW);
                    add(new JLabel("대기방 접속자"));
                }
            }


        }
    }


    // ----------------------------------------- Bottom --------------------------------------- //
    class Bottompanel extends JPanel implements ActionListener, Runnable {
        JLabel chatNick; // 바꿀 닉네임
        String loginNick = loginPanel.getNAME();

        Bottompanel() {
            setLayout(new BorderLayout());
            add((new Bottom_right()), BorderLayout.EAST);
            add((new Bottom_left()), BorderLayout.WEST);

        }

        class Bottom_right extends JPanel {
            Bottom_right() {
                setLayout(new BorderLayout());

                //add((new JLabel("친구")), BorderLayout.NORTH);
                add((new Online()), BorderLayout.CENTER);


            }



            public class Online extends JPanel
                    implements ListSelectionListener {
                private JList list;

                private JButton fireButton;
                private JTextField employeeName;

                public Online() {


                    super(new BorderLayout());

                    listModel = new DefaultListModel();
                    listModel.addElement(""); // list가 비었을때도 출력시키는 방법 찾아야해




                    //Create the list and put it in a scroll pane.
                    list = new JList(listModel);
                    list.setVisibleRowCount(5);
                    list.setFixedCellWidth(180);
                    JScrollPane listScrollPane = new JScrollPane(list);


                    add((new JLabel("온라인 접속자")), BorderLayout.NORTH);
                    add(listScrollPane, BorderLayout.CENTER);

                }


                @Override
                public void valueChanged(ListSelectionEvent e) {

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
                    chatNick = new JLabel(loginNick);
                    add(chatNick);


                    input = new JTextField(55);
                    sendBtn = new JButton("입력");
                    add(input);
                    add(sendBtn);
                }
            }

        }

        //--------------------------------------UI END--------------------------------------------------//
        public void service() {

            String serverIP = JOptionPane.showInputDialog(this, "서버IP를 입력하세요", serverIp);
            if (serverIP == null || serverIP.length() == 0) {
                System.out.println("서버IP가 입력되지 않았습니다.");
                System.exit(0);
            }



                /*nickName = JOptionPane.showInputDialog(this, "닉네임을 입력하세요", "닉네임", JOptionPane.INFORMATION_MESSAGE);
                if (nickName == null || nickName.length() == 0) {
                    nickName = "guest";}*/

                   nickName = loginNick;


            chatNick.setText(loginNick + " 님");

            try {
                socket = new Socket(serverIP, portNum);

                reader = new ObjectInputStream(socket.getInputStream());
                writer = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("전송 준비 완료!");

            } catch (UnknownHostException e) {
                System.out.println("서버를 찾을 수 없습니다.");
                e.printStackTrace();
                System.exit(0);
            } catch (IOException e) {
                System.out.println("서버와 통신 불가.");
                e.printStackTrace();
                System.exit(0);
            }
            try {

                InfoDTO dto = new InfoDTO();
                dto.setCommand(Info.JOIN);
                dto.setNickName(nickName);

                // listModel.addElement(dto.getNickName());
                writer.writeObject(dto);
                writer.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }

            Thread t = new Thread(this);
            t.start();
            input.addActionListener(this);
            sendBtn.addActionListener(this);
        }


        //Runnable
        @Override
        public void run() {
            InfoDTO dto = null;
            while (true) {

                try {
                    dto = (InfoDTO) reader.readObject();

                    if (dto.getCommand() == Info.EXIT) {

                        listModel.removeElement(dto.getNickName());
                        reader.close();
                        writer.close();
                        socket.close();
                        System.exit(0);
                    } else if (dto.getCommand() == Info.SEND) {
                        output.append(dto.getMessage() + "\n");


                        int pos = output.getText().length();
                        output.setCaretPosition(pos);

                    } /*else if(dto.getCommand()==Info.PLUS){
                        listModel.addElement(dto.getMessage());

                    }else if(dto.getCommand()==Info.MINU){
                        listModel.removeElement(dto.getMessage());}*/



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


        //ActionPerformed
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String msg = input.getText();
                InfoDTO dto = new InfoDTO();

                if (msg.equals("exit")) {
                    dto.setCommand(Info.EXIT);
                } else {

                    msg = msg.trim(); //
                    if(!msg.equals("")){
                        dto.setCommand(Info.SEND);
                        dto.setMessage(msg);
                        dto.setNickName(nickName);
                    }

                }
                writer.writeObject(dto);
                writer.flush();
                input.setText("");

            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }




}

// 회원정보 수정창
class mypage extends JFrame implements ActionListener{
    JPanel mainPanel;
    JPanel centerPanel;
    JPanel downPanel;
    JButton logout;
    login l;
    Font font = new Font("정보수정", Font.BOLD, 40);

    JTextField changenameTF;
    JTextField changepasswordTF;
    JTextField changebirthTF;

    JButton changenameButton;
    JButton changepasswordButton;
    JButton changebirthButton;

    public mypage(login l) {
        this.l=l;

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel mypageLabel = new JLabel("내 정보");
        mypageLabel.setFont(font);
        mypageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        changenameTF = new JTextField(20);
        changepasswordTF = new JTextField(20);
        changebirthTF = new JTextField(10);
        changenameButton = new JButton("이름 수정");
        changepasswordButton = new JButton("비밀번호 수정");
        changebirthButton = new JButton("생일 수정");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 0, 0);

        c.gridx = 0;
        c.gridy = 0;
        centerPanel.add(changenameTF, c);

        c.gridx = 1;
        c.gridy = 0;
        centerPanel.add(changenameButton, c);

        c.gridx = 0;
        c.gridy = 1;
        centerPanel.add(changepasswordTF, c);

        c.gridx = 1;
        c.gridy = 1;
        centerPanel.add(changepasswordButton, c);

        c.gridx = 0;
        c.gridy = 2;
        centerPanel.add(changebirthTF, c);

        c.gridx = 1;
        c.gridy = 2;
        centerPanel.add(changebirthButton, c);

        downPanel = new JPanel();
        logout = new JButton("로그아웃");

        mainPanel.add(mypageLabel);
        mainPanel.add(centerPanel);
        mainPanel.add(downPanel);





        logout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                l.card.first(l.cardPanel);
            }
        });

        changenameButton.addActionListener(this);
        changepasswordButton.addActionListener(this);
        changebirthButton.addActionListener(this);


        add(mainPanel);
        setSize(600, 300);
        setVisible(true);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = l.getConnection();

            switch (e.getActionCommand()) {

                case "이름 바꾸기":
                    String SQL = "update user_info set name=? where id=?";
                    pstmt = con.prepareStatement(SQL);

                    pstmt.setString(1, changenameTF.getText());
                    pstmt.setString(2, l.id);

                    int r = pstmt.executeUpdate();
                    System.out.println("변경된 row : " + r);

                    break;

                case "비밀번호 바꾸기":
                    String SQL1 = "update user_info set password=? where id=?";
                    pstmt = con.prepareStatement(SQL1);

                    pstmt.setString(1, changepasswordTF.getText());
                    pstmt.setString(2, l.id);

                    int r1 = pstmt.executeUpdate();
                    System.out.println("변경된 row : " + r1);
                    break;

                case "생일 바꾸기":
                    String SQL2 = "update user_info set birthday=? where id=?";
                    pstmt = con.prepareStatement(SQL2);

                    pstmt.setString(1, changepasswordTF.getText());
                    pstmt.setString(2, l.id);

                    int r2 = pstmt.executeUpdate();
                    System.out.println("변경된 row : " + r2);
                    break;
            }
        }catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}