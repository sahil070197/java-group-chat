package Project;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.sql.DriverManager;
import javax.swing.*;
import java.sql.*;
import java.util.*;

public class Server extends JFrame implements ActionListener,Runnable
{
    JLabel lbl,p,i;
    JButton b;
    JTextField t1,t2;
    Font f=new Font("elephant", Font.BOLD,20);
    Font f1=new Font("elephant",Font.PLAIN,14);
    Connection c;
    Statement s;
    Thread thread;
    ServerSocket server;
    Socket socket;
    ArrayList clients;
    PrintStream pw;
    Color color = new Color(255, 127, 39);
    
    Server()
    {
        setContentPane(new JLabel(new ImageIcon("E:\\java programs\\back.jpg")));
        setLayout(null);
        //setBackground(color);
        lbl=new JLabel("SERVER");
        
        p=new JLabel("PORT NO");
        t1=new JTextField(20);
        i=new JLabel("IP ADDRESS");
        t2=new JTextField(20);
        b=new JButton("START Server");
        
        lbl.setBounds(250,0,400,100);
        p.setBounds(0,100,80,50);
        t1.setBounds(80,100,100,50);
        i.setBounds(200,100,80,50);
        t2.setBounds(280,100,100,50);
        b.setBounds(440,100,150,50);
                
        add(lbl);  lbl.setFont(f);
        add(p);    p.setFont(f1);
        add(t1);  t1.setFont(f1);
        add(i);    i.setFont(f1);
        add(t2);  t2.setFont(f1);
        add(b);  b.setFont(f1);
        
        b.addActionListener(this);
        clients=new ArrayList<PrintStream>();
        
        try
        {
             DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
             c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","palak","garg");
             s=c.createStatement();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent w)
            {
                try
                {
                    s.executeUpdate("delete from server where 1=1");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });
    }
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource()==b)
        {
            if(!t1.getText().equals(""))
            {
                try
                {
                    s.executeUpdate("insert into server values('"+t1.getText()+"')");
                    String temp=""+InetAddress.getLocalHost();
                    int location=temp.lastIndexOf("/");
                    temp=temp.substring(location+1);
                    t2.setText(temp);
                    server=new ServerSocket(Integer.parseInt(t1.getText()));
                    thread=new Thread(this);
                    thread.start();
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            else 
            {
                JOptionPane.showMessageDialog(this,"enter the port number");
            }
        }
    }
    public void run()
    {
        while(true)
        {
            try
            {
                socket=server.accept();
                pw=new PrintStream(socket.getOutputStream(),true);
                clients.add(pw);
                new Server1(socket,clients);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e);
            }
        }
    }
    public static void main(String[] args) 
    {
        Server m=new Server();
        m.setSize(600, 500);
        m.setVisible(true);
        m.setDefaultCloseOperation(EXIT_ON_CLOSE);
        m.setResizable(false);
        m.setLocation(100,60);
    }
}
class Server1 extends JFrame implements Runnable
{
    BufferedReader b;
    Socket socket;
    Thread thread;
    ArrayList clients;
    
    Server1(Socket sockt,ArrayList<PrintStream> clients)
    {
        try
        {
            socket=sockt;
            this.clients=clients;
            b=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            thread=new Thread(this);
            thread.start();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e);
        }
    }
    public void run()
    {
        while(true)
        {
            try
            {
                String temp=b.readLine();
                Iterator<PrintStream> i=clients.iterator();
                while(i.hasNext())
                {
                    PrintStream p=i.next();
                    p.println(temp);
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }
}
