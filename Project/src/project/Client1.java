package Project;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class Client1 extends JFrame implements ActionListener,Runnable
{
    JMenuBar menubar;
    JMenuItem signout,grp;
    JTabbedPane j;
    JPanel p1,p2,p3;
    List t1,t2,t3;
    Connection c;
    Statement s;
    ResultSet rs;
    String user;
    Font f=new Font("snap itc", Font.BOLD,40);
    Font f1=new Font("elephant",Font.BOLD,20);
    Font f2=new Font("times new roman",Font.BOLD,14);
    Thread thread;
    Container con;
    boolean status,flag;
    Color color=new Color(255,127,39);
    Socket socket;
    int i=9;
    
    Client1(String username) throws SQLException
    {
        super(username);
//        setContentPane(new JLabel(new ImageIcon("C:\\Users\\palak.garg\\Desktop\\bg.jpg")));
        con=getContentPane();
        con.setBackground(color);
        con.setLayout(null);
        
        j=new JTabbedPane();
        p1=new JPanel();
        p2=new JPanel();
        p3=new JPanel();
        
        j.setBounds(100,0,400,440);
        con.add(j);     j.setFont(f1);
        
        j.add("ONLINE",p1);
        j.add("CHATS",p2);
        j.add("CONTACTS",p3);
        
        p1.setLayout(null);
        p1.setBackground(color);
        
        p2.setLayout(null);
        p2.setBackground(color);
        
        p3.setLayout(null);
        p3.setBackground(color);
        
        menubar=new JMenuBar();
        signout=new JMenuItem("Sign Out");
        grp=new JMenuItem("Create a group");
        menubar.add(signout);
        menubar.add(grp);
        setJMenuBar(menubar);
        signout.addActionListener(this);
        grp.addActionListener(this);
        
        setSize(600,500);
        setVisible(true);
        setLocation(100,60);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        user=new String(username);
        status=false;
        flag=true;
        
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil12345");
            s=c.createStatement();
            System.out.print("Connected");
            rs=s.executeQuery("select * from server");
            if(rs.next())
               socket=new Socket(InetAddress.getLocalHost(),Integer.parseInt(rs.getString(1))); 
            else 
            {
                
                dispose();
                new Project();
                JOptionPane.showMessageDialog(this,"server not found");
                s.executeUpdate("delete from available where clients='"+user+"'");
            }
        }
        catch(Exception e)
        {
            s.executeUpdate("delete from available where clients='"+user+"'");
            System.out.println("Error: "+e);
            JOptionPane.showMessageDialog(this,e+"Exception in sockets retrieval");
        } 
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent w)
            {
                try
                {
                    s.executeUpdate("delete from available where clients='"+user+"'");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,e);
                }
            }
        });
        thread=new Thread(this);
        thread.start();
    }
    public void update(Graphics g)
    {
        paint(g);
    }
    public void run()
    {
        while(flag)
        {
            if(status)
            {
                p1.remove(t1);
                p2.remove(t2);
                p3.remove(t3);
            }
            try
            {
                t1=new List(7);
                t2=new List(7);
                t3=new List(7);
                rs=s.executeQuery("select * from available");
                while(rs.next())
                {
                    if(rs.getString(1).equals(user))
                        continue;
                    t1.add(rs.getString(1));
                }
                t1.addActionListener(this);
                t1.setFont(f1);
                t1.setBounds(20,20,350,360);
                p1.add(t1);
                p1.validate();
                p1.repaint();
                
                rs=s.executeQuery("select * from grp where member='"+user+"'");
                while(rs.next())
                    t2.add(rs.getString(1));
                
                t2.addActionListener(this);
                t2.setFont(f1);
                t2.setBounds(20,20,350,360);
                p2.add(t2);
                p2.validate();
                p2.repaint();
            
                rs=s.executeQuery("select * from tblclients");
                while(rs.next())
                {
                    if(rs.getString("username").equals(user))
                        continue;
                    t3.add(rs.getString("username")+"("+rs.getString("name")+")");
                }
                t3.addActionListener(this);
                t3.setFont(f1);
                t3.setBounds(20,20,350,360);
                p3.add(t3);
                p3.validate();
                p3.repaint();
                status=true;
                thread.sleep(4000);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e+ "Exception in tblClients");
            }
        }
    }
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource()==signout)
        {
            try
            {
                flag=false;
                s.executeUpdate("delete from available where clients='"+user+"'");
                c.close();
                dispose();
                new Project();
            }
            catch(Exception e)
            {
//                JOptionPane.showMessageDialog(this,e);
                System.out.println("Exception occured! in 198! "+e);
            }
        }
        else if(a.getSource()==grp)
        {
            try
            {
                flag=false;
                c.close();
                dispose();
                new grup(user);
            }
            catch(Exception e)
            {
//                JOptionPane.showMessageDialog(this,e);
                 System.out.println("Exception occured! in 214"+e);
            }
        }
        else if(a.getSource()==t1)
        {
            try
            {
                flag=false;
                c.close();
                String temp=t1.getSelectedItem();
                dispose();
                new Client2(user,temp,socket,false);
            }
            catch(Exception e)
            {
//                JOptionPane.showMessageDialog(this,e);
                 System.out.println("Exception occured! in 230:"+e);
            }
        }
        else if(a.getSource()==t2)
        {
            try
            {
                flag=false;
                c.close();
                dispose();
                String temp=t2.getSelectedItem();
                new Client2(user,temp,socket,true);
            }
            catch(Exception e)
            {
//                JOptionPane.showMessageDialog(this,e);
                 System.out.println("Exception occured! in 246"+e);
            }
        }
        else if(a.getSource()==t3)
        {
            try
            {
                flag=false;
                c.close();
                String temp=t3.getSelectedItem();
                int location=temp.indexOf('(');
                temp=temp.substring(0,location);
                dispose();
                new Client2(user,temp,socket,false);
            }
            catch(Exception e)
            {
//                JOptionPane.showMessageDialog(this,e);
                 System.out.println("Exception occured! in 264 "+e);
            }
        }
    }
}

