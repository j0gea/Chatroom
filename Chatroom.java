import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;


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
class Bottompanel extends JPanel{

    Bottompanel(){
        setLayout(new BorderLayout());
        add((new Bottom_right()),BorderLayout.EAST);
        add((new Bottom_left()), BorderLayout.WEST);
    }

    class Bottom_right extends JPanel{
        Bottom_right(){
            setLayout(new BorderLayout());

            add((new JLabel("친구")),BorderLayout.NORTH);
            add((new Online()),BorderLayout.CENTER);
        }

        class Online extends JPanel{
            Online(){
                setLayout(new BorderLayout());
                add((new JLabel("온라인")), BorderLayout.NORTH);

                JTextArea online_list = new JTextArea(7,18);
                //online_list.setBackground(Color.BLUE);
                add(online_list, BorderLayout.CENTER);
            }

        }
    }

    class Bottom_left extends JPanel{
        Bottom_left(){
            setLayout(new BorderLayout());
            add((new Bottom_left_up()),BorderLayout.NORTH);
            add((new Bottom_left_mid()), BorderLayout.CENTER);
            add((new Bottom_left_down()), BorderLayout.SOUTH);

            //----- 통신 부분 -----//
            //this.addWindowListener

        }

        class Bottom_left_up extends JPanel{
            Bottom_left_up(){
                add(new JButton("방만들기"));
                add(new JLabel("방찾기"));
                add(new JTextField(15));
                add(new JButton("입력"));
                add(new JButton("대기방 보기"));
                add(new JButton("전체방 보기"));
            }
        }

        class Bottom_left_mid extends  JPanel{
            Bottom_left_mid(){

                JTextArea chat = new JTextArea(7, 50);
                chat.setEditable(false);
                JScrollPane chat_scroll = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                chat_scroll.setBounds(50,50,340,330);
                add(chat_scroll);
            }

        }

        class Bottom_left_down extends  JPanel{
            Bottom_left_down(){
                String[] chat_l = {"전체", "귓속말"};
                JList chat_list = new JList(chat_l);
                chat_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                add(chat_list);

                add(new JTextField(55));
                add(new JButton("입력"));
            }
        }

    }
}
