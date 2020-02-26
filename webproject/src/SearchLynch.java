import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchLynch")
public class SearchLynch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchLynch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionLynch.getDBConnection(getServletContext());
//         DBConnectionLynch.getDBConnection();
         connection = DBConnectionLynch.connection;

         if (keyword == null || keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM techTable";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM techTable WHERE SUBJECT LIKE ? OR CLASS LIKE ? OR DAYS LIKE ? OR LOCATION LIKE ? OR TIME LIKE ?";
            String search = keyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, search);
            preparedStatement.setString(2, search);
            preparedStatement.setString(3, search);
            preparedStatement.setString(4, search);
            preparedStatement.setString(5, search);

            
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            String subject = rs.getString("subject").trim();
            String className = rs.getString("class").trim();
            String days = rs.getString("days").trim();
            String location = rs.getString("location").trim();
            String time = rs.getString("time").trim();


            if (keyword == null || keyword.isEmpty() || subject.contains(keyword) || className.contains(keyword) || days.contains(keyword) || location.contains(keyword) || time.contains(keyword)) {
               out.println("Subject: " + subject + ", ");
               out.println("Class: " + className + ", ");
               out.println("Days: " + days + ", ");
               out.println("Location: " + location + ", ");
               out.println("Time: " + time + "<br>");
            }
         }
         out.println("<a href=/webproject/searchLynch.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