class Client2 extends JFrame implements ActionListener,Runnable
{
    JLabel lbl;
    JButton send;
    JScrollPane s1,s2;
    JTextArea t1;
    JTextField t2;
    JMenuBar menubar;
    JMenuItem back,add;
    Connection c;
    Statement st;
    ResultSet rs;
    BufferedReader b;
    PrintStream p;
    Socket socket;
    String user,name;
    Font f1=new Font("elephant",Font.BOLD,20);
    Font f2 = new Font("times new roman", Font.BOLD, 15);
    Thread thread;
    boolean flag,group;
    
    Client2(String username,String temp,Socket sockt,boolean group)
    {
        super(username);
        setContentPane(new JLabel(new ImageIcon("E:\\java programs\\back.jpg")));
        setLayout(null);
        
        lbl=new JLabel(temp);
        t1=new JTextArea();
        t2=new JTextField(0);
        s1=new JScrollPane(t1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        s2=new JScrollPane(t2,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        send=new JButton("SEND");
        
        send.addActionListener(this);
        
        lbl.setBounds(250,0,100,100);
        s1.setBounds(20,60,500,300);
        s2.setBounds(20,380,400,50);
        send.setBounds(430,380,100,50);
        
        add(lbl);   lbl.setFont(f1);
        add(s1);     s1.setFont(f1);
        add(s2);    s2.setFont(f2);
        add(send);  send.setFont(f2);
        t1.setFont(f2);
        t1.setEditable(false);
        t2.setFont(f2);
       
        menubar=new JMenuBar();
        back=new JMenuItem("Back");
        add=new JMenuItem("Add");
        
        menubar.add(back);
        setJMenuBar(menubar);
        back.addActionListener(this);
        
        user=username;
        name=temp;
        socket=sockt;
        this.group=group;
        flag=true;
        
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil12345");
            st=c.createStatement();
            b=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            p=new PrintStream(socket.getOutputStream(),true);
            
            if(group)
            {
                rs=st.executeQuery("select * from gchat where gname='"+name+"'");
                while(rs.next())
                    t1.setText(t1.getText()+"\n"+rs.getString(2)+": "+rs.getString(3));
            }
            else
            {
                 rs=st.executeQuery("select * from chat where (sname='"+name+"'and dname='"+user+"')or(sname='"+user+"' and dname='"+name+"')");
                while(rs.next())
                {
                    if(rs.getString(1).equals(name))
                        t1.setText(t1.getText()+"\n"+name+": "+rs.getString(3));
                    else
                        t1.setText(t1.getText()+"\n"+"You: "+rs.getString(3));    
                }
            }
            
        }
        catch(Exception e)
        {
//            JOptionPane.showMessageDialog(this,e);
            System.out.println("Exception in line 373: "+e);
        }
        thread =new Thread(this);
        thread.start();
        setSize(600,500);
        setVisible(true);
        setLocation(100,60);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent w)
            {
                try
                {   
                    st.executeUpdate("delete from available where clients='"+user+"'");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,e);
                }
            }
        });
    }
    public void run()
    {
        while(flag)
        {
            try
            {
                String str=b.readLine();
                if(str.startsWith(user))
                {
                    if(str.endsWith(name))
                    {
                        if(group)
                        {
                            int location=str.lastIndexOf(' ');
                            String temp=str.substring(location);
                            temp=temp.substring(0,temp.length()-name.length()-1);
                            t1.append("\n"+temp+": "+str.substring(user.length(),location));
                        }
                        else
                            t1.append("\n"+name+": "+str.substring(user.length(),str.length()-name.length()));
                    }
                    else
                    {
                        int location=str.lastIndexOf(' ');
                        String temp=str.substring(location);
                        JOptionPane.showMessageDialog(this,"message from "+temp);
                    }
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e);
            }
        }
    }
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource()==back)
        {
            try
            {
                flag=false;
                c.close();
                dispose();
                new Client1(user);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e);
            }
        }
        else if(a.getSource()==send)
        {
            if(group)
            {
                try
                {
                    rs=st.executeQuery("select * from grp where gname='"+name+"'");
                    while(rs.next())
                    {
                        if(!rs.getString(2).equals(user))
                            p.println(rs.getString(2)+" "+t2.getText()+" "+user+"@"+name);
                    }
                    t1.append("\nYou: "+t2.getText());
                    st.executeUpdate("insert into gchat values ('"+name+"','"+user+"','"+t2.getText()+"')");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this,e);
                }
            }
            else
            {
                p.println(name+" "+t2.getText()+" "+user);
                t1.append("\nYou: "+t2.getText());
                try
                {
                    st.executeUpdate("insert into chat values ('"+user+"','"+name+"','"+t2.getText()+"')");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this,e);
                }
            }
            t2.setText("");
        }
    }
}
class grup extends JFrame implements ActionListener
{
    Connection c;
    Statement st;
    List t1,t2;
    Color color=new Color(255,127,39);
    Font f1=new Font("elephant",Font.BOLD,20);
    JMenuBar menubar;
    JMenuItem back;
    JButton create;
    JTextField name;
    JLabel lbl;
    Container cont;
    ResultSet rs;
    String user;
    
