package Project;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import sun.swing.ImageIconUIResource;
public class Project extends JFrame implements ActionListener, Runnable {

    JPanel p1, p2, p3;
    JLabel lbl, lbl2, lbl3;
    JButton signin, signup;
    Container c;
    Color color = new Color(255, 127, 39);
    String s = "      The world becomes a better place to live in when you connect..so come and join us", n;
    Font f = new Font("snap ITC", Font.BOLD, 40);
    Font f1 = new Font("times new roman", Font.BOLD, 14);
    Font f2 = new Font("elephant", Font.PLAIN, 16);
    int i, count = 0;
    Thread thread;
    boolean status = false;

    Project() {
        c = getContentPane();
        c.setBackground(color);
        c.setLayout(null);

        p1 = new JPanel();
        p1.setLayout(null);
        lbl = new JLabel("PING ME");
        signin = new JButton("SIGN IN");
        signup = new JButton("SIGN UP");

        lbl.setBounds(20, 0, 300, 100);
        signin.setBounds(400, 60, 100, 40);
        signup.setBounds(500, 60, 100, 40);

        p1.add(lbl);
        lbl.setFont(f);
        p1.add(signin);
        signin.setFont(f1);
        p1.add(signup);
        signup.setFont(f1);
        p2 = new JPanel();
        p2.setLayout(null);
//        lbl2 = new JLabel(new ImageIcon("C:\\Users\\palak.garg\\Desktop\\group-chat.jpg"));
//        lbl2.setBounds(0, 0, 600, 350);
//        p2.add(lbl2);

        p3 = new JPanel();
        p3.setLayout(new GridLayout());

        p1.setBackground(color);
        p2.setBackground(Color.BLACK);
        p3.setBackground(color);
        c.add(p1);
//        c.add(p2);
        c.add(p3);

        signin.addActionListener(this);
        signup.addActionListener(this);

        p1.setBounds(0, 50, 600, 100);
        p2.setBounds(0, 100, 600, 350);
        p3.setBounds(0, 0, 600, 50);

        setSize(600, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            while (true) {
                for (i = s.length() - 1; i >= 0; i--)
                {
                    thread.sleep(90);
                    if (status) {
                        p3.remove(lbl3);
                    }

                    if (count == 0) {
                        n = s.substring(i, s.length());
                    } else if (i == 0) {
                        n = s;
                    } else {
                        n = s.substring(i, s.length()) + s.substring(0, i - 1);
                    }

                    lbl3 = new JLabel(n);
                    lbl3.setBounds(20, 20, 600, 30);
                    p3.add(lbl3);
                    lbl3.setFont(f2);
                    p3.validate();
                    p3.repaint();
                    status = true;
                    
                    if (i == 0) {
                        i = s.length() - 1;
                        count = 1;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == signin) {
            dispose();
            new In();
        } else if (a.getSource() == signup) {
            dispose();
            new Up();
        }
    }

    public static void main(String args[]) {
        Project p = new Project();
    }
}

class In extends JFrame implements ActionListener {

    JLabel lbl, image;
    JTextField user;
    JPasswordField pass;
    JButton u, p, signin, forgot;
    Font f = new Font("snap ITC", Font.BOLD, 40);
    Font f1 = new Font("times new roman", Font.BOLD, 14);
    Connection con;
    Statement s;
    ResultSet rs;
    Container c;
    Color color = new Color(255, 127, 39);
    JMenuBar menubar;
    JMenuItem back;
    boolean connection = false;

    In() {
        c = getContentPane();
        c.setBackground(color);
        c.setLayout(null);

        lbl = new JLabel("PING ME");
        image = new JLabel(new ImageIcon("C:\\Users\\palak.garg\\Desktop\\images.jpg"));
        u = new JButton("USERNAME");
        user = new JTextField(20);
        p = new JButton("PASSWORD");
        pass = new JPasswordField();
        signin = new JButton("Sign In");
        forgot = new JButton("Forgot password?");

        lbl.setBounds(190, 0, 260, 50);
        image.setBounds(180, 50, 260, 250);
        u.setBounds(180, 300, 120, 40);
        user.setBounds(300, 300, 140, 40);
        p.setBounds(180, 340, 120, 40);
        pass.setBounds(300, 340, 140, 40);
        signin.setBounds(180, 380, 260, 30);
        forgot.setBounds(180, 410, 260, 30);

        c.add(lbl);
        lbl.setFont(f);
        c.add(image);
        c.add(u);
        u.setFont(f1);
        c.add(user);
        user.setFont(f1);
        c.add(p);
        p.setFont(f1);
        c.add(pass);
        pass.setFont(f1);
        c.add(signin);
        signin.setFont(f1);
        c.add(forgot);
        forgot.setFont(f1);

        menubar = new JMenuBar();
        back = new JMenuItem("Back");
        menubar.add(back);
        back.addActionListener(this);
        setJMenuBar(menubar);

        signin.addActionListener(this);
        forgot.addActionListener(this);
        setSize(600, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == back) {
            if (connection) {
                try {
                    con.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            dispose();
            new Project();
        } 
        else if (a.getSource() == signin) 
        {
            try
            {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "sahil12345");
                s = con.createStatement();

                connection = true;

                rs = s.executeQuery("select * from tblclients where username='" + user.getText() + "'");
                if (rs.next()) 
                {
                    char arr[] = pass.getPassword();
                    String temp = new String(arr);
                    String t = rs.getString(5);
                    if (t.equals(temp)) 
                    {
                        s.executeUpdate("insert into available values('"+user.getText()+"')");
                        con.close();
                        dispose();
                        new Client1(user.getText());
                    }
                    else 
                    {
                        JOptionPane.showMessageDialog(this, "Either username or password is incorrect");
                    }
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, "Exception Either username or password is incorrect"+e);
            }
        } else if (a.getSource() == forgot) {
            if (connection) {
                try {
                    con.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            dispose();
            new forg();
        }
    }
}

class forg extends JFrame implements ActionListener {

    JLabel lbl;
    JTextField user, pass;
    JButton u, p, signin, forgot;
    Font f = new Font("snap ITC", Font.BOLD, 40);
    Font f1 = new Font("times new roman", Font.BOLD, 14);
    Connection con;
    Statement s;
    ResultSet rs;
    Container c;
    Color color = new Color(255, 127, 39);
    JMenuBar menubar;
    JMenuItem back;
    boolean connection = false;

    forg() {
        c = getContentPane();
        c.setBackground(color);
        c.setLayout(null);

        lbl = new JLabel("PING ME");
        u = new JButton("USERNAME");
        user = new JTextField(20);
        p = new JButton("DOB(ddmmyyyy)");
        pass = new JTextField();
        signin = new JButton("Recover");
        forgot = new JButton("Reset");

        lbl.setBounds(190, 0, 260, 50);
        u.setBounds(20, 100, 200, 40);
        user.setBounds(250, 100, 300, 40);
        p.setBounds(20, 150, 200, 40);
        pass.setBounds(250, 150, 300, 40);
        signin.setBounds(150, 200, 200, 30);
        forgot.setBounds(380, 200, 200, 30);

        c.add(lbl);
        lbl.setFont(f);
        c.add(u);
        u.setFont(f1);
        c.add(user);
        user.setFont(f1);
        c.add(p);
        p.setFont(f1);
        c.add(pass);
        pass.setFont(f1);
        c.add(signin);
        signin.setFont(f1);
        c.add(forgot);
        forgot.setFont(f1);

        menubar = new JMenuBar();
        back = new JMenuItem("Back");
        menubar.add(back);
        back.addActionListener(this);
        setJMenuBar(menubar);

        signin.addActionListener(this);
        forgot.addActionListener(this);
        setSize(600, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == forgot) {
            user.setText("");
            pass.setText("");
        } else if (a.getSource() == signin) {
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "sahil12345");
                s = con.createStatement();

                connection = true;
                rs = s.executeQuery("select * from tblclients where username='" + user.getText() + "'");
                if (rs.next()) {
                    if (pass.getText().equals(rs.getString("dob"))) {
                        con.close();
                        dispose();
                        new recover(user.getText());
                    } else {
                        JOptionPane.showMessageDialog(this, "Either username or date of birth is incorrect");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Either username or date of birth is incorrect");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        } else if (a.getSource() == back) {
            if (connection) {
                try {
                    con.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            dispose();
            new In();
        }
    }
}

class recover extends JFrame implements ActionListener {

    JLabel lbl, lbl2;
    JTextField user;
    JPasswordField pass1, pass2;
    JButton u, p, p1, reset;
    Font f = new Font("snap ITC", Font.BOLD, 40);
    Font f1 = new Font("times new roman", Font.BOLD, 14);
    Connection con;
    Statement s;
    ResultSet rs;
    Container c;
    Color color = new Color(255, 127, 39);
    JMenuBar menubar;
    JMenuItem back;
    boolean connection = false;
    String us;

    recover(String us) {
        this.us = us;
        c = getContentPane();
        c.setBackground(color);
        c.setLayout(null);

        lbl = new JLabel("PING ME");
        u = new JButton("USERNAME");
        user = new JTextField(us);
        p = new JButton("NEW PASSWORD");
        p1 = new JButton("CONFIRM PASSWORD");
        pass1 = new JPasswordField();
        pass2 = new JPasswordField();
        reset = new JButton("Reset");
        lbl2 = new JLabel("(must contain a character,digit and special symbol)");

        lbl.setBounds(190, 0, 260, 50);
        u.setBounds(20, 100, 200, 40);
        user.setBounds(250, 100, 300, 40);
        p.setBounds(20, 150, 200, 40);
        pass1.setBounds(250, 150, 300, 40);
        lbl2.setBounds(250, 190, 300, 20);
        p1.setBounds(20, 220, 200, 40);
        pass2.setBounds(250, 220, 300, 40);
        reset.setBounds(180, 270, 400, 50);

        c.add(lbl);
        lbl.setFont(f);
        c.add(u);
        u.setFont(f1);
        c.add(user);
        user.setFont(f1);
        c.add(p);
        p.setFont(f1);
        c.add(pass1);
        pass1.setFont(f1);
        c.add(p1);
        p1.setFont(f1);
        c.add(pass2);
        pass2.setFont(f1);
        c.add(reset);
        reset.setFont(f1);
        c.add(lbl2);

        menubar = new JMenuBar();
        back = new JMenuItem("Back");
        menubar.add(back);
        back.addActionListener(this);
        setJMenuBar(menubar);
        reset.addActionListener(this);
        user.setEditable(false);
        setSize(600, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == reset) {
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "sahil12345");
                s = con.createStatement();
                connection = true;

                char arr[] = pass1.getPassword();
                String temp = new String(arr);
                arr = pass2.getPassword();
                String t = new String(arr);

                if (temp.equals(t)) {
                    s.executeUpdate("update tblclients set pass='" + temp + "' where username='" + us + "'");
                    JOptionPane.showMessageDialog(this, "Password has been changed successfully");
                    con.close();
                    new In();
                } else {
                    JOptionPane.showMessageDialog(this, "passwords do not match");
                    pass1.setText("");
                    pass2.setText("");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        } else if (a.getSource() == back) {
            dispose();
            if (connection) {
                try {
                    con.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            new In();
        }
    }
}

class Up extends JFrame implements ActionListener, ItemListener {

    JLabel name, sur, dob, user, pass, mobile, lbl, gen, p;
    JTextField namet, surt, dobt, usert, mob;
    JPasswordField passt;
    JButton signup, reset;
    Connection c;
    Statement s;
    ResultSet rs;
    JMenuBar menubar;
    JMenuItem back;
    Font f = new Font("snap ITC", Font.BOLD, 40);
    Font f1 = new Font("times new roman", Font.BOLD, 18);
    Color color = new Color(255, 127, 39);
    ButtonGroup gender;
    JRadioButton male, female;
    boolean connection = false;
    String g;
    Container container;

    Up() {
        container = getContentPane();
        container.setBackground(color);
        container.setLayout(null);

        lbl = new JLabel("PING ME");
        name = new JLabel("NAME");
        namet = new JTextField();
        sur = new JLabel("SURNAME");
        surt = new JTextField();
        dob = new JLabel("DOB (ddmmyyyy)");
        gen = new JLabel("GENDER");
        gender = new ButtonGroup();
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        gender.add(male);
        gender.add(female);
        dobt = new JTextField();
        user = new JLabel("USERNAME");
        usert = new JTextField();
        pass = new JLabel("PASSWORD");
        passt = new JPasswordField();
        mobile = new JLabel("MOBILE NO");
        mob = new JTextField();
        signup = new JButton("SIGN UP");
        reset = new JButton("RESET");
        p = new JLabel("(must contain a character,digit and special symbol)");

        lbl.setBounds(190, 0, 260, 50);
        name.setBounds(20, 60, 200, 40);
        namet.setBounds(220, 60, 300, 40);
        sur.setBounds(20, 105, 200, 40);
        surt.setBounds(220, 105, 300, 40);
        dob.setBounds(20, 150, 200, 40);
        dobt.setBounds(220, 150, 300, 40);
        gen.setBounds(20, 195, 200, 40);
        male.setBounds(220, 195, 100, 40);
        female.setBounds(420, 195, 100, 40);
        user.setBounds(20, 240, 200, 40);
        usert.setBounds(220, 240, 300, 40);
        pass.setBounds(20, 285, 200, 40);
        passt.setBounds(220, 285, 300, 40);
        p.setBounds(220, 325, 300, 20);
        mobile.setBounds(20, 350, 200, 40);
        mob.setBounds(220, 350, 300, 40);
        signup.setBounds(150, 400, 200, 40);
        reset.setBounds(380, 400, 200, 40);

        container.add(lbl);
        lbl.setFont(f);
        container.add(name);
        name.setFont(f1);
        container.add(namet);
        namet.setFont(f1);
        container.add(sur);
        sur.setFont(f1);
        container.add(surt);
        surt.setFont(f1);
        container.add(dob);
        dob.setFont(f1);
        container.add(dobt);
        dobt.setFont(f1);
        container.add(user);
        user.setFont(f1);
        container.add(usert);
        usert.setFont(f1);
        container.add(pass);
        pass.setFont(f1);
        container.add(passt);
        passt.setFont(f1);
        container.add(mobile);
        mobile.setFont(f1);
        container.add(mob);
        mob.setFont(f1);
        container.add(signup);
        signup.setFont(f1);
        container.add(gen);
        gen.setFont(f1);
        container.add(male);
        male.setFont(f1);
        container.add(female);
        female.setFont(f1);
        container.add(p);
        container.add(reset);
        reset.setFont(f1);

        menubar = new JMenuBar();
        back = new JMenuItem("Back");
        menubar.add(back);
        setJMenuBar(menubar);

        signup.addActionListener(this);
        back.addActionListener(this);
        reset.addActionListener(this);
        male.addItemListener(this);
        female.addItemListener(this);

        setSize(600, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == signup) {
            try {
                char ch[] = passt.getPassword();
                String temp = new String(ch);
                boolean alphabet = false, digit = false, symbol = false;

                /*int len=temp.length();
                 for(int i=0;i<len;i++)
                 {
                 if(temp.charAt(i)<=&&temp.charAt(i)>=)
                 alphabet=true;
                 if(temp.charAt(i)<=&&temp.charAt(i)>=)
                 digit=true;
                 if(temp.charAt(i)<=&&temp.charAt(i)>=)
                 symbbol=true;
                 }
                 if(alphabet&&digit&&symbol)
                 {*/
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "sahil12345");
                s = c.createStatement();
                connection = true;
                s.executeUpdate("insert into tblclients values ('" + namet.getText() + "','" + surt.getText() + "'," + dobt.getText() + ",'" + usert.getText() + "','" + temp + "'," + mob.getText() + ",'" + g + "')");
                JOptionPane.showMessageDialog(this, "You have successfully signed up");
                try {
                    c.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
                dispose();
                new Project();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        } else if (a.getSource() == back) {
            if (connection) {
                try {
                    c.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
            dispose();
            new Project();
        } else if (a.getSource() == reset) {
            namet.setText("");
            surt.setText("");
            dobt.setText("");
            usert.setText("");
            mob.setText("");
            passt.setText("");
        }
    }

    public void itemStateChanged(ItemEvent a) {
        if (a.getItemSelectable() == male) {
            g = "male";
        } else {
            g = "female";
        }
    }
}
