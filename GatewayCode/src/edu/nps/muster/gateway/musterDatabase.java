package edu.nps.muster.gateway;

import java.sql.*;

/**
 * Created by Micah on 8/18/16.
 *
 *  This is the interface with the Database for the IoT Muster Gateway Server -- it works fully
 */
public class musterDatabase {

    // Define the SQL variables so we don't have to do it every time in each method
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;

    public musterDatabase() throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://172.16.1.143:3306/muster", "musterAdmin", "musterAdmin");
            this.stmt = this.conn.createStatement();
        } catch (Exception e ) {
            toss("Could not connect to DB: " + e.getMessage());
        }
    }

    public musterDatabase(String server, String table, String username, String password) throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+table, username, password);
            this.stmt = this.conn.createStatement();
        } catch (Exception e ) {
            toss("Could not connect to DB: " + e.getMessage());
        }
    }

    public boolean didMusterToday (String email) throws Exception {
        if(!doesStudentExist(email))
            toss("Student does not exist");

        try {
            String query = "select count(*) from students, devices, musters " +
                    "WHERE UPPER(email) = ? and students.ID = devices.studentID " +
                    "and devices.ID = musters.deviceID " +
                    "and DATE(musters.datestamp) = CURRENT_DATE;";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, email.toUpperCase());
            //print(this.pstmt);
            ResultSet rs = this.pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            toss("Could not query DB: " + e.getMessage());
            return false;
        }
    }

    public boolean addMuster(String date, String studentMAC, String sensorID) throws Exception {
        if(!doesDeviceExist(studentMAC))
            toss("Device does not exist in DB");

        try {
            String query = "INSERT INTO musters (datestamp, deviceID, nodeID) " +
                    "  VALUES (?, (SELECT ID FROM devices WHERE UPPER(devices.MAC) = ?), ?);";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setTimestamp(1, Timestamp.valueOf(date));
            this.pstmt.setString(2, studentMAC);
            this.pstmt.setString(3, sensorID);
            int rs = this.pstmt.executeUpdate();

        } catch (Exception e) {
            toss("Could not update DB: " + e.getMessage());
        }
        return true;
    }

    public boolean doesDeviceExist(String studentMAC) throws Exception {
        try {
            String query = "select count(*) from devices WHERE UPPER(MAC) = ? ;";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, studentMAC.toUpperCase());
            ResultSet rs = this.pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            toss("Could not query DB: " + e.getMessage());
            return false;
        }
    }

    public String deviceToStudent(String studentMAC) throws Exception {
        try {
            String query =
                    "select CONCAT(lname, ', ', fname, ' <', email, '>') " +
                            "from students, devices " +
                            "WHERE UPPER(devices.MAC) = ? " +
                            "and devices.studentID = students.ID;";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, studentMAC.toUpperCase());
            ResultSet rs = this.pstmt.executeQuery();
            if (rs.next()) return rs.getString(1);
            else throw new Exception("No result");

        } catch (Exception e) {
            toss("Could not query DB: " + e.getMessage());
            return "";
        }
    }

    public boolean doesStudentExist (String email) throws Exception {
        try {
            String query = "select count(*) from students WHERE UPPER(email) = ?;";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, email.toUpperCase());
            ResultSet rs = this.pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            toss("Could not query DB: " + e.getMessage());
            return false;
        }
    }

    public boolean addStudent (String fname, String lname, String email) throws Exception {
        try {
            String query = "INSERT INTO students(fname, lname, email) VALUES (?, ?, ?); ";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, fname);
            this.pstmt.setString(2, lname);
            this.pstmt.setString(3, email);
            //print(this.pstmt);
            int rs = this.pstmt.executeUpdate();
            return rs > 0;

        } catch (Exception e) {
            toss("Could not update DB: " + e.getMessage());
            return false;
        }
    }

    public boolean addDevice (String studentEmail, String MAC) throws Exception {
        if(!doesStudentExist(studentEmail))
            toss("Student does not exist");

        try {
            String query = "INSERT INTO devices(studentID, MAC) VALUES ((SELECT ID FROM students WHERE students.email = ?), ?); ";
            this.pstmt = this.conn.prepareStatement(query);
            this.pstmt.setString(1, studentEmail);
            this.pstmt.setString(2, MAC);
            //print(this.pstmt);
            int rs = this.pstmt.executeUpdate();
            return rs > 0;

        } catch (Exception e) {
            toss("Could not update DB: " + e.getMessage());
            return false;
        }
    }

    private static void print(Object out) { System.out.println(out.toString()); }

    private static void toss(String msg) throws Exception { throw new Exception(msg); }

    public static void main(String args[]) {
        try {
            print(new musterDatabase().didMusterToday("mpakin1@nps.edu"));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