    grup(String user)
    {
        super(user);
        this.user=user;
        
        cont=getContentPane();
        cont.setBackground(color);
        cont.setLayout(null);
       
        lbl=new JLabel("Enter the group name");
        name=new JTextField();
        t1=new List(5);
        t2=new List(5);
        create=new JButton("Create");
        t1.addActionListener(this);
        t2.addActionListener(this);
        create.addActionListener(this);
        
        lbl.setBounds(20,20,200,50);
        name.setBounds(300,20,200,50);
        t1.setBounds(10,100,250,250);
        t2.setBounds(300,100,250,250);
        create.setBounds(20,400,400,50);
        
        add(lbl);   lbl.setFont(f1);
        add(name);  name.setFont(f1);
        add(t1);    t1.setFont(f1);
        add(t2);    t2.setFont(f1);
        add(create);    create.setFont(f1);
        
        menubar=new JMenuBar();
        back=new JMenuItem("Back");
        menubar.add(back);
        back.addActionListener(this);
        
        setSize(600,500);
        setVisible(true);
        setLocation(100,60);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil12345");
            st=c.createStatement();
            rs=st.executeQuery("select * from tblclients");
            while(rs.next())
            {
                if(!rs.getString("username").equals(user))
                    t1.add(rs.getString("username"));
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e);
        }
        
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent w)
            {
                try
                {   
                    st.executeUpdate("delete from available where clients='"+user+"'");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,e);
                }
            }
        });
    }
    public void actionPerformed(ActionEvent a)
    {
        if(a.getSource()==t1)
        {
            t2.add(t1.getSelectedItem());
            t1.remove(t1.getSelectedItem());
            
        }
        else if(a.getSource()==t2)
        {
            t1.add(t2.getSelectedItem());
            t2.remove(t2.getSelectedItem());
        }
        else if(a.getSource()==back)
        {
            try
            {
                c.close();
                dispose();
                new Client1(user);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e);
            }
        }
        else if(a.getSource()==create)
        {
            try
            {
                name.setEditable(false);
                if(name.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Invalid group name!");
                }
                t2.removeActionListener(this);
                st.executeUpdate("insert into grp values('"+name.getText()+"','"+user+"')");
                int count=t2.getItemCount();
                System.out.print(count);
                for(int i=0;i<count;i++)
                    st.executeUpdate("insert into grp values('"+name.getText()+"','"+t2.getItem(i)+"')");
                c.close();
                dispose();
                new Client1(user);
            }
            catch(Exception e)
            {
                System.out.println("Error in 617: "+e);
            }
        }
    }
}